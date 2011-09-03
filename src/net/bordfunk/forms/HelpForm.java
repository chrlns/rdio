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
package net.bordfunk.forms;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Spacer;
import net.bordfunk.RadioMIDlet;

/**
 *
 * @author Christian Lins
 */
public class HelpForm extends Form implements CommandListener {

	public static final Command BACK = new Command("Zurück", Command.BACK, 0);

	private RadioMIDlet midlet;

	public HelpForm(RadioMIDlet midlet) {
		super("Hilfe");
		setCommandListener(this);

		this.midlet = midlet;

		addCommand(BACK);

		append("Wähle einen Stream und drücke 'Start' um mit dem Streamen zu beginnen.");
		append(new Spacer(getWidth(), 1));
		append("Bitte beachte, dass das Streamen von Internetradio hohen Datentraffic verursachen kann. ");
		append("Eine Datenflat oder WLAN-Verbindung (sofern verfügbar) wird dringend empfohlen.");
	}

	public void commandAction(Command cmd, Displayable disp) {
		if(cmd.equals(BACK)) {
			Display.getDisplay(midlet).setCurrent(new StationsList(midlet));
		}
	}
}
