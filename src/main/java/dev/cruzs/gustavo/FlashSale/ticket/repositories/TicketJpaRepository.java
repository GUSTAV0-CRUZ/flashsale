package dev.cruzs.gustavo.FlashSale.ticket.repositories;

import dev.cruzs.gustavo.FlashSale.ticket.enums.TicketStatusEnum;
import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TicketJpaRepository extends JpaRepository<TicketModel, UUID> {
  @Cacheable("ticketIds")
  @Query("SELECT t.id FROM TicketModel t WHERE t.status = :status")
  List<UUID> findIdsByStatus(@Param("status") TicketStatusEnum status);

  List<TicketModel> findByIdIn(List<UUID> ids);
}
