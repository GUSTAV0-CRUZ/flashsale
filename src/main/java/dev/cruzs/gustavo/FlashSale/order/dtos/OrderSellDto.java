package dev.cruzs.gustavo.FlashSale.order.dtos;

import dev.cruzs.gustavo.FlashSale.payment.enums.PaymentTypeEnum;

import java.util.UUID;

public record OrderSellDto(
    UUID ticketId,
    String cpfHolder,
    PaymentTypeEnum paymentType,
    String email,
    String token,
    String paymentMethodId,
    Integer installments
) {
}
