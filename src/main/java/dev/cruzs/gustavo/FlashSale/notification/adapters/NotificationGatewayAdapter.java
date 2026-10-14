package dev.cruzs.gustavo.FlashSale.notification.adapters;

import dev.cruzs.gustavo.FlashSale.notification.exceptions.NotificationFailException;
import dev.cruzs.gustavo.FlashSale.notification.gateways.NotificationGateway;
import org.springframework.stereotype.Component;

@Component
public class NotificationGatewayAdapter implements NotificationGateway {
  @Override
  public void sendToEmail(String title, String message, String email) throws NotificationFailException {

  }
}
