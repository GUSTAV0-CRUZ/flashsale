package dev.cruzs.gustavo.FlashSale.payment.dtos;

import dev.cruzs.gustavo.FlashSale.payment.enums.PaymentTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProcessPaymentDto(
    @NotNull
    PaymentTypeEnum paymentType,

    @NotNull
    @NotEmpty
    String externalReference,

    @NotNull
    BigDecimal amount,

    @NotNull
    @NotEmpty
    String IdempotencyKey,

    @NotNull
    @NotEmpty
    String email,

    @NotEmpty
    String token,

    @NotEmpty
    String paymentMethodId,
    Integer installments
) {
}
