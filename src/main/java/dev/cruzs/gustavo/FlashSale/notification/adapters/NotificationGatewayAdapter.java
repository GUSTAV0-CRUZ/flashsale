package dev.cruzs.gustavo.FlashSale.notification.adapters;

import dev.cruzs.gustavo.FlashSale.notification.exceptions.NotificationFailException;
import dev.cruzs.gustavo.FlashSale.notification.gateways.NotificationGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationGatewayAdapter implements NotificationGateway {
  private final Logger logger = LoggerFactory.getLogger(NotificationGatewayAdapter.class);

  @Override
  public void sendToEmail(String title, String message, String email) throws NotificationFailException {
    logger.info("Send notification to email={}\ntitle={}\nmessage={}", email, title, message);
  }
}
