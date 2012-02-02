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
