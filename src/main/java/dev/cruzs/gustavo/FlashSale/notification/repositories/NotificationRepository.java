package dev.cruzs.gustavo.FlashSale.notification.repositories;

import dev.cruzs.gustavo.FlashSale.notification.models.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {
}
