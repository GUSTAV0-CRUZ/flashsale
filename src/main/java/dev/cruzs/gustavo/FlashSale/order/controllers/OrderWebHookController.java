package dev.cruzs.gustavo.FlashSale.order.controllers;

import dev.cruzs.gustavo.FlashSale.order.services.OrderWebHookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/order/webHook")
public class OrderWebHookController {
  private final OrderWebHookService orderWebHookService;

  public OrderWebHookController(OrderWebHookService orderWebHookService) {
    this.orderWebHookService = orderWebHookService;
  }

  @PostMapping(value = "/payment")
  public ResponseEntity<Void> orderPayment(@RequestBody Map<String, Object> mercadoPagoOrderResponseDto) {
    orderWebHookService.orderPayment(mercadoPagoOrderResponseDto);
    return ResponseEntity.ok().build();
  }
}
