package com.jsonde.gui.components.accordion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedHashSet;
import java.util.Set;

public class JAccordionPanel extends JPanel {

    private Set<JAccordionPane> accordionPanes = new LinkedHashSet<JAccordionPane>();
    private JAccordionPane selectedAccordionPane;

    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel buttonsPanel;

    public JAccordionPanel() {

        setLayout(new BorderLayout());

        headerPanel = new JPanel(new GridBagLayout());
        add(headerPanel, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        add(contentPanel, BorderLayout.CENTER);

        buttonsPanel = new JPanel(new GridBagLayout());
        add(buttonsPanel, BorderLayout.SOUTH);

    }

    private void buildButtonsPanel() {

        buttonsPanel.removeAll();

        int i = 0;

        AccordionPanelButtonAction accordionPanelButtonAction = null;

        for (JAccordionPane accordionPane : accordionPanes) {

            accordionPanelButtonAction = new AccordionPanelButtonAction(accordionPane);
            JButton button = new JButton(accordionPanelButtonAction);

            buttonsPanel.add(
                    button,
                    new GridBagConstraints(
                            0, i,
                            1, 1,
                            1, 1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5),
                            0, 0
                    )
            );

            i++;

        }

        /*if (1 == i) {
            accordionPanelButtonAction.selectPane();
        }*/

        validate();

    }

    public void addPanel(final String name, final Icon icon, JComponent component) {

        final JAccordionPane accordionPane = new JAccordionPane(name, icon, component);

        accordionPanes.add(accordionPane);

        buildButtonsPanel();

        validate();

    }

    public void addPanel(final String name, JComponent component) {

        final JAccordionPane accordionPane = new JAccordionPane(name, component);

        accordionPanes.add(accordionPane);

        buildButtonsPanel();

        validate();

    }

    private class AccordionPanelButtonAction extends AbstractAction {

        private JAccordionPane accordionPane;

        private AccordionPanelButtonAction(JAccordionPane accordionPane) {
            this.accordionPane = accordionPane;
            putValue(NAME, accordionPane.getTitle());
            putValue(SMALL_ICON, accordionPane.getIcon());
        }

        public void actionPerformed(ActionEvent e) {
            selectPane();
        }

        public void selectPane() {

            if (null != selectedAccordionPane) {
                accordionPanes.add(selectedAccordionPane);
            }

            selectedAccordionPane = accordionPane;
            accordionPanes.remove(accordionPane);

            contentPanel.removeAll();
            contentPanel.add(selectedAccordionPane.getComponent(), BorderLayout.CENTER);
            contentPanel.updateUI();

            headerPanel.removeAll();
            headerPanel.add(
                    new JButton(accordionPane.getTitle(), accordionPane.getIcon()),
                    new GridBagConstraints(
                            0, 0,
                            1, 1,
                            1, 1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5),
                            0, 0
                    )
            );

            buildButtonsPanel();

        }

    }

}
