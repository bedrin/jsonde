package com.jsonde.gui.dialog.project;

import com.jsonde.api.configuration.ClassFilterDto;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class SimpleFiltersPanel extends JPanel implements FiltersPanel {

    private JTextField packageFilter;

    public void setClassFilters(List<ClassFilterDto> classFilters) {

    }

    public List<ClassFilterDto> getClassFilters() {

        ClassFilterDto excludeAll = new ClassFilterDto(false, "*");
        ClassFilterDto includePackage = new ClassFilterDto(true, packageFilter.getText());

        return Arrays.asList(
                excludeAll, includePackage
        );
    }

    public SimpleFiltersPanel() {

        setLayout(new GridBagLayout());

        JLabel packageFilterLabel = new JLabel("<html>Enter package containing your application (e.g. com.mycompany.*)");
        packageFilter = new JTextField();

        add(
                packageFilterLabel,
                new GridBagConstraints(
                        0, 0,
                        1, 1,
                        1, 0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5), 0, 0
                )
        );

        add(
                packageFilter,
                new GridBagConstraints(
                        0, 1,
                        1, 1,
                        0, 1,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5), 0, 0
                )
        );

    }

}