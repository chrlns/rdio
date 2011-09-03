/*
 *   Bordfunk - Internet Radio App
 *   Copyright (C) 2010-2011 Christian Lins <christian@lins.me>
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
