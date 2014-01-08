/*
 *  rdio Internet Radio App
 *  Copyright (C) 2010-2013 Christian Lins <christian@lins.me>
 *
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
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
 * Form that display information about this app.
 * 
 * @author Christian Lins
 */
public class AboutForm extends Form implements CommandListener {

    public static final Command BACK = new Command("Back", null, Command.BACK, 0);

    private final RadioMIDlet   midlet;

    public AboutForm(RadioMIDlet midlet) {
        super("About this app");

        this.midlet = midlet;

        append(new StringItem("Name", midlet.getAppProperty("MIDlet-Name")));
        append(new StringItem("Version", midlet.getAppProperty("MIDlet-Version")));
        append(new StringItem("Autor", midlet.getAppProperty("MIDlet-Vendor")));

        addCommand(BACK);
        setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable disp) {
        if (cmd.equals(BACK)) {
            Display.getDisplay(midlet).setCurrent(new StationsList(midlet));
        }
    }
}
