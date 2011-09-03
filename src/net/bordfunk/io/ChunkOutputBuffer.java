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

/**
 * An adapted @see{ByteArrayOutputStream} that simply provides access to
 * the stream's backbuffer byte array which is originally protected.
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
