package dev.cruzs.gustavo.FlashSale.notification.gateways;

import dev.cruzs.gustavo.FlashSale.notification.exceptions.NotificationFailException;

public interface NotificationGateway {
  void sendToEmail(String title, String message, String email) throws NotificationFailException;
}
