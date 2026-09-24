package dev.cruzs.gustavo.FlashSale.ticket.exceptions;

public class TicketNotFoundException extends RuntimeException {
  public TicketNotFoundException() {
    super("Ticket not found.");
  }
}
