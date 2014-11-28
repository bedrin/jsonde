package com.jsonde.gui.components.timeChart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Date;

public class PannableTimeChartPanel extends JPanel  implements ChangeListener {

    private XYPlot plot;
    private BoundedRangeModel rangeModel;

    public PannableTimeChartPanel(long startTime, long endTime, TimeSeriesCollection dataset) {

        double interval = ((endTime - startTime) / (30000.));

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Memory Telemtry View", "Time",
                "Memory (Mb)", dataset, PlotOrientation.VERTICAL,
                true, true, false
        );

        plot = (XYPlot) chart.getPlot();

        DateAxis domainAxis = new DateAxis();
        plot.setDomainAxis(domainAxis);
        domainAxis.setLabelAngle(Math.PI / 2);
        domainAxis.setAutoRange(true);
        domainAxis.setMinimumDate(new Date(startTime));
        domainAxis.setMaximumDate(new Date(startTime + 30L * 1000L));

        plot.setDomainPannable(true);

        setLayout(new BorderLayout());

        add(new ChartPanel(chart), BorderLayout.CENTER);
        JScrollBar panScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);

        rangeModel = new DefaultBoundedRangeModel();
        rangeModel.setMinimum(0);
        rangeModel.setMaximum((int)((interval - 1) * 100));
        rangeModel.addChangeListener(this);

        panScrollBar.setModel(rangeModel);

        add(panScrollBar, BorderLayout.SOUTH);

    }

    private int previousValue;

    public void stateChanged(ChangeEvent e) {

        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();

        domainAxis.pan((double) (rangeModel.getValue() - previousValue) / 100.);
        plot.axisChanged(new AxisChangeEvent(domainAxis));

        previousValue = rangeModel.getValue();

    }

}
