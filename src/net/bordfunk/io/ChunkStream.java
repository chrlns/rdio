/*
 *  rdio Internet Radio App
 *  Copyright (C) 2010-2013 Christian Lins <christian@lins.me>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.bordfunk.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.bordfunk.RadioPlayerListener;

/**
 * Reads from an online source and chunks the data into blocks. Mobile devices
 * tend to buffer ALL data they get from an InputStream, which is really bad for
 * online radio stream (which never ends). So we read a chunk of data, virtually
 * return EOF and restart the Player again with the same ChunkStream, that
 * internally returns the next chunk.
 * 
 * @author Christian Lins
 */
public class ChunkStream extends InputStream {

    public static final int      CHUNKSIZE_MIN = 16384;       // 16k

    private ByteArrayInputStream buf           = null;
    private int                  chunksize;
    private ChunkOutputBuffer    out_front;
    private ChunkOutputBuffer    out_back;
    private InputStream          in            = null;
    private ChunkWorker          currentWorker = null;
    private HttpConnection       conn          = null;
    private String               contentType   = "audio/mpeg";

    public ChunkStream() {
        // Create front and back buffers
        System.out.println("Free memory: " + Runtime.getRuntime().freeMemory() / 1024 + "k");
        chunksize = (int) (Runtime.getRuntime().freeMemory() / 8);
        chunksize -= chunksize % 8;
        while (chunksize >= CHUNKSIZE_MIN) {
            try {
                out_front = new ChunkOutputBuffer(chunksize);
                out_back = new ChunkOutputBuffer(chunksize);
            } catch (OutOfMemoryError err) {
                System.out.println("Chunksize " + (chunksize / 1024) + "k is too big");
                chunksize = chunksize - CHUNKSIZE_MIN;
                out_front = null;
                out_back = null;
                System.gc();
            }
            break;
        }
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setURL(String host, String url) throws IOException {
        closeStream();

        conn = (HttpConnection) Connector.open("http://" + host + url, Connector.READ, true);
        conn.setRequestMethod(HttpConnection.GET);
        if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
            in = conn.openInputStream();

            // Retrieve MIME type of the stream
            contentType = conn.getHeaderField("Content-Type");
        } else {
            throw new IOException("Unexpected HTTP return: " + conn.getResponseCode());
        }
    }

    /**
     * Close is probably called by the MMAPI's Player. But that's bad timing.
     */
    public void close() {
        // Do nothing
    }

    public void closeStream() throws IOException {
        if (in != null) {
            in.close();
            in = null;
        }
        if (conn != null) {
            conn.close();
            conn = null;
        }

        try {
            if (currentWorker != null) {
                currentWorker.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        // Reset of buffers is necessary otherwise after selecting a new
        // channel a few seconds of the old channel is played
        out_front.reset();
        out_back.reset();
    }

    public void prebuffer(Vector listeners) {
        // Fill front buffer again
        currentWorker = new ChunkWorker(this.out_front, in, chunksize);
        for (int n = 0; n < listeners.size(); n++) {
            currentWorker.addListener((RadioPlayerListener) listeners.elementAt(n));
        }
        currentWorker.run();
    }

    public void nextChunk() throws InterruptedException {
        if (currentWorker != null) {
            currentWorker.join();
        }

        this.buf = new ByteArrayInputStream(out_front.getBackbuffer(), 0, out_front.size());

        // Switch buffers
        ChunkOutputBuffer tmp = this.out_front;
        this.out_front = this.out_back;
        this.out_back = tmp;

        // Fill front buffer again
        this.out_front.reset();
        currentWorker = new ChunkWorker(this.out_front, in, chunksize);
        currentWorker.start();
    }

    public int read() throws IOException {
        if (buf == null) {
            return -1;
        }

        return this.buf.read();
    }

    public int read(byte[] buf, int off, int len) {
        return this.buf.read(buf, off, len);
    }

}
