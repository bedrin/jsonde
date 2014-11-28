package com.jsonde.gui.dialog.project;

import com.jsonde.api.configuration.ClassFilterDto;

import java.util.List;

public interface FiltersPanel {

    void setClassFilters(List<ClassFilterDto> classFilters);

    List<ClassFilterDto> getClassFilters();

}
