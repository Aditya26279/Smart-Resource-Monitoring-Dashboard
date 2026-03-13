package com.monitor.service.collector;

import com.monitor.entity.MetricType;
import com.monitor.entity.SystemMetric;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class CpuCollector implements MetricCollector {

    private final CentralProcessor processor;
    private long[] prevTicks;

    public CpuCollector() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        this.processor = hal.getProcessor();
        this.prevTicks = processor.getSystemCpuLoadTicks();
    }

    @Override
    public List<SystemMetric> collect() {
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        prevTicks = processor.getSystemCpuLoadTicks();

        SystemMetric metric = SystemMetric.builder()
                .metricType(MetricType.CPU)
                .metricName("CPU_LOAD")
                .metricValue(Double.isNaN(cpuLoad) ? 0.0 : cpuLoad)
                .metricUnit("%")
                .timestamp(LocalDateTime.now())
                .build();

        return Collections.singletonList(metric);
    }
}
