package dev.cruzs.gustavo.FlashSale.ticket.services;

import dev.cruzs.gustavo.FlashSale.ticket.enums.TicketStatusEnum;
import dev.cruzs.gustavo.FlashSale.ticket.exceptions.TicketNotFoundException;
import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import dev.cruzs.gustavo.FlashSale.ticket.repositories.TicketJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TicketService {
  private final Logger logger = LoggerFactory.getLogger(TicketService.class);

  private final TicketJpaRepository ticketJpaRepository;
  private final TicketCacheService ticketCacheService;

  public TicketService(TicketJpaRepository ticketJpaRepository, TicketCacheService ticketCacheService) {
    this.ticketJpaRepository = ticketJpaRepository;
    this.ticketCacheService = ticketCacheService;
  }

  public List<TicketModel> findAllRandomLimitFivePerStatusAvailable() {
    logger.info("Find five tickets per status AVAILABLE");

    List<TicketModel> ticketsModel = ticketCacheService.getOrLoadFindAllByStatus(TicketStatusEnum.AVAILABLE);

    if (ticketsModel.isEmpty()) return Collections.emptyList();

    List<TicketModel> mutableTicketModels = new ArrayList<>(ticketsModel);
    Collections.shuffle(mutableTicketModels);

    return mutableTicketModels.stream().limit(5).toList();
  }

  @Transactional(readOnly = true)
  public TicketModel findByIdOrTicketNotFoundException(UUID id) {
    logger.info("Find a ticket by id={}", id);

    return ticketJpaRepository.findById(id).orElseThrow(
        TicketNotFoundException::new
    );
  }

  @CacheEvict(value = "tickets", key = "'AVAILABLE'")
  @Transactional
  public void sell(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);

    ticketModel.sell();
    ticketJpaRepository.save(ticketModel);

    logger.info("Ticket with id={} will be sold, status={}.", id, ticketModel.getStatus());
  }

  @Transactional
  public void confirmSale(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);

    ticketModel.confirmSale();
    ticketJpaRepository.save(ticketModel);

    logger.info("Ticket with id={} has been sold, status={}.", id, ticketModel.getStatus());
  }

  @Transactional
  public TicketModel cancelSale(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);

    ticketModel.cancelSale();
    ticketJpaRepository.save(ticketModel);

    logger.info("Ticket with id={} has been canceled, status={}.", id, ticketModel.getStatus());

    return ticketModel;
  }

  @Transactional
  public TicketModel refund(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);

    ticketModel.refund();
    ticketJpaRepository.save(ticketModel);

    logger.info("Ticket with id={} has been refunded, status={}.", id, ticketModel.getStatus());

    return ticketModel;
  }
}
