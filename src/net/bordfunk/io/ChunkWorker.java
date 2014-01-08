/*
 *  rdio Internet Radio App
 *  Copyright (C) 2010-2013 Christian Lins <christian@lins.me>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.bordfunk.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import net.bordfunk.RadioPlayerListener;

/**
 * Thread reading from InputStream in byte buffers.
 * 
 * @author Christian Lins
 */
class ChunkWorker extends Thread {

    public static final int    BUFFERSIZE = 2048;

    private final InputStream  in;
    private final OutputStream out;
    private int                len;
    private final Vector       listeners  = new Vector();

    public ChunkWorker(OutputStream out, InputStream in, int len) {
        this.out = out;
        this.in = in;
        this.len = len;
    }

    public void addListener(RadioPlayerListener listener) {
        this.listeners.addElement(listener);
    }

    protected void fireStatusEvent(int level, int max) {
        for (int n = 0; n < listeners.size(); n++) {
            ((RadioPlayerListener) listeners.elementAt(n)).bufferStatus(level, max);
        }
    }

    protected void fireExceptionOccurredEvent(Exception ex) {
        for (int n = 0; n < listeners.size(); n++) {
            ((RadioPlayerListener) listeners.elementAt(n)).exceptionOccurred(ex);
        }
    }

    public void run() {
        try {
            byte[] buf = new byte[BUFFERSIZE];
            int read = 0;
            int total = len;
            while (len > 0) {
                read = in.read(buf);
                if (read != -1) {
                    out.write(buf, 0, read);
                    len -= read;
                } else {
                    break;
                }
                fireStatusEvent(total - len, total);
            }
        } catch (Exception ex) {
            fireExceptionOccurredEvent(ex);
        }
    }
}
