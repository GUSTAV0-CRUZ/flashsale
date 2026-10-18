package dev.cruzs.gustavo.FlashSale.notification.services;

import dev.cruzs.gustavo.FlashSale.notification.enums.NotificationStatusEnum;
import dev.cruzs.gustavo.FlashSale.notification.exceptions.NotificationFailException;
import dev.cruzs.gustavo.FlashSale.notification.gateways.NotificationGateway;
import dev.cruzs.gustavo.FlashSale.notification.models.NotificationModel;
import dev.cruzs.gustavo.FlashSale.notification.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NotificationService {
  private final Logger logger = LoggerFactory.getLogger(NotificationService.class);
  private final NotificationGateway  notificationGateway;
  private final NotificationRepository notificationRepository;

  public NotificationService(
      NotificationGateway notificationGateway,
      NotificationRepository notificationRepository
  ) {
    this.notificationGateway = notificationGateway;
    this.notificationRepository = notificationRepository;
  }

  public void sendToEmail(String title, String message, String email) {
    NotificationModel notificationModel = NotificationModel.create(title, message, email);
    notificationRepository.save(notificationModel);

    logger.info("Try send notification to email={}, timestamp={}", email, notificationModel.getCreatedAt());
    try {
      notificationGateway.sendToEmail(
          notificationModel.getTitle(),
          notificationModel.getMessage(),
          notificationModel.getToEmail()
      );

      notificationModel.changeStatus(NotificationStatusEnum.SENT);
      notificationRepository.save(notificationModel);
    } catch (NotificationFailException e) {
      logger.warn("Fail send notification to email={}, timestamp={}", email, Instant.now());

      notificationModel.changeStatus(NotificationStatusEnum.FAILED);
      notificationRepository.save(notificationModel);
    }
  }
}
