package dev.cruzs.gustavo.FlashSale.ticket.services;

import dev.cruzs.gustavo.FlashSale.ticket.enums.TicketStatusEnum;
import dev.cruzs.gustavo.FlashSale.ticket.exceptions.TicketNotFoundException;
import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import dev.cruzs.gustavo.FlashSale.ticket.repositories.TicketJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {
  private final Logger logger = LoggerFactory.getLogger(TicketService.class);
  private final TicketJpaRepository ticketJpaRepository;

  public TicketService(TicketJpaRepository ticketJpaRepository) {
    this.ticketJpaRepository = ticketJpaRepository;
  }

  public List<TicketModel> findAllRandomLimitFivePerStatus() {
    logger.info("Find all tickets");

    List<UUID> ids = ticketJpaRepository.findIdsByStatus(TicketStatusEnum.AVAILABLE);

    if (ids.isEmpty()) return Collections.emptyList();

    Collections.shuffle(ids);
    List<UUID> randomIds = ids.stream().limit(5).toList();

    return ticketJpaRepository.findByIdIn(randomIds);
  }

  public TicketModel findByIdOrTicketNotFoundException(UUID id) {
    logger.info("Searches for a ticket by id={} .", id);

    return ticketJpaRepository.findById(id).orElseThrow(
        TicketNotFoundException::new
    );
  }

  @CacheEvict(value = "ticketIds", allEntries = true)
  public void sell(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);
    ticketModel.sell();

    ticketJpaRepository.save(ticketModel);

    logger.info("Ticket with id={} will be sold, status={}.", id, ticketModel.getStatus());
  }

  @CacheEvict(value = "ticketIds", allEntries = true)
  public TicketModel confirmSale(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);

    ticketModel.confirmSale();
    ticketJpaRepository.save(ticketModel);

    logger.info("Ticket with id={} has been sold, status={}.", id, ticketModel.getStatus());

    return ticketModel;
  }

  @CacheEvict(value = "ticketIds", allEntries = true)
  public TicketModel cancelSale(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);

    ticketModel.cancelSale();

    logger.info("Ticket with id={} has been canceled, status={}.", id, ticketModel.getStatus());

    return ticketJpaRepository.save(ticketModel);
  }

  @CacheEvict(value = "ticketIds", allEntries = true)
  public void refund(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);

    ticketModel.refund();

    logger.info("Ticket with id={} has been refunded, status={}.", id, ticketModel.getStatus());

    ticketJpaRepository.save(ticketModel);
  }
}
