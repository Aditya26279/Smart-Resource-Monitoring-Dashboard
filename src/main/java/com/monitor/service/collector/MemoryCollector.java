package com.monitor.service.collector;

import com.monitor.entity.MetricType;
import com.monitor.entity.SystemMetric;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class MemoryCollector implements MetricCollector {

    private final GlobalMemory memory;

    public MemoryCollector() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();
        this.memory = hal.getMemory();
    }

    @Override
    public List<SystemMetric> collect() {
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();
        long usedMemory = totalMemory - availableMemory;

        double usedMemoryPercentage = (double) usedMemory / totalMemory * 100;
        double totalMemoryGb = (double) totalMemory / (1024 * 1024 * 1024);
        double availableMemoryGb = (double) availableMemory / (1024 * 1024 * 1024);

        SystemMetric usedMetric = SystemMetric.builder()
                .metricType(MetricType.MEMORY)
                .metricName("USED_MEMORY")
                .metricValue(usedMemoryPercentage)
                .metricUnit("%")
                .timestamp(LocalDateTime.now())
                .build();

        SystemMetric totalMetricGb = SystemMetric.builder()
                .metricType(MetricType.MEMORY)
                .metricName("TOTAL_MEMORY_GB")
                .metricValue(totalMemoryGb)
                .metricUnit("GB")
                .timestamp(LocalDateTime.now())
                .build();

        SystemMetric availableMetricGb = SystemMetric.builder()
                .metricType(MetricType.MEMORY)
                .metricName("AVAILABLE_MEMORY_GB")
                .metricValue(availableMemoryGb)
                .metricUnit("GB")
                .timestamp(LocalDateTime.now())
                .build();

        return Arrays.asList(usedMetric, totalMetricGb, availableMetricGb);
    }
}
