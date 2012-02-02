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
