package com.jsonde.gui.reports;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "report-group")
public class ReportGroup {

    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "report")
    private List<Report> reports;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
    
}
