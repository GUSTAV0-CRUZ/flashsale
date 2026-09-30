package dev.cruzs.gustavo.FlashSale.order.dtos;

import dev.cruzs.gustavo.FlashSale.payment.enums.PaymentTypeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record SellRequestDto(
    @NotNull
    @NotEmpty
    @CPF
    String cpfHolder,

    @NotNull
    PaymentTypeEnum paymentType,

    @NotNull
    @NotEmpty
    @Email
    String email,
    String token,
    String paymentMethodId,
    Integer installment
) {
}
