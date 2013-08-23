/*
 *  rdio Internet Radio App
 *  Copyright (C) 2010-2013 Christian Lins <christian@lins.me>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
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
 * List that shows stations.
 * 
 * @author Christian Lins
 */
public class StationsList extends Form implements CommandListener {

    public static final Command ABOUT         = new Command("About", "About rdio",
                                                      Command.HELP, 0);
    public static final Command HELP          = new Command("Help", Command.HELP, 1);

    private final RadioMIDlet   midlet;
    private final ChoiceGroup   stationChoice = new ChoiceGroup("Choose a station:",
                                                      ChoiceGroup.EXCLUSIVE, Stations.NAMES,
                                                      null);

    public StationsList(RadioMIDlet midlet) {
        super("Radio Stations");
        this.midlet = midlet;

        append(stationChoice);

        addCommand(RadioPlayerView.CMD_START);
        addCommand(RadioPlayerView.CMD_EXIT);
        addCommand(ABOUT);
        addCommand(HELP);

        setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable disp) {
        if (cmd.equals(RadioPlayerView.CMD_START)) {
            int selIdx = stationChoice.getSelectedIndex();
            RadioPlayerView playerView = new RadioPlayerView(selIdx, midlet);
            RadioPlayer player = new RadioPlayer(selIdx);
            Display.getDisplay(midlet).setCurrent(playerView);
            playerView.setPlayer(player);
            player.startPlayer();
        } else if (cmd.equals(RadioPlayerView.CMD_EXIT)) {
            this.midlet.destroyApp(false);
            this.midlet.notifyDestroyed();
        } else if (cmd.equals(ABOUT)) {
            Display.getDisplay(midlet).setCurrent(new AboutForm(midlet));
        } else if (cmd.equals(HELP)) {
            Display.getDisplay(midlet).setCurrent(new HelpForm(midlet));
        }
    }
}
