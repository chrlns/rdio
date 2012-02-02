/*
 *  Bordfunk Internet Radio App
 *  Copyright (C) 2010-2011 Christian Lins <christian@lins.me>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package net.bordfunk.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
//import javax.microedition.io.SocketConnection;
import net.bordfunk.RadioPlayerListener;

/**
 * Reads from an online source and chunks the data into blocks.
 * Mobile devices tend to buffer ALL data they get from an InputStream,
 * which is really bad for online radio stream (which never ends).
 * So we read a chunk of data, virtually return EOF and restart the Player
 * again with the same ChunkStream, that internally returns the next chunk.
 * @author christian
 */
public class ChunkStream extends InputStream {

	public static final int CHUNKSIZE = 196608; // 196k

	private ByteArrayInputStream buf = null;
	private ChunkOutputBuffer out_front = new ChunkOutputBuffer(CHUNKSIZE);
	private ChunkOutputBuffer out_back = new ChunkOutputBuffer(CHUNKSIZE);
	private InputStream in = null;
	private ChunkWorker currentWorker = null;
	private HttpConnection conn = null;
	//private SocketConnection conn = null;
	private String contentType = "audio/mpeg";


	public ChunkStream() {
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setURL(String host, String url) throws IOException {
		closeStream();

		/*this.conn = (SocketConnection)
				Connector.open("socket://" + host + ":80", Connector.READ, true);
		this.in = new HttpInputStream(host, url,
				conn.openInputStream(), conn.openOutputStream());*/
		conn = (HttpConnection)Connector.open("http://" + host + url, Connector.READ, true);
		conn.setRequestMethod(HttpConnection.GET);
		if(conn.getResponseCode() == HttpConnection.HTTP_OK) {
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
		if(in != null) {
			in.close();
			in = null;
		}
		if(conn != null) {
			conn.close();
			conn = null;
		}

		try {
			if(currentWorker != null) {
				currentWorker.join();
			}
		} catch(InterruptedException ex) {
			ex.printStackTrace();
		}

		// Reset of buffers is necessary otherwise after selecting a new
		// channel a few seconds of the old channel is played
		out_front.reset();
		out_back.reset();
	}

	public void prebuffer(Vector listeners) {
		// Fill front buffer again
		currentWorker = new ChunkWorker(this.out_front, in, CHUNKSIZE);
		for(int n = 0; n < listeners.size(); n++) {
			currentWorker.addListener((RadioPlayerListener)listeners.elementAt(n));
		}
		currentWorker.run();
	}

	public void nextChunk() throws InterruptedException {
		if(currentWorker != null) {
			currentWorker.join();
		}

		this.buf = new ByteArrayInputStream(
				out_front.getBackbuffer(), 0, out_front.size());

		// Switch buffers
		ChunkOutputBuffer tmp = this.out_front;
		this.out_front = this.out_back;
		this.out_back = tmp;

		// Fill front buffer again
		this.out_front.reset();
		currentWorker = new ChunkWorker(this.out_front, in, CHUNKSIZE);
		currentWorker.start();
	}

	public int read() throws IOException {
		if(buf == null) {
			return -1;
		}

		return this.buf.read();
	}

	public int read(byte[] buf, int off, int len) {
		return this.buf.read(buf, off, len);
	}

}
