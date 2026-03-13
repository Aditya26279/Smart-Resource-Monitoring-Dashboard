package com.monitor.service.collector;

import com.monitor.entity.SystemMetric;
import java.util.List;

/**
 * Interface representing a collector for system metrics.
 * Concrete implementations will collect specific types like CPU, Memory, Disk, etc.
 * Uses OOAD principle of polymorphism and Strategy pattern.
 */
public interface MetricCollector {
    
    /**
     * Collects the current system metrics.
     * @return List of collected SystemMetric objects.
     */
    List<SystemMetric> collect();
}
