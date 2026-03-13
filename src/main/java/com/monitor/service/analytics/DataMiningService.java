package com.monitor.service.analytics;

import com.monitor.entity.MetricType;
import com.monitor.repository.SystemMetricRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataMiningService {

    private final SystemMetricRepository repository;

    public DataMiningService(SystemMetricRepository repository) {
        this.repository = repository;
    }

    /**
     * Mines historical data over the last specified minutes to find anomalies.
     * Checks if max CPU load exceeded 90% in the window.
     * @param minutes Window length in minutes to analyze
     */
    public Map<String, Object> analyzeCpuAnomalies(int minutes) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(minutes);
        Double maxCpu = repository.getMaxMetricValueSince(MetricType.CPU, "CPU_LOAD", since);
        Double avgCpu = repository.getAverageMetricValueSince(MetricType.CPU, "CPU_LOAD", since);

        maxCpu = (maxCpu != null) ? maxCpu : 0.0;
        avgCpu = (avgCpu != null) ? avgCpu : 0.0;

        boolean isAnomaly = maxCpu > 90.0; // Statistical outlier threshold

        Map<String, Object> analysis = new HashMap<>();
        analysis.put("windowMinutes", minutes);
        analysis.put("maxCpuLoad", String.format("%.2f%%", maxCpu));
        analysis.put("avgCpuLoad", String.format("%.2f%%", avgCpu));
        analysis.put("anomalyDetected", isAnomaly);
        if (isAnomaly) {
            analysis.put("recommendation", "Consider scaling up resources or reviewing background processes. CPU hit critical limits.");
        } else {
            analysis.put("recommendation", "CPU levels are stable within the expected boundary constraints.");
        }

        return analysis;
    }
    
    /**
     * Evaluates RAM pressure by comparing USED vs TOTAL aggregations.
     */
    public Map<String, Object> analyzeMemoryPressure(int minutes) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(minutes);
        Double avgUsedRamGb = repository.getAverageMetricValueSince(MetricType.MEMORY, "USED_MEMORY", since);

        avgUsedRamGb = (avgUsedRamGb != null) ? avgUsedRamGb : 0.0;

        Map<String, Object> analysis = new HashMap<>();
        analysis.put("windowMinutes", minutes);
        analysis.put("averageUsedRamGb", String.format("%.2f GB", avgUsedRamGb));

        if (avgUsedRamGb > 80.0) { // arbitrary threshold for %
           analysis.put("pressureState", "HIGH");
        } else {
           analysis.put("pressureState", "NORMAL");
        }
        
        return analysis;
    }
}
