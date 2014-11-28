package com.jsonde.gui.profiler;

import com.jsonde.client.Client;
import com.jsonde.client.dao.DaoException;
import com.jsonde.client.dao.DaoFactory;
import com.jsonde.client.domain.Clazz;
import com.jsonde.gui.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HeapProfilerView extends JPanel {

    private Client client;

    public HeapProfilerView(Client client) {

        this.client = client;

        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("Class name");
        tableModel.addColumn("Instances Count");
        tableModel.addColumn("Collected Instances Count");
        tableModel.addColumn("Total Size");

        try {

            for (Clazz clazz : DaoFactory.getClazzDao().getByCondition("CREATECOUNTER > 0 ORDER BY TOTALCURRENTSIZE DESC")) {

                tableModel.addRow(new Object[]{
                        clazz.getName(),
                        clazz.getCreateCounter() - clazz.getCollectCounter(),
                        clazz.getCollectCounter(),
                        clazz.getTotalCurrentSize()
                });

            }

        } catch (DaoException e) {
            Main.getInstance().processException(e);
        }

        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(table);

        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);


    }

}