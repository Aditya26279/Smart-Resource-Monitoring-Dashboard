package com.monitor.service.collector;

import com.monitor.entity.MetricType;
import com.monitor.entity.SystemMetric;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiskCollector implements MetricCollector {

    private final HardwareAbstractionLayer hal;

    public DiskCollector() {
        SystemInfo systemInfo = new SystemInfo();
        this.hal = systemInfo.getHardware();
    }

    @Override
    public List<SystemMetric> collect() {
        List<SystemMetric> metrics = new ArrayList<>();
        List<HWDiskStore> diskStores = hal.getDiskStores();

        long totalReads = 0;
        long totalWrites = 0;

        for (HWDiskStore disk : diskStores) {
            disk.updateAttributes();
            totalReads += disk.getReadBytes();
            totalWrites += disk.getWriteBytes();
        }

        metrics.add(SystemMetric.builder()
                .metricType(MetricType.DISK)
                .metricName("DISK_READS")
                .metricValue((double) totalReads)
                .metricUnit("Bytes")
                .timestamp(LocalDateTime.now())
                .build());

        metrics.add(SystemMetric.builder()
                .metricType(MetricType.DISK)
                .metricName("DISK_WRITES")
                .metricValue((double) totalWrites)
                .metricUnit("Bytes")
                .timestamp(LocalDateTime.now())
                .build());

        return metrics;
    }
}
