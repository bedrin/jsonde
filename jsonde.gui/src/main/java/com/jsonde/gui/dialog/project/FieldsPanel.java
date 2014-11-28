package com.jsonde.gui.dialog.project;

import com.jsonde.gui.components.JActionIcon;
import net.sf.sdedit.icons.Icons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FieldsPanel extends JPanel {

    JTextField projectNameField;
    JTextField projectFilesLocationField;
    JTextField agentHostField;
    JTextField agentPortField;

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

    public String getAgentHost() {
        return agentHostField.getText();
    }

    public void setAgentHost(String agentHost) {
        agentHostField.setText(agentHost);
    }

    public String getAgentPort() {
        return agentPortField.getText();
    }

    public void setAgentPort(String agentPort) {
        agentPortField.setText(agentPort);
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

    public FieldsPanel() {

        setLayout(new GridBagLayout());

        projectNameField = new JTextField();
        JLabel projectNameLabel = new JLabel("Project name:");
        projectNameLabel.setLabelFor(projectNameField);

        projectFilesLocationField = new JTextField();
        JLabel projectFilesLocationLabel = new JLabel("Project files location:");
        projectFilesLocationLabel.setLabelFor(projectFilesLocationField);

        agentHostField = new JTextField();
        JLabel agentHostLabel = new JLabel("Agent host:");
        agentHostLabel.setLabelFor(agentHostField);

        agentPortField = new JTextField();
        JLabel agentPortLabel = new JLabel("Agent port:");
        agentPortLabel.setLabelFor(agentPortField);

        JLabel chooseFolderLabel = new JActionIcon(new AbstractAction() {

            {
                putValue(
                        Action.SMALL_ICON,
                        new ImageIcon(
                                Icons.class.getResource("open.png")
                        )
                );
            }

            public void actionPerformed(ActionEvent e) {
                // todo implement
            }

        });

        setMinimumHeight(
                projectNameField, projectNameLabel,
                projectFilesLocationField, projectFilesLocationLabel,
                agentHostField, agentHostLabel,
                agentPortField, agentPortLabel,
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
                agentHostLabel,
                new GridBagConstraints(
                        0, 2,
                        1, 1,
                        0, 0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        add(
                agentHostField,
                new GridBagConstraints(
                        1, 2,
                        2, 1,
                        1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        add(
                agentPortLabel,
                new GridBagConstraints(
                        0, 3,
                        1, 1,
                        0, 0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        add(
                agentPortField,
                new GridBagConstraints(
                        1, 3,
                        2, 1,
                        1, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

    }

}
