package dev.cruzs.gustavo.FlashSale.order.models;

import dev.cruzs.gustavo.FlashSale.order.enums.OrderStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
  private OrderModel orderModel;

  @BeforeEach
  void setUp() {
    UUID ticketId =  UUID.randomUUID();
    String cpfHolder = "591.385.200-10";
    String emailHolder = "test@gmail.com";
    BigDecimal ticketValue = BigDecimal.valueOf(50);

    orderModel = OrderModel.create(ticketId, cpfHolder, emailHolder, ticketValue);
  }

  @Test
  void create() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm'Z'").withZone(ZoneOffset.UTC);
    String date = formatter.format(orderModel.getCreatedAt());
    String idempotencyKey = orderModel.getTicketId().toString() + "-" + orderModel.getCpfHolder() + "-" + date;

    assertNotNull(orderModel);
    assertNotNull(orderModel.getId());
    assertNotNull(orderModel.getTicketId());
    assertNotNull(orderModel.getCreatedAt());
    assertNotNull(orderModel.getUpdatedAt());
    assertEquals("591.385.200-10", orderModel.getCpfHolder());
    assertEquals("test@gmail.com",  orderModel.getEmailHolder());
    assertEquals(BigDecimal.valueOf(50),  orderModel.getTicketValue());
    assertEquals(idempotencyKey, orderModel.getIdempotencyKey());
    assertEquals(OrderStatusEnum.PENDING, orderModel.getStatus());
  }

  @DisplayName("changeStatusSuccess - Should changed order per status=CONFIRMED")
  @Test
  void changeStatusSuccess() {
    orderModel.changeStatus(OrderStatusEnum.CONFIRMED);

    assertEquals(OrderStatusEnum.CONFIRMED, orderModel.getStatus());
  }

  @DisplayName("changeStatusErrorCase1 - Should return error: Status cannot be null.")
  @Test
  void changeStatusErrorCase1() {
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> orderModel.changeStatus(null)
    );

    assertEquals("Status cannot be null.", result.getMessage());
  }

  @DisplayName("changeStatusErrorCase2 - Should return error: Status cannot be PENDING.")
  @Test
  void changeStatusErrorCase2() {
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> orderModel.changeStatus(OrderStatusEnum.PENDING)
    );

    assertEquals("Status cannot be PENDING.", result.getMessage());
  }

  @DisplayName("changeStatusErrorCase3 - Should return error: Status is already different from PENDING.")
  @Test
  void changeStatusErrorCase3() {
    orderModel.setStatus(OrderStatusEnum.CONFIRMED);
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> orderModel.changeStatus(OrderStatusEnum.CANCELLED)
    );

    assertEquals("Status is already different from PENDING.", result.getMessage());
  }
}