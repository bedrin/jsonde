package com.jsonde.gui.action;

import com.jsonde.api.configuration.AgentConfigurationMessage;
import com.jsonde.client.Client;
import com.jsonde.client.sun.VirtualMachineService;
import com.jsonde.client.sun.VirtualMachineServiceException;
import com.jsonde.gui.Main;
import com.jsonde.gui.configuration.SessionConfiguration;
import com.jsonde.gui.configuration.SessionConfigurationException;
import com.jsonde.gui.dialog.project.sun.AttachSunJVMDialog;
import com.jsonde.gui.dialog.project.sun.SunJVMFieldsPanel;
import com.jsonde.gui.sdedit.SdEditUIAdapter;
import net.sf.sdedit.icons.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class NewLocalSunJVMProjectAction extends AbstractAction {

    private SdEditUIAdapter sdEditUIAdapter;

    public NewLocalSunJVMProjectAction(SdEditUIAdapter sdEditUIAdapter) {
        this.sdEditUIAdapter = sdEditUIAdapter;
    }

    {
        putValue(Action.SMALL_ICON,
                new ImageIcon(
                        Icons.class.getResource("new.png")
                ));
        putValue(Action.NAME, "Attach to Sun JVM");
        putValue(Action.SHORT_DESCRIPTION, "Create a new jSonde Project by attaching to Sun JVM process");
    }

    public void actionPerformed(ActionEvent e) {

        VirtualMachineService vmService;

        try {
            vmService = VirtualMachineService.getInstance();
        } catch (VirtualMachineServiceException e1) {
            Main.getInstance().processException(e1);
            showSunVMErrorMessage();
            return;
        }

        try {
            if (!vmService.isSun16JVM()) {
                showSunVMErrorMessage();
            }
        } catch (VirtualMachineServiceException e1) {
            Main.getInstance().processException(e1);
            showSunVMErrorMessage();
            return;
        }

        AttachSunJVMDialog newProjectDialog = new AttachSunJVMDialog();

        if (newProjectDialog.showDialog()) {

            SunJVMFieldsPanel fieldsPanel = newProjectDialog.getFieldsPanel();

            String vmId = fieldsPanel.getVirtualMachineId();

            try {

                File agentJarFile = new File("./lib/jsonde.agent-1.1.0.jar");

                vmService.attachAgent(vmId, agentJarFile.getAbsolutePath(), "60001");

            } catch (VirtualMachineServiceException ex) {
                Main.getInstance().processException(ex);
            }

            String databaseFileName =
                    fieldsPanel.getProjectFilesLocation() +
                            System.getProperty("file.separator") +
                            fieldsPanel.getProjectName();

            String host = "127.0.0.1";
            int port = 60001;

            Client client = new Client(databaseFileName, host, port);

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                Main.getInstance().processException(ex);
            }

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

    private void showSunVMErrorMessage() {
        JOptionPane.showMessageDialog(sdEditUIAdapter.getFrame(), "Failed to get list of virtual machines. " +
                "This feature is available only on Sun JDK 1.6+. " +
                "If you're using such version of JDK, please check if your environments variable JAVA_HOME or JDK_HOME " +
                "contains correct path to your Sun JDK 1.6+");
    }

}