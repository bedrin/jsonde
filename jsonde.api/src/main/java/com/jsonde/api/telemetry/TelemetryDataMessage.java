package com.jsonde.api.telemetry;

import com.jsonde.api.Message;

public class TelemetryDataMessage extends Message {

    private TelemetryDataDto telemetryDataDto;

    public TelemetryDataMessage(TelemetryDataDto telemetryDataDto) {
        this.telemetryDataDto = telemetryDataDto;
    }

    public TelemetryDataDto getTelemetryData() {
        return telemetryDataDto;
    }

}
