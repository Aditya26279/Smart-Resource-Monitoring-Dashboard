package com.monitor.repository;

import com.monitor.entity.MetricType;
import com.monitor.entity.SystemMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemMetricRepository extends JpaRepository<SystemMetric, Long> {

    List<SystemMetric> findByMetricTypeAndTimestampAfter(MetricType metricType, LocalDateTime timestamp);

    @Query("SELECT AVG(m.metricValue) FROM SystemMetric m WHERE m.metricType = :metricType AND m.metricName = :metricName AND m.timestamp >= :since")
    Double getAverageMetricValueSince(@Param("metricType") MetricType metricType, @Param("metricName") String metricName, @Param("since") LocalDateTime since);

    @Query("SELECT MAX(m.metricValue) FROM SystemMetric m WHERE m.metricType = :metricType AND m.metricName = :metricName AND m.timestamp >= :since")
    Double getMaxMetricValueSince(@Param("metricType") MetricType metricType, @Param("metricName") String metricName, @Param("since") LocalDateTime since);
}
