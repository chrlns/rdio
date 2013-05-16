/*
 *  Bordfunk Internet Radio App
 *  Copyright (C) 2010-2013 Christian Lins <christian@lins.me>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.bordfunk;

import javax.microedition.media.PlayerListener;

/**
 * 
 * @author Christian Lins
 */
public interface RadioPlayerListener extends PlayerListener {

    void exceptionOccurred(Exception ex);

    void bufferStatus(int level, int max);

    void statusEvent(String msg);
}
