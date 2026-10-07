package dev.cruzs.gustavo.FlashSale.order.exceptions;

public class MercadoPagoWebhookSignatureException extends RuntimeException {
  public MercadoPagoWebhookSignatureException() {
        super("Error al oftener el Webhook Signature of Mercado Pago");
    }
}
