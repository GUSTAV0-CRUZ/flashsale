package dev.cruzs.gustavo.FlashSale.order.dtos;

import dev.cruzs.gustavo.FlashSale.payment.dtos.QrCodeDto;

import java.util.UUID;

public record OrderSellResponseDto(UUID orderId, QrCodeDto qrCodeDto) {
}
