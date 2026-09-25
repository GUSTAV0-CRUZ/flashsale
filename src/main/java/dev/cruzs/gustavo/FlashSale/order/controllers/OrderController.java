package dev.cruzs.gustavo.FlashSale.order.controllers;

import dev.cruzs.gustavo.FlashSale.order.dtos.OrderSellDto;
import dev.cruzs.gustavo.FlashSale.order.dtos.SellRequestDto;
import dev.cruzs.gustavo.FlashSale.order.services.OrderService;
import jakarta.validation.constraints.NotNull;
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
  public String sell(@NotNull @PathVariable UUID id, @RequestBody SellRequestDto sellRequestDto) {
    return orderService.sell(
        new OrderSellDto(id,sellRequestDto.cpfHolder())
    );
  }
}
