package dev.cruzs.gustavo.FlashSale.payment.commands;

import java.math.BigDecimal;

public record PaymentProcessCreditCartCommand (
    String externalReference,
    BigDecimal amount,
    String idempotencyKey,
    String email,
    String token,
    String paymentMethodId,
    Integer installments
) {
}
