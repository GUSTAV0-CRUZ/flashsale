package dev.cruzs.gustavo.FlashSale.order.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class OrderWebHookService {
  private final OrderService orderService;

  public OrderWebHookService(OrderService orderService) {
    this.orderService = orderService;
  }

  public void orderPayment(Map<String, Object> mercadoPagoOrderResponseDto) {
    Map<String, Object> data = (Map<String, Object>) mercadoPagoOrderResponseDto.get("data");
    UUID idOrder = UUID.fromString((String) data.get("external_reference"));
    String action = (String) mercadoPagoOrderResponseDto.get("action");

    if (action.equals("order.failed")) {
      orderService.refund(idOrder);
      //  Add call to the notification system

      return;
    }

    orderService.confirmSale(idOrder);
    //  Add call to the notification system
  }
}
