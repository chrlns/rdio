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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import net.bordfunk.RadioPlayerListener;

/**
 * Thread reading from InputStream in byte buffers.
 * @author Christian Lins
 */
class ChunkWorker extends Thread {

	public static final int BUFFERSIZE = 2048;

	private InputStream in;
	private OutputStream out;
	private int len;
	private Vector listeners = new Vector();

	public ChunkWorker(OutputStream out, InputStream in, int len) {
		this.out = out;
		this.in = in;
		this.len = len;
	}

	public void addListener(RadioPlayerListener listener) {
		this.listeners.addElement(listener);
	}

	protected void fireStatusEvent(int level, int max) {
		for(int n = 0; n < listeners.size(); n++) {
			((RadioPlayerListener)listeners.elementAt(n)).bufferStatus(level, max);
		}
	}

	protected void fireExceptionOccurredEvent(Exception ex) {
		for(int n = 0; n < listeners.size(); n++) {
			((RadioPlayerListener)listeners.elementAt(n)).exceptionOccurred(ex);
		}
	}

	public void run() {
		try {
			byte[] buf = new byte[BUFFERSIZE];
			int read = 0;
			int total = len;
			while(len > 0) {
				read = in.read(buf);
				if(read != -1) {
					out.write(buf, 0, read);
					len -= read;
				} else {
					break;
				}
				fireStatusEvent(total - len, total);
			}
		} catch(Exception ex) {
			fireExceptionOccurredEvent(ex);
		}
	}
}
