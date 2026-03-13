package com.monitor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class SystemMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MetricType metricType;

    private String metricName;

    private Double metricValue;

    private String metricUnit;

    private LocalDateTime timestamp;

    public SystemMetric() {
    }

    public SystemMetric(MetricType metricType, String metricName, Double metricValue, String metricUnit, LocalDateTime timestamp) {
        this.metricType = metricType;
        this.metricName = metricName;
        this.metricValue = metricValue;
        this.metricUnit = metricUnit;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MetricType getMetricType() { return metricType; }
    public void setMetricType(MetricType metricType) { this.metricType = metricType; }
    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    public Double getMetricValue() { return metricValue; }
    public void setMetricValue(Double metricValue) { this.metricValue = metricValue; }
    public String getMetricUnit() { return metricUnit; }
    public void setMetricUnit(String metricUnit) { this.metricUnit = metricUnit; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static SystemMetricBuilder builder() {
        return new SystemMetricBuilder();
    }

    public static class SystemMetricBuilder {
        private MetricType metricType;
        private String metricName;
        private Double metricValue;
        private String metricUnit;
        private LocalDateTime timestamp;

        public SystemMetricBuilder metricType(MetricType metricType) { this.metricType = metricType; return this; }
        public SystemMetricBuilder metricName(String metricName) { this.metricName = metricName; return this; }
        public SystemMetricBuilder metricValue(Double metricValue) { this.metricValue = metricValue; return this; }
        public SystemMetricBuilder metricUnit(String metricUnit) { this.metricUnit = metricUnit; return this; }
        public SystemMetricBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public SystemMetric build() { return new SystemMetric(metricType, metricName, metricValue, metricUnit, timestamp); }
    }
}
