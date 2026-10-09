package com.streamflix.repository;
import com.streamflix.entity.ProcessedWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProcessedWebhookEventRepository extends JpaRepository<ProcessedWebhookEvent, Long> { boolean existsByEventId(String eventId); }
