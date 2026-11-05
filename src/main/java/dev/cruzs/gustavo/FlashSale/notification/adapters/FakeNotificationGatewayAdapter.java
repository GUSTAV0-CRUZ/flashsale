package dev.cruzs.gustavo.FlashSale.notification.adapters;

import dev.cruzs.gustavo.FlashSale.notification.exceptions.NotificationFailException;
import dev.cruzs.gustavo.FlashSale.notification.gateways.NotificationGateway;
import dev.cruzs.gustavo.FlashSale.utils.LoadingTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Primary
@Profile("test")
@Component
public class FakeNotificationGatewayAdapter implements NotificationGateway {
  private final Logger logger = LoggerFactory.getLogger(FakeNotificationGatewayAdapter.class);

  @Override
  public void sendToEmail(String title, String message, String email) throws NotificationFailException {
    LoadingTimeUtil.load();
    logger.info("Fake send with title={} and message{}, to email={}", title, message, email);
  }
}
