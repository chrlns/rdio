/*
 *  Bordfunk Internet Radio App
 *  Copyright (C) 2010-2013 Christian Lins <christian@lins.me>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
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
    public static final Command CMD_EXIT  = new Command("Exit", Command.EXIT, 0);

    private final Command       cmdBack   = new Command("Back", Command.BACK, 0);
    private RadioPlayer         player    = null;
    private final RadioMIDlet   midlet;
    private final Timer         timer     = new Timer();
    private long                runtime;

    public RadioPlayerView(int stationIdx, RadioMIDlet midlet) {
        super("Station: " + Stations.NAMES[stationIdx]);
        setCommandListener(this);

        this.midlet = midlet;
        append(new StringItem(null, Stations.DESCRIPTIONS[stationIdx]));
        append(new Spacer(getWidth(), 5));
        append(new StringItem("Status", "Starting..."));

        addCommand(cmdBack);
    }

    public void commandAction(Command c, Displayable d) {
        try {
            if (c.equals(cmdBack)) {
                this.player.stopPlayer();
                Display.getDisplay(midlet).setCurrent(new StationsList(midlet));
            }
        } catch (Exception ex) {
            exceptionOccurred(ex);
        }
    }

    protected void setStatusText(String text) {
        set(2, new StringItem("Status", text));
        // append(text);
    }

    public void playerUpdate(Player player, String event, Object eventData) {
        setStatusText(event);

        try {
            if (event.equals(PlayerListener.STARTED)) {
                setStatusText("Started.");
                runtime = System.currentTimeMillis();
            } else if (event.equals(PlayerListener.END_OF_MEDIA)) {
                this.player.restartPlayer();
            }
        } catch (Exception ex) {
            exceptionOccurred(ex);
        }
    }

    public void exceptionOccurred(Exception ex) {
        setStatusText(ex.toString());

        // An exception means in most cases that the connection was lost or
        // the mimetype of the stream is not supported
        // this.player.restartPlayer();
    }

    public void bufferStatus(int level, int max) {
        setStatusText("Buffering... " + Integer.toString((level * 100) / max) + "%");
    }

    public void statusEvent(String msg) {
        if (msg.equals(PlayerListener.END_OF_MEDIA)) {
            setStatusText("Connection to slow!");
        } else {
            setStatusText(msg);
        }
    }

    public void setPlayer(RadioPlayer player) {
        this.player = player;
        player.addPlayerListener(this);
    }

}
