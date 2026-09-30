package dev.cruzs.gustavo.FlashSale.order.controllers;

import dev.cruzs.gustavo.FlashSale.order.dtos.OrderSellDto;
import dev.cruzs.gustavo.FlashSale.order.dtos.SellRequestDto;
import dev.cruzs.gustavo.FlashSale.order.services.OrderService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/tickets")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/{id}/buy")
  public ResponseEntity<String> sell(@NotNull @PathVariable UUID id, @RequestBody SellRequestDto sellRequestDto) {
    orderService.sell(
        new OrderSellDto(
            id,
            sellRequestDto.cpfHolder(),
            sellRequestDto.paymentType(),
            sellRequestDto.email(),
            sellRequestDto.token(),
            sellRequestDto.paymentMethodId(),
            sellRequestDto.installment()
        )
    );

    return ResponseEntity.accepted().build();
  }

  @PatchMapping("/{orderId}/refund")
  public ResponseEntity<Void> refund(@NotNull @PathVariable UUID orderId) {
    orderService.refund(orderId);

    return ResponseEntity.noContent().build();
  }
}
