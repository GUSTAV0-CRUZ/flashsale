package dev.cruzs.gustavo.FlashSale.ticket.dtos;

import dev.cruzs.gustavo.FlashSale.ticket.enums.TicketStatus;

import java.time.Instant;
import java.util.UUID;

public record QrCodeDto(
    UUID id,
    String cpfHolder,
    String name,
    Instant date,
    String location,
    String sector,
    String type,
    TicketStatus status
) {
}
