package dev.cruzs.gustavo.FlashSale.ticket.enums;

public enum TicketStatusEnum {
  AVAILABLE ("AVAILABLE"),
  PENDING ("PENDING"),
  SOLD ("SOLD");

  private final String value;
  TicketStatusEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
