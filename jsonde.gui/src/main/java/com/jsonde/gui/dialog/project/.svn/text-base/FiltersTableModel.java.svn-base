package com.jsonde.gui.dialog.project;

import com.jsonde.api.configuration.ClassFilterDto;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FiltersTableModel extends AbstractTableModel {

    private List<ClassFilterDto> classFilters;

    public FiltersTableModel() {
        this(Collections.<ClassFilterDto>emptyList());
    }

    public FiltersTableModel(List<ClassFilterDto> classFilters) {
        this.classFilters = classFilters;
    }

    public List<ClassFilterDto> getClassFilters() {
        return Collections.unmodifiableList(classFilters);
    }

    public void setClassFilters(List<ClassFilterDto> classFilters) {
        this.classFilters = new ArrayList<ClassFilterDto>(classFilters);
        fireTableDataChanged();
    }

    public void addClassFilter(ClassFilterDto classFilter) {
        classFilters.add(classFilter);
        fireTableRowsInserted(classFilters.size(), classFilters.size());
    }

    public void swapRows(int row1, int row2) {
        ClassFilterDto classFilter1 = classFilters.get(row1);
        ClassFilterDto classFilter2 = classFilters.get(row2);
        classFilters.set(row1, classFilter2);
        classFilters.set(row2, classFilter1);
        fireTableRowsUpdated(Math.min(row1, row2), Math.max(row1, row2));
    }

    public void deleteClassFilter(int id) {
        classFilters.remove(id);
        fireTableRowsDeleted(id, id);
    }

    public int getRowCount() {
        return classFilters.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ClassFilterDto classFilter = classFilters.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return classFilter.isInclusive();
            case 1:
                return classFilter.getPackageName();
        }

        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        ClassFilterDto classFilter = classFilters.get(rowIndex);

        switch (columnIndex) {
            case 0:
                classFilter.setInclusive((Boolean) aValue);
                break;
            case 1:
                classFilter.setPackageName((String) aValue);
                break;
        }

    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Inclusive";
            case 1:
                return "Class";
            default:
                return super.getColumnName(columnIndex);
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            default:
                return super.getColumnClass(columnIndex);
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

}
