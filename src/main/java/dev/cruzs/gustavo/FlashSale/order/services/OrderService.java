package dev.cruzs.gustavo.FlashSale.order.services;

import dev.cruzs.gustavo.FlashSale.notification.services.NotificationService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Service
public class OrderService {
  private final Logger logger = LoggerFactory.getLogger(OrderService.class);
  private final TransactionTemplate transactionTemplate;

  private final OrderJpaRepository orderJpaRepository;
  private final TicketService ticketService;
  private final PaymentService paymentService;
  private final NotificationService notificationService;

  public OrderService(
      TransactionTemplate transactionTemplate, OrderJpaRepository orderJpaRepository,
      TicketService ticketService,
      PaymentService paymentService,
      NotificationService notificationService
  ) {
    this.transactionTemplate = transactionTemplate;
    this.orderJpaRepository = orderJpaRepository;
    this.ticketService = ticketService;
    this.paymentService = paymentService;
    this.notificationService = notificationService;
  }

  public OrderModel findByIdOrOrderNotFoundException(UUID id) {
    return orderJpaRepository.findById(id).orElseThrow(
        OrderNotFoundException::new
    );
  }

  public QrCodeDto sell(OrderSellDto orderSellDto) {
    TicketModel ticketModel = ticketService.findByIdOrTicketNotFoundException(orderSellDto.ticketId());
    OrderModel orderModel = OrderModel.create(
        orderSellDto.ticketId(),
        orderSellDto.cpfHolder(),
        orderSellDto.email(),
        ticketModel.getPrice()
    );

    transactionTemplate.execute(
        status -> {
          ticketService.sell(orderSellDto.ticketId());
          orderJpaRepository.save(orderModel);

          return null;
        }
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

    logger.info("Create order with id={} and status={}.", orderModel.getId(), orderModel.getStatus().getValue());
    return qrCodeDto;
  }

  public void confirmSale(UUID id) {
    OrderModel orderModel = findByIdOrOrderNotFoundException(id);

    orderModel.changeStatus(OrderStatusEnum.CONFIRMED);
    transactionTemplate.execute(
        status -> {
          ticketService.confirmSale(orderModel.getTicketId());
          orderJpaRepository.save(orderModel);

          return null;
        }
    );

    notificationService.sendToEmail(
        "Confirm sell",
        "Sold Ticket with id of order=" + orderModel.getTicketId() + " has been confirmed",
        orderModel.getEmailHolder()
    );
    logger.info("Update order with id={}. status={}.", id, orderModel.getStatus().getValue());
  }

  public void cancelled(UUID id) {
    OrderModel orderModel = findByIdOrOrderNotFoundException(id);

    transactionTemplate.execute(
        status -> {
          ticketService.cancelSale(orderModel.getTicketId());
          orderModel.changeStatus(OrderStatusEnum.CANCELLED);
          orderJpaRepository.save(orderModel);

          return null;
        }
    );

    notificationService.sendToEmail(
        "Cancel sell",
        "Cancel sell of Ticket with id=" +
            orderModel.getTicketId() +
            "and with id of order=" +
            orderModel.getId(),
        orderModel.getEmailHolder()
    );
    logger.info("Update order with id={}. status={}.", id, orderModel.getStatus().getValue());
  }

  public void actionRequired(UUID id) {
    OrderModel orderModel = findByIdOrOrderNotFoundException(id);

    notificationService.sendToEmail(
        "Required action",
        "Sell of Ticket pending, with id of order=" + orderModel.getId(),
        orderModel.getEmailHolder()
    );
    logger.info("Order with id: {} is required action", id);
  }

  public void refund(UUID id) {
    OrderModel orderModel = findByIdOrOrderNotFoundException(id);

    transactionTemplate.execute(
        status -> {
          ticketService.refund(orderModel.getTicketId());
          orderModel.changeStatus(OrderStatusEnum.REFUND);
          orderJpaRepository.save(orderModel);

          return null;
        }
    );

//    Add call to the payment system
    logger.info("Update order with id={}. status={}", id, orderModel.getStatus().getValue());
  }
}
