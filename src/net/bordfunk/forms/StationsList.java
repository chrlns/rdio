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
