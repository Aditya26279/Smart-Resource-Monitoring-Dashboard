package com.monitor.service;

import com.monitor.entity.SystemMetric;
import com.monitor.service.collector.MetricCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.monitor.repository.SystemMetricRepository;

import java.util.List;

@Service
public class OsMonitorScheduler {

    private static final Logger log = LoggerFactory.getLogger(OsMonitorScheduler.class);

    private final List<MetricCollector> collectors;
    private final WebSocketMetricBroadcaster broadcaster;
    private final SystemMetricRepository repository;

    public OsMonitorScheduler(List<MetricCollector> collectors, WebSocketMetricBroadcaster broadcaster, SystemMetricRepository repository) {
        this.collectors = collectors;
        this.broadcaster = broadcaster;
        this.repository = repository;
    }

    @Scheduled(fixedRate = 5000)
    public void collectMetrics() {
        log.info("--- Polling OS Metrics ---");
        for (MetricCollector collector : collectors) {
            try {
                List<SystemMetric> metrics = collector.collect();
                metrics.forEach(metric ->
                    log.info("[{}] {}: {}{}",
                            metric.getMetricType(),
                            metric.getMetricName(),
                            String.format("%.2f", metric.getMetricValue()),
                            metric.getMetricUnit())
                );
                
                // Persist metrics for historical Data Mining (Phase 4 Integration)
                repository.saveAll(metrics);

                // Broadcast the metric to WebSocket subscribers (Phase 3 Integration)
                broadcaster.broadcastMetrics(metrics);
                
            } catch (Exception e) {
                log.error("Error collecting metric from {}", collector.getClass().getSimpleName(), e);
            }
        }
    }
}
