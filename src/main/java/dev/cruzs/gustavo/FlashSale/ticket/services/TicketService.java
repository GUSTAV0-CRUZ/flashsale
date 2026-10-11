package dev.cruzs.gustavo.FlashSale.ticket.services;

import dev.cruzs.gustavo.FlashSale.ticket.exceptions.TicketNotFoundException;
import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import dev.cruzs.gustavo.FlashSale.ticket.repositories.TicketJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {
  private final Logger logger = LoggerFactory.getLogger(TicketService.class);
  private final TicketJpaRepository ticketJpaRepository;

  public TicketService(TicketJpaRepository ticketJpaRepository) {
    this.ticketJpaRepository = ticketJpaRepository;
  }

  public List<TicketModel> findAll() {
    logger.info("Find all tickets");

    return this.ticketJpaRepository.findAll();
  }

  public TicketModel findByIdOrTicketNotFoundException(UUID id) {
    logger.info("Searches for a ticket by id={} .", id);

    return ticketJpaRepository.findById(id).orElseThrow(
        TicketNotFoundException::new
    );
  }

  public void sell(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);
    ticketModel.sell();

    ticketJpaRepository.save(ticketModel);

    logger.info("Ticket with id={} will be sold, status={}.", id, ticketModel.getStatus());
  }

  public TicketModel confirmSale(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);

    ticketModel.confirmSale();
    ticketJpaRepository.save(ticketModel);

    logger.info("Ticket with id={} has been sold, status={}.", id, ticketModel.getStatus());

    return ticketModel;
  }

  public TicketModel cancelSale(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);

    ticketModel.cancelSale();

    logger.info("Ticket with id={} has been canceled, status={}.", id, ticketModel.getStatus());

    return ticketJpaRepository.save(ticketModel);
  }

  public void refund(UUID id) {
    var ticketModel = findByIdOrTicketNotFoundException(id);

    ticketModel.refund();

    logger.info("Ticket with id={} has been refunded, status={}.", id, ticketModel.getStatus());

    ticketJpaRepository.save(ticketModel);
  }
}
