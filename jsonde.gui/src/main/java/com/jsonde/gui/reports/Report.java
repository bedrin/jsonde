package com.jsonde.gui.reports;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "report")
public class Report {

    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "title")
    private String title;

    @XmlAttribute(name = "small-icon-url")
    private String smallIconUrl;

    @XmlAttribute(name = "large-icon-url")
    private String largeIconUrl;

    @XmlAttribute(name = "chart-type")
    private ChartType chartType;

    @XmlElement(name = "sql")
    private String sql;

    @XmlAttribute(name = "report-generator")
    private String reportGenerator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmallIconUrl() {
        return smallIconUrl;
    }

    public void setSmallIconUrl(String smallIconUrl) {
        this.smallIconUrl = smallIconUrl;
    }

    public String getLargeIconUrl() {
        return largeIconUrl;
    }

    public void setLargeIconUrl(String largeIconUrl) {
        this.largeIconUrl = largeIconUrl;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getReportGenerator() {
        return reportGenerator;
    }

    public void setReportGenerator(String reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

}
