package dev.cruzs.gustavo.FlashSale.ticket.repositories;

import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketJpaRepository extends JpaRepository<TicketModel, UUID> {
}
