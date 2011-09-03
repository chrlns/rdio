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

import java.util.Timer;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.StringItem;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import net.bordfunk.RadioMIDlet;
import net.bordfunk.RadioPlayer;
import net.bordfunk.RadioPlayerListener;
import net.bordfunk.Stations;

/**
 *
 * @author Christian Lins
 */
public class RadioPlayerView extends Form implements RadioPlayerListener, CommandListener {

	public static final Command CMD_START = new Command("Start", Command.OK, 5);
	public static final Command CMD_EXIT  = new Command("Ende", Command.EXIT, 0);

	private Command cmdBack	= new Command("Zurück", Command.BACK, 0);
	private RadioPlayer player = null;
	private RadioMIDlet midlet;
	private Timer timer = new Timer();
	private long runtime;

	public RadioPlayerView(int stationIdx, RadioMIDlet midlet) {
		super("Sender: " + Stations.NAMES[stationIdx]);
		setCommandListener(this);

		this.midlet = midlet;
		append(new StringItem(null, Stations.DESCRIPTIONS[stationIdx]));
		append(new Spacer(getWidth(), 5));
		append(new StringItem("Status", "Starte..."));

		addCommand(cmdBack);
	}

	public void commandAction(Command c, Displayable d) {
		try {
			if(c.equals(cmdBack)) {
				this.player.stopPlayer();
				Display.getDisplay(midlet).setCurrent(new StationsList(midlet));
			}
		} catch(Exception ex) {
			exceptionOccurred(ex);
		}
	}

	protected void setStatusText(String text) {
		set(2, new StringItem("Status", text));
		//append(text);
	}

	public void playerUpdate(Player player, String event, Object eventData) {
		setStatusText(event);

		try {
			if(event.equals(PlayerListener.STARTED)) {
				setStatusText("Gestartet.");
				runtime = System.currentTimeMillis();
			} else if(event.equals(PlayerListener.END_OF_MEDIA)) {
				this.player.restartPlayer();
			}
		} catch(Exception ex) {
			exceptionOccurred(ex);
		}
	}

	public void exceptionOccurred(Exception ex) {
		setStatusText(ex.toString());

		// An exception means in most cases that the connection was lost or
		// the mimetype of the stream is not supported
		//this.player.restartPlayer();
	}

	public void bufferStatus(int level, int max) {
		setStatusText("Puffern... " + Integer.toString((level * 100) / max) + "%");
	}

	public void statusEvent(String msg) {
		if(msg.equals(PlayerListener.END_OF_MEDIA)) {
			setStatusText("Verbindung zu langsam!");
		} else {
			setStatusText(msg);
		}
	}
	
	public void setPlayer(RadioPlayer player) {
		this.player = player;
		player.addPlayerListener(this);
	}

}
