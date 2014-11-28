package com.jsonde.gui.components.listpane;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class JListPane extends JList {

    private List<ActionListener> actionListeners = new LinkedList<ActionListener>();

    private ListPaneModel listPaneModel;

    private final ListCellRenderer listCellRenderer = new JListPaneListCellRenderer();
    private final ListModel listModel = new JListPaneListModel();
    private final MouseListener mouseListener = new JListPaneMouseListener();

    public JListPane() {
        this(new DefaultListPaneModel());
    }

    public JListPane(ListPaneModel listPaneModel) {
        this.listPaneModel = listPaneModel;
        setModel(listModel);
        addMouseListener(mouseListener);
    }

    public synchronized void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    public synchronized void removeActionListener(ActionListener actionListener) {
        actionListeners.remove(actionListener);
    }

    public ListPaneModel getListPaneModel() {
        return listPaneModel;
    }

    public void setListPaneModel(ListPaneModel listPaneModel) {
        this.listPaneModel = listPaneModel;
    }

    private void fireActionEvent(int index, ActionEvent e) {

        Action action = getListPaneModel().getActionAt(index);

        if (null != action) {
            action.actionPerformed(e);
        }

        for (ActionListener actionListener : actionListeners) {
            actionListener.actionPerformed(e);
        }
    }

    @Override
    public ListCellRenderer getCellRenderer() {
        return listCellRenderer;
    }

    private class JListPaneListCellRenderer implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            JLabel label = new JLabel();

            label.setIcon(listPaneModel.getIconAt(index));
            label.setText(listPaneModel.getLabelAt(index));

            label.setBackground(list.getBackground());
            label.setForeground(list.getForeground());

            label.setEnabled(list.isEnabled());
            label.setFont(list.getFont());

            label.setComponentOrientation(list.getComponentOrientation());

            Border border = null;
            if (cellHasFocus) {
                if (isSelected) {
                    border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
                }
                if (border == null) {
                    border = UIManager.getBorder("List.focusCellHighlightBorder");
                }
            } else {
                border = getNoFocusBorder();
            }
            label.setBorder(border);

            return label;

        }

        private final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
        private final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

        private Border getNoFocusBorder() {
            if (System.getSecurityManager() != null) {
                return SAFE_NO_FOCUS_BORDER;
            } else {
                return noFocusBorder;
            }
        }

    }

    private class JListPaneListModel extends AbstractListModel {

        private static final long serialVersionUID = 1979045198103713994L;

        public int getSize() {
            return listPaneModel.getSize();
        }

        public Object getElementAt(int index) {
            return listPaneModel.getLabelAt(index);
        }

    }

    private class JListPaneMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if ((MouseEvent.BUTTON1 == e.getButton()) && (2 == e.getClickCount())) {
                for (int i = 0; i < listPaneModel.getSize(); i++) {
                    if (isSelectedIndex(i)) {
                        ActionEvent actionEvent =
                                new ActionEvent(JListPane.this, ActionEvent.ACTION_PERFORMED, "listPaneItemDoubleClicked");
                        fireActionEvent(i, actionEvent);
                    }
                }
            }
        }

    }

}
