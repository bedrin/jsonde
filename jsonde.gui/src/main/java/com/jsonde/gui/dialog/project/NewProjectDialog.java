package com.jsonde.gui.dialog.project;

import com.jsonde.gui.dialog.JSondeDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewProjectDialog extends JSondeDialog {

    private FieldsPanel fieldsPanel;
    private FiltersPanelImpl filtersPanel;
    private ButtonsPanel buttonsPanel;

    public NewProjectDialog() throws HeadlessException {

        super();

        setSize(400, 325);
        setTitle("New jSonde Project");
        setResizable(false);
        setModal(true);
        setBounds(getFrameBounds());

        createGUI();
        setDefaultValues();
    }

    public boolean showDialog() {
        setVisible(true);
        return buttonsPanel.isOk();
    }

    public FieldsPanel getFieldsPanel() {
        return fieldsPanel;
    }

    public FiltersPanel getFiltersPanel() {
        return filtersPanel;
    }

    private void createGUI() {

        Container contentPane = getContentPane();

        contentPane.setLayout(new BorderLayout());

        contentPane.add(fieldsPanel = new FieldsPanel(), BorderLayout.NORTH);
        contentPane.add(filtersPanel = new FiltersPanelImpl(), BorderLayout.CENTER);
        contentPane.add(buttonsPanel = new ButtonsPanel("Connect"), BorderLayout.SOUTH);

    }

    private void setDefaultValues() {

        fieldsPanel.setProjectFilesLocation(
                System.getProperty("user.home") +
                        System.getProperty("file.separator") +
                        "jSondeProjects" +
                        System.getProperty("file.separator")
        );
        fieldsPanel.setAgentHost("127.0.0.1");
        fieldsPanel.setAgentPort("60001");

    }

    private class ButtonsPanel extends JPanel {

        private boolean ok;

        public boolean isOk() {
            return ok;
        }

        private ButtonsPanel() {
            this("Ok");
        }

        private ButtonsPanel(String okTitle) {

            setLayout(new FlowLayout(FlowLayout.RIGHT));

            JButton okButton = new JButton(okTitle);

            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ok = true;
                    NewProjectDialog.this.setVisible(false);
                }

            });

            add(okButton);

            JButton cancelButton = new JButton("Cancel");

            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ok = false;
                    NewProjectDialog.this.setVisible(false);
                }

            });

            add(cancelButton);

        }

    }

}
