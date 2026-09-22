package dev.cruzs.gustavo.FlashSale.ticket.controllers;

import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import dev.cruzs.gustavo.FlashSale.ticket.services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/tickets")
public class TicketController {
  private final TicketService ticketService;

  public TicketController(TicketService ticketService) {
    this.ticketService = ticketService;
  }

  @GetMapping
  public ResponseEntity<List<TicketModel>> ticketModel() {
    return ResponseEntity.ok(ticketService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TicketModel> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(ticketService.findByIdOrTicketNotFoundException(id));
  }
}
