package com.jsonde.gui.dialog.about;

import com.jsonde.gui.dialog.JSondeDialog;
import com.jsonde.util.file.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class AboutDialog extends JSondeDialog {

    public AboutDialog() {

        setSize(620, 460);
        setTitle("jSonde Errors");
        setResizable(false);
        setModal(true);
        setBounds(getFrameBounds());

        Container contentPane = getContentPane();

        contentPane.setLayout(new BorderLayout());

        JLabel label = new JLabel("<html>jSonde version 1.1.0 https://github.com/bedrin/jsonde Copyright (c) 2015");
        label.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        contentPane.add(label, BorderLayout.NORTH);

        StringBuilder stringBuffer = new StringBuilder();

        stringBuffer.
                append("System Properties:").
                append(FileUtils.LINE_SEPARATOR).
                append(FileUtils.LINE_SEPARATOR);

        for (Map.Entry entry : System.getProperties().entrySet()) {
            stringBuffer.
                    append(entry.getKey()).
                    append(" = ").
                    append(entry.getValue()).
                    append(FileUtils.LINE_SEPARATOR);
        }

        stringBuffer.
                append(FileUtils.LINE_SEPARATOR).
                append(FileUtils.LINE_SEPARATOR).
                append("Environment Variables:").
                append(FileUtils.LINE_SEPARATOR).
                append(FileUtils.LINE_SEPARATOR);

        for (Map.Entry<String,String> entry : System.getenv().entrySet()) {
            stringBuffer.
                    append(entry.getKey()).
                    append(" = ").
                    append(entry.getValue()).
                    append(FileUtils.LINE_SEPARATOR);
        }

        // todo fill string buffer

        JTextArea errorsTextArea = new JTextArea(stringBuffer.toString());
        errorsTextArea.setEditable(false);
        errorsTextArea.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(errorsTextArea);

        contentPane.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton(new AbstractAction() {

            {
                putValue(Action.NAME, "Close");
            }

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }

        });

        JPanel closeButtonPanel = new JPanel();
        closeButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        closeButtonPanel.add(closeButton);

        contentPane.add(closeButtonPanel, BorderLayout.SOUTH);

    }

}
