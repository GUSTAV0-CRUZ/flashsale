package dev.cruzs.gustavo.FlashSale.payment.exceptions;

public class MercadoPagoException extends RuntimeException {
  public MercadoPagoException(String message, Throwable cause) {
      super("Error in process payment with Mercado Pago", cause);
  }
}
