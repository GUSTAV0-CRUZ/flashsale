package dev.cruzs.gustavo.FlashSale.order.services;

import dev.cruzs.gustavo.FlashSale.order.dtos.OrderSellDto;
import dev.cruzs.gustavo.FlashSale.order.enums.OrderStatusEnum;
import dev.cruzs.gustavo.FlashSale.order.exceptions.OrderNotFoundException;
import dev.cruzs.gustavo.FlashSale.order.models.OrderModel;
import dev.cruzs.gustavo.FlashSale.order.repositories.OrderJpaRepository;
import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import dev.cruzs.gustavo.FlashSale.ticket.services.TicketService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
  private final OrderJpaRepository orderJpaRepository;
  private final TicketService ticketService;

  public OrderService(OrderJpaRepository orderJpaRepository, TicketService ticketService) {
    this.orderJpaRepository = orderJpaRepository;
    this.ticketService = ticketService;
  }

  public OrderModel findByIdOrOrderNotFoundException(UUID id) {
    return orderJpaRepository.findById(id).orElseThrow(
        OrderNotFoundException::new
    );
  }

  public String sell(OrderSellDto orderSellDto) {
    TicketModel ticketModel = ticketService.findByIdOrTicketNotFoundException(orderSellDto.ticketId());
    ticketService.sell(orderSellDto.ticketId(), orderSellDto.cpfHolder());

    OrderModel orderModel = OrderModel.create(
        orderSellDto.ticketId(),
        orderSellDto.cpfHolder(),
        ticketModel.getPrice()
    );
    orderJpaRepository.save(orderModel);

//    Add call to the payment system
    return "Return of payment system";
  }

  public void confirmSale(UUID id) {
    OrderModel orderModel = findByIdOrOrderNotFoundException(id);

    orderModel.changeStatus(OrderStatusEnum.CONFIRMED);
    orderJpaRepository.save(orderModel);

//    Add call to the notification system
  }

  public void refund(UUID id) {
    OrderModel orderModel = findByIdOrOrderNotFoundException(id);

    ticketService.refund(orderModel.getTicketId());

    orderModel.changeStatus(OrderStatusEnum.REFUND);
    orderJpaRepository.save(orderModel);

//    Add call to the notification system
  }
}
