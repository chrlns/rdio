/*
 *  Bordfunk Internet Radio App
 *  Copyright (C) 2010-2012 Christian Lins <christian@lins.me>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
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
