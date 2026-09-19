package dev.cruzs.gustavo.FlashSale.ticket.enums;

public enum TicketStatus {
  AVAILABLE ("AVAILABLE"),
  PENDING ("PENDING"),
  SOLD ("SOLD");

  private final String value;
  TicketStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
