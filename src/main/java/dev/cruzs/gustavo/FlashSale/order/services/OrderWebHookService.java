package dev.cruzs.gustavo.FlashSale.order.services;

import dev.cruzs.gustavo.FlashSale.ticket.services.TicketService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class OrderWebHookService {
  private final TicketService ticketService;

  public OrderWebHookService(TicketService ticketService) {
    this.ticketService = ticketService;
  }

  public void orderPayment(Map<String, Object> mercadoPagoOrderResponseDto) {
    Map<String, Object> data = (Map<String, Object>) mercadoPagoOrderResponseDto.get("data");
    UUID idOrder = (UUID) data.get("external_reference");
    String action = (String) mercadoPagoOrderResponseDto.get("action");

    if (action.equals("order.failed")) {
      ticketService.refund(idOrder);
      //  Add call to the notification system

      return;
    }

    ticketService.confirmSale(idOrder);
    //  Add call to the notification system
  }
}
