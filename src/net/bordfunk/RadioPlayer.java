/*
 *  Bordfunk Internet Radio App
 *  Copyright (C) 2010-2013 Christian Lins <christian@lins.me>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.bordfunk;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

import net.bordfunk.io.ChunkStream;

/**
 * 
 * @author Christian Lins
 */
public class RadioPlayer {

    private static ChunkStream IN        = new ChunkStream();

    private Player             player    = null;
    private final int          stationIdx;
    private final Vector       listeners = new Vector();

    public RadioPlayer(int stationsIdx) {
        this.stationIdx = stationsIdx;
    }

    public void addPlayerListener(RadioPlayerListener listener) {
        listeners.addElement(listener);
    }

    public void stopPlayer() throws IOException, MediaException {
        if (this.player != null) {
            this.player.stop();
        }
        if (IN != null) {
            IN.closeStream();
        }
    }

    public void startPlayer() {
        Runnable runner = new Runnable() {
            public void run() {
                try {
                    fireStatusEvent("Connecting...");
                    IN.setURL(Stations.HOSTS[stationIdx], Stations.URLS[stationIdx]);
                    fireStatusEvent("Buffering...");
                    IN.prebuffer(listeners);
                    restartPlayer();
                } catch (Exception ex) {
                    fireExecptionOccurredEvent(ex);
                }
            }
        };
        new Thread(runner).start();
    }

    public void restartPlayer() {
        try {
            fireStatusEvent("Wait...");
            IN.nextChunk();

            // Free old player
            if (player != null) {
                for (int n = 0; n < listeners.size(); n++) {
                    this.player.removePlayerListener((PlayerListener) listeners.elementAt(n));
                }
                this.player.close();
            }

            fireStatusEvent("Create Player...");
            this.player = Manager.createPlayer(IN, IN.getContentType());

            for (int n = 0; n < listeners.size(); n++) {
                this.player.addPlayerListener((PlayerListener) listeners.elementAt(n));
            }
            this.player.realize();

            VolumeControl vcon = (VolumeControl) this.player.getControl("VolumeControl");
            vcon.setLevel(100);

            fireStatusEvent("Starting...");
            this.player.start();
        } catch (Exception ex) {
            fireExecptionOccurredEvent(ex);
        }
    }

    protected void fireStatusEvent(String msg) {
        for (int n = 0; n < listeners.size(); n++) {
            ((RadioPlayerListener) listeners.elementAt(n)).statusEvent(msg);
        }
    }

    protected void fireExecptionOccurredEvent(Exception ex) {
        for (int n = 0; n < listeners.size(); n++) {
            ((RadioPlayerListener) listeners.elementAt(n)).exceptionOccurred(ex);
        }
    }
}
