package com.jsonde.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JActionLabel extends JLabel implements MouseListener {

    private Action action;

    public JActionLabel(Action action) {

        super();

        Object nameObject = action.getValue(Action.NAME);
        if (null != nameObject) {
            super.setText((String) nameObject);
        }

        this.action = action;

        addMouseListener(this);

    }

    private final static String HOVER_PRE_HTML = "<html><u><font color=\"blue\">";

    public void mouseEntered(MouseEvent e) {
        super.setText(HOVER_PRE_HTML + getText());
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void mouseExited(MouseEvent e) {
        super.setText(getText().substring(HOVER_PRE_HTML.length()));
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void setText(String text) {
        if (getText().startsWith(HOVER_PRE_HTML)) {
            super.setText(HOVER_PRE_HTML + text);
        } else {
            super.setText(text);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {

        Component component = e.getComponent();
        if (component.contains(e.getPoint())) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "labelClicked");
            action.actionPerformed(event);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

}