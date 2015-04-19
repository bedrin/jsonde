package com.jsonde.gui.action;

import com.jsonde.gui.ApplicationUserInterface;
import com.jsonde.gui.dialog.about.AboutDialog;
import com.jsonde.gui.sdedit.SdEditUIAdapter;
import net.sf.sdedit.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AboutAction extends AbstractAction {

    private ApplicationUserInterface applicationUserInterface;

    public AboutAction(SdEditUIAdapter sdEditUIAdapter) {
        this.applicationUserInterface = sdEditUIAdapter;
    }

    {
        putValue(Action.SMALL_ICON,
                new ImageIcon(
                        Icons.class.getResource("help.png")
                ));
        putValue(Action.NAME, "About");
        putValue(Action.SHORT_DESCRIPTION, "About jSonde");
    }

    public void actionPerformed(ActionEvent e) {

        AboutDialog aboutDialog = new AboutDialog();

        aboutDialog.setVisible(true);

    }

}