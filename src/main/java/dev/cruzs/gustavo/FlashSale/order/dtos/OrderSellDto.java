package dev.cruzs.gustavo.FlashSale.order.dtos;

import java.util.UUID;

public record OrderSellDto(UUID ticketId, String cpfHolder) {
}
