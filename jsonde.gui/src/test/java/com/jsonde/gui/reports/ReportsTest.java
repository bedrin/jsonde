package com.jsonde.gui.reports;

import junit.framework.TestCase;

import java.net.URI;
import java.util.List;

public class ReportsTest extends TestCase {

    public void testGetReports() throws Exception {

        List<Report> reports = Reports.getReportsByGroupId("reports");

        assertNotNull(reports);

    }

}
