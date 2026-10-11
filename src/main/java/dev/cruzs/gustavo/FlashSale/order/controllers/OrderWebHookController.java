package dev.cruzs.gustavo.FlashSale.order.controllers;

import dev.cruzs.gustavo.FlashSale.order.gateways.OrderWebHookGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/order/webHook")
public class OrderWebHookController {
  private final OrderWebHookGateway orderWebHookGateway;

  public OrderWebHookController(OrderWebHookGateway orderWebHookGateway) {
    this.orderWebHookGateway = orderWebHookGateway;
  }

  @PostMapping(value = "/payment")
  public ResponseEntity<Void> orderPayment(@RequestBody Map<String, Object> requestBody) {
    orderWebHookGateway.orderPayment(requestBody);
    return ResponseEntity.ok().build();
  }
}
