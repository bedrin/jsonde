package com.jsonde.gui.reports;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "reports")
public class Reports {

    @XmlElement(name = "report")
    private List<Report> reports;

    @XmlElement(name = "report-group")
    private List<ReportGroup> reportGroups;

    private Map<String,Report> reportsMap = new HashMap<String,Report>();
    private Map<String,ReportGroup> reportGroupsMap = new HashMap<String,ReportGroup>();

    public static List<Report> getReportsByGroupId(String groupId) throws ReportException {
        return getInstance().reportGroupsMap.get(groupId).getReports();
    }

    public static Report getReport(String reportId) throws ReportException {
        return getInstance().reportsMap.get(reportId);
    }

    private void processReports() {

        for (Report report : reports) {
            if (null != report.getId()) {
                reportsMap.put(report.getId(), report);
            }
        }

        for (ReportGroup reportGroup : reportGroups) {
            if (null != reportGroup.getId()) {
                reportGroupsMap.put(reportGroup.getId(), reportGroup);
            }
            for (Report report : reportGroup.getReports()) {
                if (null != report.getId()) {
                    reportsMap.put(report.getId(), report);
                }
            }
        }

    }

    private static Reports instance;

    private Reports() {
    }

    private static Reports getInstance() throws ReportException {
        if (null == instance) {

            try {

                JAXBContext context = JAXBContext.newInstance(Reports.class);

                Unmarshaller unmarshaller = context.createUnmarshaller();

                URL reportsXmlUrl = Reports.class.getClassLoader().getResource("reports.xml");

                instance = (Reports) unmarshaller.unmarshal(reportsXmlUrl);

                instance.processReports();

            } catch (JAXBException e) {
                throw new ReportException(e);
            }

        }
        return instance;
    }

}
