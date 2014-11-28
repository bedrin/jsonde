package com.jsonde.profiler.telemetry;

import com.jsonde.api.telemetry.TelemetryDataDto;
import com.jsonde.api.telemetry.TelemetryDataMessage;
import com.jsonde.profiler.Profiler;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;

public class TelemetryDataProvider implements Runnable {

    private Profiler profiler;

    public TelemetryDataProvider(Profiler profiler) {
        this.profiler = profiler;
    }

    public void run() {

        TelemetryDataDto telemetryDataDto = new TelemetryDataDto();

        telemetryDataDto.time = System.currentTimeMillis();

        Runtime runtime = Runtime.getRuntime();

        telemetryDataDto.freeMemory = runtime.freeMemory();
        telemetryDataDto.maxMemory = runtime.maxMemory();
        telemetryDataDto.totalMemory = runtime.totalMemory();

        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();

        telemetryDataDto.loadedClassCount = classLoadingMXBean.getLoadedClassCount();
        telemetryDataDto.classCount = classLoadingMXBean.getTotalLoadedClassCount();
        telemetryDataDto.unloadedClassCount = classLoadingMXBean.getUnloadedClassCount();

        CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();

        telemetryDataDto.totalCompilationTime = compilationMXBean.getTotalCompilationTime();

        if (null != profiler)
            profiler.sendMessage(new TelemetryDataMessage(telemetryDataDto));

    }

}
