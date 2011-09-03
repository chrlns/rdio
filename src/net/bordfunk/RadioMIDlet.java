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
