package com.jsonde.gui.profiler;

import com.jsonde.client.dao.DaoException;
import com.jsonde.client.dao.DaoFactory;
import com.jsonde.client.domain.Method;
import com.jsonde.client.domain.MethodCallSummary;
import com.jsonde.gui.Main;
import org.freehep.swing.treetable.AbstractTreeTableModel;
import org.freehep.swing.treetable.JTreeTable;
import org.freehep.swing.treetable.TreeTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MethodCallProfilerView extends JPanel {

    private static class MethodCallProfilerNode {

        final Long id;

        final String name;
        final long invocationCount;
        final String totalTime;

        List<MethodCallProfilerNode> childNodes;

        @Override
        public String toString() {
            return name;
        }

        private MethodCallProfilerNode(Long id, String name, long invocationCount, String totalTime) {
            this.id = id;
            this.name = name;
            this.invocationCount = invocationCount;
            this.totalTime = totalTime;
        }

        public List<MethodCallProfilerNode> getChildNodes() {

            if (null == childNodes) {

                try {
                    /*List<MethodCallSummary> methodCallSummaries;
                    if (null == id) {
                         methodCallSummaries =
                                DaoFactory.getMethodCallSummaryDao().getByCondition("CALLERID IS NULL ORDER BY TOTALTIME DESC");

                        // select methodid as methodid, count(id) as invocationcount, sum(executiontime) as executiontime from methodcall where callerid is null group by methodid order by executiontime desc

                    } else {
                        methodCallSummaries =
                                DaoFactory.getMethodCallSummaryDao().getByCondition("CALLERID = ? ORDER BY TOTALTIME DESC", id);
                    }*/

                    List<MethodCallSummary> methodCallSummaries =
                            DaoFactory.getMethodCallSummaryDao().getCpuProfilerData(id);

                    childNodes = new ArrayList<MethodCallProfilerNode>(methodCallSummaries.size());

//                    for (MethodCallSummary methodCallSummary : methodCallSummaries) {
                    for (MethodCallSummary methodCallSummary : methodCallSummaries) {

                        Method method = DaoFactory.getMethodDao().get(methodCallSummary.getMethodId());

                        String name =
                                DaoFactory.getClazzDao().get(method.getClassId()).getName() +
                                        "." +
                                        method.getName();

                        long time = methodCallSummary.getExecutionTime();

                        StringBuilder timeStringBuilder = new StringBuilder();

                        time /= 1000L;

                        timeStringBuilder.append(time % 1000L + " ms");

                        time /= 1000L;

                        timeStringBuilder.insert(0, time % 1000L + ".");

                        time /= 1000L;

                        if (time > 0) {

                            timeStringBuilder.insert(0, time % 60L + " s ");

                            time /= 60L;

                            if (time > 0) {
                                timeStringBuilder.insert(0, time % 60L + " m ");

                                time /= 60L;

                                if (time > 0) {
                                    timeStringBuilder.insert(0, time + " h ");
                                }

                            }

                        }

                        MethodCallProfilerNode childNode = new MethodCallProfilerNode(
                                methodCallSummary.getId(),
                                name,
                                methodCallSummary.getInvocationCount(),
                                timeStringBuilder.toString()
                        );

                        childNodes.add(childNode);

                    }

                } catch (DaoException e) {
                    Main.getInstance().processException(e);
                }

            }

            return childNodes;
        }

    }

    public MethodCallProfilerView() {

        MethodCallProfilerNode rootNode = new MethodCallProfilerNode(
                null, "Method calls", 0, null
        );

        MethodCallProfilerViewTreeTableModel treeTableModel = new MethodCallProfilerViewTreeTableModel(rootNode);

        JTreeTable treeTable = new JTreeTable(treeTableModel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(treeTable);

        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);
    }

    private class MethodCallProfilerViewTreeTableModel extends AbstractTreeTableModel {

        private MethodCallProfilerViewTreeTableModel(MethodCallProfilerNode root) {
            super(root);
        }

        public Object getChild(Object parent, int index) {
            MethodCallProfilerNode parentNode = (MethodCallProfilerNode) parent;
            return parentNode.getChildNodes().get(index);
        }

        public int getChildCount(Object parent) {
            MethodCallProfilerNode parentNode = (MethodCallProfilerNode) parent;
            return parentNode.getChildNodes().size();
        }

        public int getColumnCount() {
            return 3;
        }

        public String getColumnName(int i) {
            switch (i) {
                case 0:
                    return "Name";
                case 1:
                    return "Invocation Count";
                case 2:
                    return "Invocation Time";
            }
            return null;
        }

        @Override
        public Class getColumnClass(int column) {
            switch (column) {
                case 0:
                    return TreeTableModel.class;
                case 1:
                    return Long.class;
                case 2:
                    return String.class;
            }
            return null;
        }

        @Override
        public Object getValueAt(Object o, int i) {

            MethodCallProfilerNode node = (MethodCallProfilerNode) o;

            switch (i) {
                case 0:
                    return node.name;
                case 1:
                    return node.invocationCount;
                case 2:
                    return node.totalTime;
            }
            return null;
        }


    }

}
