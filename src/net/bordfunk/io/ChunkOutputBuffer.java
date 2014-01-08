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

/**
 * An adapted @see{ByteArrayOutputStream} that simply provides access to the
 * stream's backbuffer byte array which is originally protected.
 * 
 * @author Christian Lins
 */
class ChunkOutputBuffer extends ByteArrayOutputStream {

    protected ChunkOutputBuffer() {
    }

    public ChunkOutputBuffer(int size) {
        super(size);
    }

    public byte[] getBackbuffer() {
        return this.buf;
    }
}
