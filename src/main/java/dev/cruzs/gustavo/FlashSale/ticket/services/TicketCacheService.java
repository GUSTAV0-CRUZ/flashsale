package dev.cruzs.gustavo.FlashSale.ticket.services;

import dev.cruzs.gustavo.FlashSale.ticket.enums.TicketStatusEnum;
import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import dev.cruzs.gustavo.FlashSale.ticket.repositories.TicketJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketCacheService {
  private final Logger logger = LoggerFactory.getLogger(TicketCacheService.class);

  private final TicketJpaRepository ticketJpaRepository;

  public TicketCacheService(TicketJpaRepository ticketJpaRepository) {
    this.ticketJpaRepository = ticketJpaRepository;
  }

  @Cacheable(value = "tickets", key = "#ticketStatus.getValue()")
  public List<TicketModel> getOrLoadFindAllByStatus(TicketStatusEnum ticketStatus) {
    logger.info("Load in DB");

    return ticketJpaRepository.findAllByStatus(ticketStatus, Limit.of(100));
  }
}
