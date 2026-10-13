package dev.cruzs.gustavo.FlashSale.notification.enums;

public enum NotificationStatusEnum {
  PENDING("PENDING"),
  SENT("SENT"),
  FAILED("FAILED");

  private final String value;

  NotificationStatusEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
