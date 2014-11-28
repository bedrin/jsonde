package com.jsonde.gui.action;

import com.jsonde.gui.sdedit.SdEditUIAdapter;
import net.sf.sdedit.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAllDiagramsAction extends AbstractAction {

    private SdEditUIAdapter sdEditUIAdapter;

    public CloseAllDiagramsAction(SdEditUIAdapter sdEditUIAdapter) {
        this.sdEditUIAdapter = sdEditUIAdapter;
    }

    {
        putValue(Action.SMALL_ICON,
                new ImageIcon(
                        Icons.class.getResource("close.png")
                ));
        putValue(Action.NAME, "Close All");
        putValue(Action.SHORT_DESCRIPTION, "Close all tabs");
    }

    public void actionPerformed(ActionEvent e) {

        while (sdEditUIAdapter.getUserInterface().removeCurrentTab(false)) {
        }
        ;

    }

}
