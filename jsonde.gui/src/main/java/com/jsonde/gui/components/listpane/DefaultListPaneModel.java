package com.jsonde.gui.components.listpane;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class DefaultListPaneModel implements ListPaneModel {

    private List<String> labelsList;
    private List<Icon> iconsList;
    private List<Action> actionsList;

    public DefaultListPaneModel() {
        this.labelsList = new LinkedList<String>();
        this.iconsList = new LinkedList<Icon>();
        this.actionsList = new LinkedList<Action>();
    }

    public int getSize() {
        return labelsList.size();
    }

    public String getLabelAt(int index) {
        return labelsList.get(index);
    }

    public Icon getIconAt(int index) {
        return iconsList.get(index);
    }

    public Action getActionAt(int index) {
        return actionsList.get(index);
    }

    public void addListPaneItem(String label, Icon icon) {
        labelsList.add(label);
        iconsList.add(icon);
        actionsList.add(null);
    }

    public void addListPaneItem(Action action) {
        labelsList.add((String) action.getValue(Action.NAME));
        iconsList.add((Icon) action.getValue(Action.LARGE_ICON_KEY));
        actionsList.add(action);
    }

}
