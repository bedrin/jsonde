package com.jsonde.gui.action.composite;

import com.jsonde.gui.ApplicationUserInterface;
import com.jsonde.gui.components.composite.CompositeComponentProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CreateCompositeComponentTabAction extends AbstractAction implements Runnable {

    private ApplicationUserInterface applicationUserInterface;
    private CompositeComponentProvider compositeComponentProvider;

    JPanel panel = new JPanel(new BorderLayout());

    public CreateCompositeComponentTabAction(
            ApplicationUserInterface applicationUserInterface,
            CompositeComponentProvider compositeComponentProvider) {

        super(compositeComponentProvider.getTitle(), compositeComponentProvider.getSmallIcon());

        putValue(LARGE_ICON_KEY, compositeComponentProvider.getLargeIcon());

        this.applicationUserInterface = applicationUserInterface;
        this.compositeComponentProvider = compositeComponentProvider;

        panel.add(getWaitComponent(), BorderLayout.CENTER);

    }

    private JComponent getWaitComponent() {
        return new JLabel("Loading...");
    }

    public void actionPerformed(ActionEvent e) {

        applicationUserInterface.addTab(panel,compositeComponentProvider.getTitle());

        new Thread(this).start();

    }

    public void run() {

        final JComponent component =
                compositeComponentProvider.createCompositeComponent();

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                panel.removeAll();
                panel.add(component, BorderLayout.CENTER);
                panel.validate();

            }

        });

    }

}
