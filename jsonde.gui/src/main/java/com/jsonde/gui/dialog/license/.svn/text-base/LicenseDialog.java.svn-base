package com.jsonde.gui.dialog.license;

import com.jsonde.gui.dialog.JSondeDialog;
import com.jsonde.gui.license.LicenseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LicenseDialog extends JSondeDialog {

    private boolean evaluate;
    private boolean enterLicenseCode;

    private boolean licenseValid;
    private String license;

    public boolean isEvaluate() {
        return evaluate;
    }

    public boolean isEnterLicenseCode() {
        return enterLicenseCode;
    }

    public boolean isLicenseValid() {
        return licenseValid;
    }

    public String getLicense() {
        return license;
    }

    public LicenseDialog() {

        //setSize(400, 150);
        setTitle("jSonde License");
        setResizable(false);
        setModal(true);

        JLabel licenseLabel = new JLabel("Please enter your jSonde license code or evaluate it for free");

        ButtonGroup buttonGroup = new ButtonGroup();

        JRadioButton evaluateRadioButton = new JRadioButton();
        JRadioButton enterLicenseRadioButton = new JRadioButton();

        buttonGroup.add(evaluateRadioButton);
        buttonGroup.add(enterLicenseRadioButton);

        final JTextField licenseTextField = new JTextField();
        licenseTextField.setEnabled(false);

        JButton exitButton = new JButton();
        JButton okButton = new JButton();

        final LicenseManager licenseManager = new LicenseManager();

        okButton.setAction(new AbstractAction() {

            {
                putValue(NAME, "Ok");
                setName("Ok");
            }

            public void actionPerformed(ActionEvent e) {
                String license = licenseTextField.getText();
                if (enterLicenseCode) {

                    if (licenseManager.checkLicense(license)) {
                        LicenseDialog.this.license = license;
                        licenseValid = true;
                        setVisible(false);
                    } else {
                        LicenseDialog.this.license = null;
                        licenseValid = false;
                        JOptionPane.showMessageDialog(LicenseDialog.this, "Incorrect license");
                    }

                } else if (evaluate) {
                    setVisible(false);
                }

            }
        });

        exitButton.setAction(new AbstractAction() {

            {
                putValue(NAME, "Exit");
                setName("Exit");
            }

            public void actionPerformed(ActionEvent e) {
                LicenseDialog.this.license = null;
                licenseValid = false;
                setVisible(false);
                System.exit(0);
            }

        });

        evaluateRadioButton.setAction(new AbstractAction() {

            {
                putValue(NAME, "Evaluate");
                setName("Evaluate");
            }

            public void actionPerformed(ActionEvent e) {
                evaluate = true;
                enterLicenseCode = false;
                licenseTextField.setEnabled(false);
            }

        });

        enterLicenseRadioButton.setAction(new AbstractAction() {

            {
                putValue(NAME, "Enter license code");
                setName("Enter license code");
            }

            public void actionPerformed(ActionEvent e) {
                evaluate = false;
                enterLicenseCode = true;
                licenseTextField.setEnabled(true);
            }

        });

        Container container = getContentPane();

        container.setLayout(new GridBagLayout());

        int y = 0;

        container.add(
                licenseLabel,
                new GridBagConstraints(
                        0, y,
                        2, 1,
                        1, 0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        y++;

        container.add(
                evaluateRadioButton,
                new GridBagConstraints(
                        0, y,
                        2, 1,
                        1, 0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        y++;

        container.add(
                enterLicenseRadioButton,
                new GridBagConstraints(
                        0, y,
                        2, 1,
                        1, 0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        y++;

        container.add(
                licenseTextField,
                new GridBagConstraints(
                        0, y,
                        2, 1,
                        1, 0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 0, 5), 0, 0
                )
        );

        y++;

        container.add(
                exitButton,
                new GridBagConstraints(
                        0, y,
                        1, 1,
                        1, 1,
                        GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0
                )
        );

        container.add(
                okButton,
                new GridBagConstraints(
                        1, y,
                        1, 1,
                        0, 1,
                        GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0
                )
        );

        pack();
        setBounds(getFrameBounds());

    }

    public static void main(String[] args) {
        LicenseDialog ld = new LicenseDialog();
        ld.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ld.setVisible(true);
        System.exit(0);
    }

}
