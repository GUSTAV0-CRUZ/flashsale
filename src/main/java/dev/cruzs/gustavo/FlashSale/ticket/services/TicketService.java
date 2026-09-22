package dev.cruzs.gustavo.FlashSale.ticket.services;

import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import dev.cruzs.gustavo.FlashSale.ticket.repositories.TicketJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {
  private final TicketJpaRepository ticketJpaRepository;

  public TicketService(TicketJpaRepository ticketJpaRepository) {
    this.ticketJpaRepository = ticketJpaRepository;
  }

  public List<TicketModel> findAll() {
    return this.ticketJpaRepository.findAll();
  }

  public TicketModel findByIdOrIllegalArgumentException(UUID id) {
    return ticketJpaRepository.findById(id).orElseThrow(
        () -> new IllegalArgumentException("Ticket not found")
    );
  }

  public TicketModel sell(UUID id, String cpfHolder) {
    var ticketModel = findByIdOrIllegalArgumentException(id);
    ticketModel.sell(cpfHolder);

    return ticketJpaRepository.save(ticketModel);
  }

  public TicketModel confirmSale(UUID id) {
    var ticketModel = findByIdOrIllegalArgumentException(id);

    ticketModel.confirmSale();

    return ticketJpaRepository.save(ticketModel);
  }

  public TicketModel refund(UUID id) {
    var ticketModel = findByIdOrIllegalArgumentException(id);

    ticketModel.refund();

    return ticketJpaRepository.save(ticketModel);
  }

}
