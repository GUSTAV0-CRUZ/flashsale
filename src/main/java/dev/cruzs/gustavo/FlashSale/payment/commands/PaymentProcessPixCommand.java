package dev.cruzs.gustavo.FlashSale.payment.commands;

import java.math.BigDecimal;

public record PaymentProcessPixCommand(
    String externalReference,
    BigDecimal amount,
    String idempotencyKey,
    String email
) {
}
