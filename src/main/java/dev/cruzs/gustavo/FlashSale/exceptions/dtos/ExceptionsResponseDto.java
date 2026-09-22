package dev.cruzs.gustavo.FlashSale.exceptions.dtos;

import java.time.Instant;

public record ExceptionsResponseDto(
    Integer status,
    String error,
    Instant timestamp,
    String path
) {
}
