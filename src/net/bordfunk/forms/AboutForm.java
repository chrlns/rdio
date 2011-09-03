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
import javax.microedition.lcdui.StringItem;
import net.bordfunk.RadioMIDlet;

/**
 *
 * @author Christian Lins
 */
public class AboutForm extends Form implements CommandListener {

	public static final Command BACK = new Command("Zurück", null, Command.BACK, 0);

	private RadioMIDlet midlet;

	public AboutForm(RadioMIDlet midlet) {
		super("Über diese Anwendung");

		this.midlet = midlet;

		append(new StringItem("Name", midlet.getAppProperty("MIDlet-Name")));
		append(new StringItem("Version", midlet.getAppProperty("MIDlet-Version")));
		append(new StringItem("Autor", midlet.getAppProperty("MIDlet-Vendor")));

		addCommand(BACK);
		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if(cmd.equals(BACK)) {
			Display.getDisplay(midlet).setCurrent(new StationsList(midlet));
		}
	}
}
