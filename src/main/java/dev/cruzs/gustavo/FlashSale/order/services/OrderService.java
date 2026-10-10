package dev.cruzs.gustavo.FlashSale.order.services;

import dev.cruzs.gustavo.FlashSale.order.dtos.OrderSellDto;
import dev.cruzs.gustavo.FlashSale.order.enums.OrderStatusEnum;
import dev.cruzs.gustavo.FlashSale.order.exceptions.OrderNotFoundException;
import dev.cruzs.gustavo.FlashSale.order.models.OrderModel;
import dev.cruzs.gustavo.FlashSale.order.repositories.OrderJpaRepository;
import dev.cruzs.gustavo.FlashSale.payment.dtos.ProcessPaymentDto;
import dev.cruzs.gustavo.FlashSale.payment.dtos.QrCodeDto;
import dev.cruzs.gustavo.FlashSale.payment.services.PaymentService;
import dev.cruzs.gustavo.FlashSale.ticket.models.TicketModel;
import dev.cruzs.gustavo.FlashSale.ticket.services.TicketService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
  private final OrderJpaRepository orderJpaRepository;
  private final TicketService ticketService;
  private final PaymentService paymentService;

  public OrderService(
      OrderJpaRepository orderJpaRepository,
      TicketService ticketService,
      PaymentService paymentService
  ) {
    this.orderJpaRepository = orderJpaRepository;
    this.ticketService = ticketService;
    this.paymentService = paymentService;
  }

  public OrderModel findByIdOrOrderNotFoundException(UUID id) {
    return orderJpaRepository.findById(id).orElseThrow(
        OrderNotFoundException::new
    );
  }

  @Transactional
  public QrCodeDto sell(OrderSellDto orderSellDto) {
    TicketModel ticketModel = ticketService.findByIdOrTicketNotFoundException(orderSellDto.ticketId());
    ticketService.sell(orderSellDto.ticketId(), orderSellDto.cpfHolder());

    OrderModel orderModel = OrderModel.create(
        orderSellDto.ticketId(),
        orderSellDto.cpfHolder(),
        ticketModel.getPrice()
    );

    QrCodeDto qrCodeDto = paymentService.processPayment(
        new ProcessPaymentDto(
            orderSellDto.paymentType(),
            orderModel.getId().toString(),
            orderModel.getTicketValue(),
            orderModel.getIdempotencyKey(),
            orderSellDto.email(),
            orderSellDto.token(),
            orderSellDto.paymentMethodId(),
            orderSellDto.installments()
        )
    );

    orderJpaRepository.save(orderModel);
    return qrCodeDto;
  }

  @Transactional
  public void confirmSale(UUID id) {
    OrderModel orderModel = findByIdOrOrderNotFoundException(id);

    orderModel.changeStatus(OrderStatusEnum.CONFIRMED);
    ticketService.confirmSale(orderModel.getTicketId());
    orderJpaRepository.save(orderModel);

//    Add call to the notification system
  }

  public void cancelled(UUID id) {
    OrderModel orderModel = findByIdOrOrderNotFoundException(id);

    ticketService.refund(orderModel.getTicketId());
    orderModel.changeStatus(OrderStatusEnum.CANCELLED);
    orderJpaRepository.save(orderModel);

//    Add call to the notification system
  }

  public void refund(UUID id) {
    OrderModel orderModel = findByIdOrOrderNotFoundException(id);

    ticketService.refund(orderModel.getTicketId());
    orderModel.changeStatus(OrderStatusEnum.REFUND);
    orderJpaRepository.save(orderModel);

//    Add call to the payment system
//    Add call to the notification system
  }
}
