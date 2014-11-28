package com.jsonde.gui.action;

import com.jsonde.gui.sdedit.SdEditUIAdapter;
import net.sf.sdedit.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseDiagramAction extends AbstractAction {

    private SdEditUIAdapter sdEditUIAdapter;

    public CloseDiagramAction(SdEditUIAdapter sdEditUIAdapter) {
        this.sdEditUIAdapter = sdEditUIAdapter;
    }

    {
        putValue(Action.SMALL_ICON,
                new ImageIcon(
                        Icons.class.getResource("close.png")
                ));
        putValue(Action.NAME, "Close Current Tab");
        putValue(Action.SHORT_DESCRIPTION, "Close current tab");
    }

    public void actionPerformed(ActionEvent e) {

        sdEditUIAdapter.getUserInterface().removeCurrentTab(false);

    }

}