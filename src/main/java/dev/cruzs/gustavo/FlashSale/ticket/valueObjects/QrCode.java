package dev.cruzs.gustavo.FlashSale.ticket.valueObjects;

public class QrCode {
  private final String value;

  public QrCode(final String value) {
    this.value = checkValue(value);
  }

  private String checkValue(final String value) {
    if (null == value) throw new NullPointerException("Value cannot be null.");
    if (value.isEmpty()) throw new IllegalArgumentException("Value cannot be empty.");

    return value;
  }

  public String getValue() {
    return this.value;
  }
}
