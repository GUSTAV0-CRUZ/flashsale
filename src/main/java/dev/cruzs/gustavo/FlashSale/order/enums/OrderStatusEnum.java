package dev.cruzs.gustavo.FlashSale.order.enums;

public enum OrderStatusEnum {
  PENDING ("PENDING"),
  CONFIRMED("CONFIRMED"),
  REFUND("REFUND");

  private final String value;

  OrderStatusEnum(String value) {
    this.value = value;
  }
  public String getValue() {
    return this.value;
  }
}
