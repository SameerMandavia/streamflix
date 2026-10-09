package com.streamflix.repository;
import com.streamflix.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> { List<Subscription> findByUserIdOrderByUpdatedAtDesc(Long userId); Optional<Subscription> findFirstByUserIdOrderByUpdatedAtDesc(Long userId); Optional<Subscription> findByStripeSubscriptionId(String id); long countByStatus(String status); }
