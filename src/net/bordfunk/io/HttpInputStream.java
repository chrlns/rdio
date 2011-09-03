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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Christian Lins
 */
public class HttpInputStream extends InputStream {

	private InputStream in;
	private OutputStream out;
	private String url;
	private String host;
	private boolean requested = false;

	public HttpInputStream(String host, String url, InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
		this.url = url;
		this.host = host;
	}

	private void sendRequest() throws IOException {
		// Send initial HTTP request
		out.write("GET ".getBytes());
		out.write(this.url.getBytes());
		out.write(" HTTP/1.1\n".getBytes());
		out.write("Host: ".getBytes());
		out.write(this.host.getBytes());
		out.write("\n\n".getBytes());
		out.flush();

		// Read reply
		System.out.println(readLine(in));
		while(!"".equals(readLine(in))) {
		}

		requested = true;
	}

	private String readLine(InputStream in) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		for(;;) {
			byte b = (byte)in.read();
			if(b == '\n') {
				break;
			} else if(b == '\r') {
				// Ignore
			} else {
				buf.write(b);
			}
		}
		return buf.toString();
	}

	public int read() throws IOException {
		byte[] smallbuf = new byte[1];
		if(read(smallbuf, 0, 1) == 1) {
			return smallbuf[0];
		} else {
			return -1;
		}
	}

	public int read(byte[] buf, int off, int len) throws IOException {
		if(!requested) {
			sendRequest();
		}
		return this.in.read(buf, off, len);
	}
}
