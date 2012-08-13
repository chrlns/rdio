/*
 *  Bordfunk Internet Radio App
 *  Copyright (C) 2010-2012 Christian Lins <christian@lins.me>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.bordfunk;

import net.bordfunk.forms.StationsList;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

/**
 * Main MIDlet of this app.
 * @author Christian Lins
 */
public class RadioMIDlet extends MIDlet {

	private Displayable currentDisplay = new StationsList(this);

	public RadioMIDlet() {
	}

	public void startApp() {
		Display.getDisplay(this).setCurrent(currentDisplay);
	}

	public void pauseApp() {
		currentDisplay = Display.getDisplay(this).getCurrent();
	}

	public void destroyApp(boolean unconditional) {
	}
}
