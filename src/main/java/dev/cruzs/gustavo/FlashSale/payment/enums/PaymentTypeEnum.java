package dev.cruzs.gustavo.FlashSale.payment.enums;

public enum PaymentTypeEnum {
  PIX("PIX"),
  CREDIT_CARD("CREDIT_CARD"),;

  private final String value;

  PaymentTypeEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
