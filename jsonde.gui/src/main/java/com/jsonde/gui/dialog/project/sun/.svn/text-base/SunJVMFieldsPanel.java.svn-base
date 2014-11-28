package com.jsonde.gui.dialog.project.sun;

import com.jsonde.client.sun.VirtualMachineData;
import com.jsonde.client.sun.VirtualMachineService;
import com.jsonde.client.sun.VirtualMachineServiceException;
import com.jsonde.gui.Main;
import net.sf.sdedit.icons.Icons;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SunJVMFieldsPanel extends JPanel {

    JTextField projectNameField;
    JTextField projectFilesLocationField;
    JTable virtualMachineTable;

    public String getProjectName() {
        return projectNameField.getText();
    }

    public void setProjectName(String projectName) {
        projectNameField.setText(projectName);
    }

    public String getProjectFilesLocation() {
        return projectFilesLocationField.getText();
    }

    public void setProjectFilesLocation(String projectFilesLocation) {
        projectFilesLocationField.setText(projectFilesLocation);
    }

    public String getVirtualMachineId() {

        TableModel tableModel = virtualMachineTable.getModel();
        ListSelectionModel tableSelectionModel = virtualMachineTable.getSelectionModel();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableSelectionModel.isSelectedIndex(i)) {
                return (String) tableModel.getValueAt(i, 0);
            }
        }

        return null;

    }

    private void setMinimumHeight(JComponent... components) {

        int minimumHeight = 0;

        for (JComponent component : components) {
            Dimension componentMinimumSize = component.getMinimumSize();

            if (componentMinimumSize.height > minimumHeight) {
                minimumHeight = componentMinimumSize.height;
            }

        }

        for (JComponent component : components) {
            Dimension componentMinimumSize = component.getMinimumSize();

            if (componentMinimumSize.height < minimumHeight) {
                componentMinimumSize.height = minimumHeight;
            }

        }

    }

    public SunJVMFieldsPanel() {

        setLayout(new GridBagLayout());

        projectNameField = new JTextField();
        JLabel projectNameLabel = new JLabel("Project name:");
        projectNameLabel.setLabelFor(projectNameField);

        projectFilesLocationField = new JTextField();
        JLabel projectFilesLocationLabel = new JLabel("Project files location:");
        projectFilesLocationLabel.setLabelFor(projectFilesLocationField);

        final JLabel chooseFolderLabel = new JLabel(
                new ImageIcon(
                        Icons.class.getResource("open.png")
                )
        );

        chooseFolderLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));

        chooseFolderLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                chooseFolderLabel.setBorder(BorderFactory.createRaisedBevelBorder());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                chooseFolderLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                chooseFolderLabel.setBorder(BorderFactory.createLoweredBevelBorder());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                chooseFolderLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
            }

        });

        virtualMachineTable = getVirtualMachineTable();

        setMinimumHeight(
                projectNameField, projectNameLabel,
                projectFilesLocationField, projectFilesLocationLabel,
                chooseFolderLabel);

        add(
                projectNameLabel,
                new GridBagConstraints(
                        0, 0,
                        1, 1,
                        0, 0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        add(
                projectNameField,
                new GridBagConstraints(
                        1, 0,
                        2, 1,
                        1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        add(
                projectFilesLocationLabel,
                new GridBagConstraints(
                        0, 1,
                        1, 1,
                        0, 0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        add(
                projectFilesLocationField,
                new GridBagConstraints(
                        1, 1,
                        1, 1,
                        1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        add(
                chooseFolderLabel,
                new GridBagConstraints(
                        2, 1,
                        1, 1,
                        0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        add(
                virtualMachineTable,
                new GridBagConstraints(
                        0, 2,
                        3, 1,
                        1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

    }

    private JTable getVirtualMachineTable() {
        DefaultTableModel vmTableModel = new DefaultTableModel();

        vmTableModel.addColumn("PID");
        vmTableModel.addColumn("Application");

        try {

            VirtualMachineService vmService = VirtualMachineService.getInstance();

            for (VirtualMachineData vmData : vmService.getVirtualMachines()) {

                vmTableModel.addRow(new Object[]{
                        vmData.getId(),
                        vmData.getDescription()
                });

            }

        } catch (VirtualMachineServiceException e) {
            Main.getInstance().processException(e);
        }

        JTable vmTable = new JTable(vmTableModel);
        return vmTable;
    }

}