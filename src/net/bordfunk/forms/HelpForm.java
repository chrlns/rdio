/*
 *  Bordfunk Internet Radio App
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
import javax.microedition.lcdui.Spacer;

import net.bordfunk.RadioMIDlet;

/**
 * 
 * @author Christian Lins
 */
public class HelpForm extends Form implements CommandListener {

    public static final Command BACK = new Command("Back", Command.BACK, 0);

    private final RadioMIDlet   midlet;

    public HelpForm(RadioMIDlet midlet) {
        super("Help");
        setCommandListener(this);

        this.midlet = midlet;

        addCommand(BACK);

        append("Choose a stream and press 'Start' to start listening.");
        append(new Spacer(getWidth(), 1));
        append("Please note that streaming requires high data traffic.");
        append(new Spacer(getWidth(), 1));
        append("Data flat plan or Wifi is highly recommended.");
    }

    public void commandAction(Command cmd, Displayable disp) {
        if (cmd.equals(BACK)) {
            Display.getDisplay(midlet).setCurrent(new StationsList(midlet));
        }
    }
}
