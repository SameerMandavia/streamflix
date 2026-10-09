package com.streamflix.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name = "processed_webhook_events")
public class ProcessedWebhookEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "event_id", nullable = false, unique = true) private String eventId;
    @Column(name = "received_at", nullable = false) private Instant receivedAt;
    protected ProcessedWebhookEvent() {}
    public ProcessedWebhookEvent(String eventId) { this.eventId = eventId; this.receivedAt = Instant.now(); }
}
