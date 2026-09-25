package dev.cruzs.gustavo.FlashSale.order.exceptions;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException() {
        super("Order not found");
    }
}
