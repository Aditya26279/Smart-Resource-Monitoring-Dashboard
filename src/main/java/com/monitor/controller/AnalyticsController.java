package com.monitor.controller;

import com.monitor.service.analytics.DataMiningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final DataMiningService dataMiningService;

    public AnalyticsController(DataMiningService dataMiningService) {
        this.dataMiningService = dataMiningService;
    }

    /**
     * Endpoint to mine historical CPU anomalies.
     * @param windowMinutes The number of historical minutes to analyze (e.g., 5, 10, 60).
     */
    @GetMapping("/cpu")
    public ResponseEntity<Map<String, Object>> getCpuAnalytics(
            @RequestParam(defaultValue = "15") int windowMinutes) {
        
        Map<String, Object> analysis = dataMiningService.analyzeCpuAnomalies(windowMinutes);
        return ResponseEntity.ok(analysis);
    }

    /**
     * Endpoint to evaluate memory pressure.
     * @param windowMinutes The number of historical minutes to analyze.
     */
    @GetMapping("/memory")
    public ResponseEntity<Map<String, Object>> getMemoryAnalytics(
            @RequestParam(defaultValue = "15") int windowMinutes) {
        
        Map<String, Object> analysis = dataMiningService.analyzeMemoryPressure(windowMinutes);
        return ResponseEntity.ok(analysis);
    }
}
