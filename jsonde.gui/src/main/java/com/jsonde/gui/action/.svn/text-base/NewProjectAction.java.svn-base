package com.jsonde.gui.action;

import com.jsonde.api.configuration.AgentConfigurationMessage;
import com.jsonde.client.Client;
import com.jsonde.gui.Main;
import com.jsonde.gui.configuration.SessionConfiguration;
import com.jsonde.gui.configuration.SessionConfigurationException;
import com.jsonde.gui.dialog.project.FieldsPanel;
import com.jsonde.gui.dialog.project.NewProjectDialog;
import com.jsonde.gui.sdedit.SdEditUIAdapter;
import net.sf.sdedit.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewProjectAction extends AbstractAction {

    private SdEditUIAdapter sdEditUIAdapter;

    public NewProjectAction(SdEditUIAdapter sdEditUIAdapter) {
        this.sdEditUIAdapter = sdEditUIAdapter;
    }

    {
        putValue(Action.SMALL_ICON,
                new ImageIcon(
                        Icons.class.getResource("new.png")
                ));
        putValue(Action.NAME, "New Project");
        putValue(Action.SHORT_DESCRIPTION, "Create a new jSonde Project");
    }

    public void actionPerformed(ActionEvent e) {

        NewProjectDialog newProjectDialog = new NewProjectDialog();

        if (newProjectDialog.showDialog()) {

            FieldsPanel fieldsPanel = newProjectDialog.getFieldsPanel();

            String databaseFileName =
                    fieldsPanel.getProjectFilesLocation() +
                            System.getProperty("file.separator") +
                            fieldsPanel.getProjectName();

            String host = fieldsPanel.getAgentHost();
            int port = Integer.parseInt(fieldsPanel.getAgentPort());

            Client client = new Client(databaseFileName, host, port);

            client.start();

            SessionConfiguration sessionConfiguration = new SessionConfiguration();
            sessionConfiguration.setDatabaseFileName(databaseFileName);

            try {
                sessionConfiguration.save(databaseFileName + ".jss");
            } catch (SessionConfigurationException e1) {
                Main.getInstance().processException(e1);
            }

            AgentConfigurationMessage agentConfigurationMessage =
                    new AgentConfigurationMessage();

            agentConfigurationMessage.setClassFilters(
                    newProjectDialog.getFiltersPanel().getClassFilters());

            client.sendMessage(agentConfigurationMessage);

            sdEditUIAdapter.setClient(client);

        }

    }

}
