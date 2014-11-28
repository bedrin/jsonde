package com.jsonde.gui.dialog.project.sun;

import com.jsonde.gui.dialog.JSondeDialog;
import com.jsonde.gui.dialog.project.FiltersPanel;
import com.jsonde.gui.dialog.project.FiltersPanelImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AttachSunJVMDialog extends JSondeDialog {

    private SunJVMFieldsPanel fieldsPanel;
    private FiltersPanelImpl filtersPanel;
    private ButtonsPanel buttonsPanel;

    public AttachSunJVMDialog() throws HeadlessException {

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

    public SunJVMFieldsPanel getFieldsPanel() {
        return fieldsPanel;
    }

    public FiltersPanel getFiltersPanel() {
        return filtersPanel;
    }

    private void createGUI() {

        Container contentPane = getContentPane();

        contentPane.setLayout(new BorderLayout());

        contentPane.add(fieldsPanel = new SunJVMFieldsPanel(), BorderLayout.NORTH);
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

                    String vmId = getFieldsPanel().getVirtualMachineId();

                    if (null == vmId) {
                        JOptionPane.showMessageDialog(AttachSunJVMDialog.this, "Please select Java process");
                    } else {
                        ok = true;
                        AttachSunJVMDialog.this.setVisible(false);
                    }

                }

            });

            add(okButton);

            JButton cancelButton = new JButton("Cancel");

            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ok = false;
                    AttachSunJVMDialog.this.setVisible(false);
                }

            });

            add(cancelButton);

        }

    }

}