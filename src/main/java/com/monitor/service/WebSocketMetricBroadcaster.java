package com.monitor.service;

import com.monitor.entity.SystemMetric;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for broadcasting system metrics to connected WebSocket clients.
 * This component demonstrates Computer Network concepts (Publish-Subscribe, Full-Duplex communication).
 */
@Service
public class WebSocketMetricBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketMetricBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Broadcasts a list of metrics to all subscribers on the /topic/metrics destination.
     * @param metrics The metrics to broadcast.
     */
    public void broadcastMetrics(List<SystemMetric> metrics) {
        // Send the payload to the specific topic
        messagingTemplate.convertAndSend("/topic/metrics", metrics);
    }
}
