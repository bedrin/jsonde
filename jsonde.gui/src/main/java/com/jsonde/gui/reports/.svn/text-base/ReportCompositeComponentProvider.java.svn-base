package com.jsonde.gui.reports;

import com.jsonde.client.dao.DaoException;
import com.jsonde.client.dao.DaoFactory;
import com.jsonde.gui.Main;
import com.jsonde.gui.components.composite.CompositeComponentProvider;
import com.jsonde.gui.components.timeChart.PannableTimeChartPanel;
import com.jsonde.gui.reports.custom.ReportGenerator;
import com.jsonde.util.db.DbUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.List;

public class ReportCompositeComponentProvider implements CompositeComponentProvider {

    private Report report;

    public ReportCompositeComponentProvider(Report report) {
        this.report = report;
    }

    public Icon getLargeIcon() {
        return new ImageIcon(
                getClass().getClassLoader().getResource(report.getLargeIconUrl())
        );
    }

    public Icon getSmallIcon() {
        return new ImageIcon(
                getClass().getClassLoader().getResource(report.getSmallIconUrl())
        );
    }

    public String getTitle() {
        return report.getTitle();
    }

    public JComponent createCompositeComponent() {

        switch (report.getChartType()) {
            case PIE:
                return createPieChart();
            case TIME:
                return createTimeChart();
            case CUSTOM:
                return createCustomChart();
        }

        return new JLabel("Unknown chart type");

    }

    private JComponent createCustomChart() {

        String reportGeneratorClassName = report.getReportGenerator();

        try {
            Class reportGeneratorClass = Class.forName(reportGeneratorClassName);
            ReportGenerator reportGenerator = (ReportGenerator) reportGeneratorClass.newInstance();
            return reportGenerator.generateReport();
        } catch (ClassNotFoundException e) {
            Main.getInstance().processException(e);
            return new JLabel("Failed to create report");
        } catch (InstantiationException e) {
            Main.getInstance().processException(e);
            return new JLabel("Failed to create report");
        } catch (IllegalAccessException e) {
            Main.getInstance().processException(e);
            return new JLabel("Failed to create report");
        }

    }

    private JComponent createTimeChart() {

        String sql = report.getSql();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = DaoFactory.getConnection();

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.execute();

            resultSet = preparedStatement.getResultSet();

            //

            TimeSeriesCollection dataset = new TimeSeriesCollection();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            List<TimeSeries> timeSeriesList = new ArrayList<TimeSeries>(columnCount);

            for (int i = 2; i <= columnCount; i++) {

                String columnName = resultSetMetaData.getColumnName(i);

                TimeSeries timeSeries = new TimeSeries(columnName);

                timeSeriesList.add(timeSeries);
                dataset.addSeries(timeSeries);

            }

            long startTime = 0;
            long endTime = 0;

            while (resultSet.next()) {

                long time = resultSet.getLong(1);

                if (0 == startTime) {
                    startTime = time;
                }

                endTime = time;

                RegularTimePeriod timePeriod = new Millisecond(new java.util.Date(time));

                for (int i = 0; i < timeSeriesList.size(); i++) {

                    TimeSeries timeSeries = timeSeriesList.get(i);

                    double value = resultSet.getDouble(i + 2);
                    timeSeries.add(timePeriod, value);

                }

            }

            return new PannableTimeChartPanel(startTime, endTime, dataset);

        } catch (DaoException e) {
            Main.getInstance().processException(e);
            return new JLabel("Failed to create report");
        } catch (SQLException e) {
            Main.getInstance().processException(e);
            return new JLabel("Failed to create report");
        } finally {
            DbUtils.close(resultSet);
            DbUtils.close(preparedStatement);
            DbUtils.close(connection);
        }

    }

    private JComponent createPieChart() {

        String sql = report.getSql();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = DaoFactory.getConnection();

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.execute();

            resultSet = preparedStatement.getResultSet();

            DefaultPieDataset dataSet = new DefaultPieDataset();

            while (resultSet.next()) {
                dataSet.setValue(resultSet.getString(1), resultSet.getLong(2));
            }

            JFreeChart pieChart = ChartFactory.createPieChart3D(
                    getTitle(),
                    dataSet,
                    true,
                    true,
                    false);

            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setViewportView(new ChartPanel(pieChart));

            return scrollPane;

        } catch (DaoException e) {
            Main.getInstance().processException(e);
            return new JLabel("Failed to create report");
        } catch (SQLException e) {
            Main.getInstance().processException(e);
            return new JLabel("Failed to create report");
        } finally {
            DbUtils.close(resultSet);
            DbUtils.close(preparedStatement);
            DbUtils.close(connection);
        }

    }

}
