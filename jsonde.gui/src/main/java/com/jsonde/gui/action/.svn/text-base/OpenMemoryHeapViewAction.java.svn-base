package com.jsonde.gui.action;

import com.jsonde.client.Client;
import com.jsonde.gui.ApplicationUserInterface;
import com.jsonde.gui.profiler.HeapProfilerView;
import com.jsonde.gui.reports.custom.DependencyReport;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenMemoryHeapViewAction extends AbstractAction {

    private ApplicationUserInterface applicationUserInterface;


    public OpenMemoryHeapViewAction(ApplicationUserInterface applicationUserInterface) {
        super(
                "Heap",
                new ImageIcon(
                        DependencyReport.class.getClassLoader().getResource("moc_src.png")
                )
        );
        putValue(
                LARGE_ICON_KEY,
                new ImageIcon(
                        DependencyReport.class.getClassLoader().getResource("moc_src_large.png")
                )
        );
        this.applicationUserInterface = applicationUserInterface;
    }

    public void actionPerformed(ActionEvent e) {

        final Client client = applicationUserInterface.getClient();

        if (client.isOnline()) {
            new Thread(new Runnable() {

                public void run() {

                    client.dumpHeap();

                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            JPanel heapProfilerView = new HeapProfilerView(client);
                            applicationUserInterface.addTab(heapProfilerView, "Memory Heap");
                        }

                    });

                }

            }).start();
        }

    }

}