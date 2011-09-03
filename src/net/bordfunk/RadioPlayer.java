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

import net.bordfunk.io.ChunkStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

/**
 *
 * @author Christian Lins
 */
public class RadioPlayer {

	private static ChunkStream IN = new ChunkStream();

	private Player player = null;
	private int stationIdx;
	private Vector listeners = new Vector();

	public RadioPlayer(int stationsIdx) {
		this.stationIdx = stationsIdx;
	}

	public void addPlayerListener(RadioPlayerListener listener) {
		listeners.addElement(listener);
	}

	public void stopPlayer() throws IOException, MediaException {
		if(this.player != null) {
			this.player.stop();
		}
		if(IN != null) {
			IN.closeStream();
		}
	}

	public void startPlayer() {
		Runnable runner = new Runnable() {
			public void run() {
				try {
					fireStatusEvent("Verbinden...");
					IN.setURL(Stations.HOSTS[stationIdx], Stations.URLS[stationIdx]);
					fireStatusEvent("Vorpuffern...");
					IN.prebuffer(listeners);
					restartPlayer();
				} catch(Exception ex) {
					fireExecptionOccurredEvent(ex);
				}
			}
		};
		new Thread(runner).start();
	}

	public void restartPlayer() {
		try {
			fireStatusEvent("Warte...");
			IN.nextChunk();

			// Free old player
			if (player != null) {
				for (int n = 0; n < listeners.size(); n++) {
					this.player.removePlayerListener((PlayerListener)listeners.elementAt(n));
				}
				this.player.close();
			}

			fireStatusEvent("Erzeuge Player...");
			this.player = Manager.createPlayer(IN, IN.getContentType());

			for (int n = 0; n < listeners.size(); n++) {
				this.player.addPlayerListener((PlayerListener)listeners.elementAt(n));
			}
			this.player.realize();

			VolumeControl vcon = (VolumeControl) this.player.getControl("VolumeControl");
			vcon.setLevel(100);

			fireStatusEvent("Starte...");
			this.player.start();
		} catch (Exception ex) {
			fireExecptionOccurredEvent(ex);
		}
	}

	protected void fireStatusEvent(String msg) {
		for (int n = 0; n < listeners.size(); n++) {
			((RadioPlayerListener)listeners.elementAt(n)).statusEvent(msg);
		}
	}

	protected void fireExecptionOccurredEvent(Exception ex) {
		for (int n = 0; n < listeners.size(); n++) {
			((RadioPlayerListener)listeners.elementAt(n)).exceptionOccurred(ex);
		}
	}
}
