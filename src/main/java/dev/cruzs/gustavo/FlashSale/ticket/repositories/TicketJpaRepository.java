package dev.cruzs.gustavo.FlashSale.ticket.repositories;

import dev.cruzs.gustavo.FlashSale.ticket.enums.TicketStatusEnum;
import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketJpaRepository extends JpaRepository<TicketModel, UUID> {
  List<TicketModel> findAllByStatus(TicketStatusEnum status, Limit limit);
}
