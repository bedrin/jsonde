package com.jsonde.profiler.telemetry;

import junit.framework.TestCase;

public class TelemetryDataProviderTest extends TestCase {

    public void testRun() throws Exception {

        TelemetryDataProvider telemetryDataProvider = new TelemetryDataProvider(null);

        telemetryDataProvider.run();

    }

}
