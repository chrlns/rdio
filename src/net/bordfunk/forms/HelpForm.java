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
