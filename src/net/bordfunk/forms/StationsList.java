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

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import net.bordfunk.RadioMIDlet;
import net.bordfunk.RadioPlayer;
import net.bordfunk.Stations;

/**
 *
 * @author Christian Lins
 */
public class StationsList extends Form implements CommandListener {

	public static final Command ABOUT = new Command("Über", "Über Bordfunk", Command.HELP, 0);
	public static final Command HELP = new Command("Hilfe", Command.HELP, 1);

	private RadioMIDlet midlet;
	private ChoiceGroup stationChoice =
		new ChoiceGroup("Wähle einen Stream:", ChoiceGroup.EXCLUSIVE, Stations.NAMES, null);

	public StationsList(RadioMIDlet midlet) {
		super("Internet Radiosender");
		this.midlet = midlet;

		append(stationChoice);

		addCommand(RadioPlayerView.CMD_START);
		addCommand(RadioPlayerView.CMD_EXIT);
		addCommand(ABOUT);
		addCommand(HELP);

		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable disp) {
		if(cmd.equals(RadioPlayerView.CMD_START)) {
			RadioPlayerView playerView =
					new RadioPlayerView(stationChoice.getSelectedIndex(), midlet);
			Display.getDisplay(midlet).setCurrent(playerView);
			RadioPlayer player = new RadioPlayer(stationChoice.getSelectedIndex());
			playerView.setPlayer(player);
			player.startPlayer();
		} else if(cmd.equals(RadioPlayerView.CMD_EXIT)) {
			this.midlet.destroyApp(false);
			this.midlet.notifyDestroyed();
		} else if(cmd.equals(ABOUT)) {
			Display.getDisplay(midlet).setCurrent(new AboutForm(midlet));
		} else if(cmd.equals(HELP)) {
			Display.getDisplay(midlet).setCurrent(new HelpForm(midlet));
		}
	}
}
