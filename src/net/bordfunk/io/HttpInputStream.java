/*
 *  rdio Internet Radio App
 *  Copyright (C) 2010-2013 Christian Lins <christian@lins.me>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.bordfunk.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A simple HTTP input stream.
 * 
 * @author Christian Lins
 */
public class HttpInputStream extends InputStream {

    private final InputStream  in;
    private final OutputStream out;
    private final String       url;
    private final String       host;
    private boolean            requested = false;

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
        while (!"".equals(readLine(in))) {
        }

        requested = true;
    }

    private String readLine(InputStream in) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (;;) {
            byte b = (byte) in.read();
            if (b == '\n') {
                break;
            } else if (b == '\r') {
                // Ignore
            } else {
                buf.write(b);
            }
        }
        return buf.toString();
    }

    public int read() throws IOException {
        byte[] smallbuf = new byte[1];
        if (read(smallbuf, 0, 1) == 1) {
            return smallbuf[0];
        } else {
            return -1;
        }
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        if (!requested) {
            sendRequest();
        }
        return this.in.read(buf, off, len);
    }
}
