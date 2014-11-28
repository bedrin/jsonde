package com.jsonde.gui.action;

import com.jsonde.gui.ApplicationUserInterface;
import net.sf.sdedit.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ExitJSondeAction extends AbstractAction {

    private ApplicationUserInterface sdEditUIAdapter;

    public ExitJSondeAction(ApplicationUserInterface sdEditUIAdapter) {
        this.sdEditUIAdapter = sdEditUIAdapter;
    }

    {
        putValue(Action.SMALL_ICON,
                new ImageIcon(
                        Icons.class.getResource("exit.png")
                ));
        putValue(Action.NAME, "Exit jSonde");
        putValue(Action.SHORT_DESCRIPTION, "Exit jSonde");
    }

    public void actionPerformed(ActionEvent e) {

        System.exit(0);

    }

}