package dev.cruzs.gustavo.FlashSale.ticket.models;

import dev.cruzs.gustavo.FlashSale.ticket.enums.TicketStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {
  private TicketModel ticketModel;

  @BeforeEach
  void setUp() {
    UUID id = UUID.randomUUID();
    String name = "Name Test";
    Instant date = Instant.now().plus(Duration.ofDays(7));
    String location = "Location of test";
    String sector = "Sector of test";
    String type = "Ticket Type of test";
    BigDecimal price = BigDecimal.valueOf(50);
    TicketStatusEnum status = TicketStatusEnum.AVAILABLE;
    Instant instant = Instant.now();

    ticketModel = new TicketModel(id, name, date, location, sector, type, price, status, instant, instant);
  }

  @DisplayName("sellSuccess - Should return ticket with status=PENDING")
  @Test
  void sellSuccess() {
    ticketModel.sell();

    assertEquals(TicketStatusEnum.PENDING, ticketModel.getStatus());
  }

  @DisplayName("sellError - Should return error: Status must be AVAILABLE.")
  @Test
  void sellError() {
    ticketModel.setStatus(TicketStatusEnum.PENDING);
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> ticketModel.sell()
    );

    assertEquals("Status must be AVAILABLE.", result.getMessage());
  }

  @DisplayName("confirmSaleSuccess - Should return ticket with status=SOLD")
  @Test
  void confirmSaleSuccess() {
    ticketModel.setStatus(TicketStatusEnum.PENDING);
    ticketModel.confirmSale();

    assertEquals(TicketStatusEnum.SOLD, ticketModel.getStatus());
  }

  @DisplayName("confirmSaleError - Should return error: Status must be PENDING.")
  @Test
  void confirmSaleError() {
    ticketModel.setStatus(TicketStatusEnum.AVAILABLE);
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> ticketModel.confirmSale()
    );

    assertEquals("Status must be PENDING.", result.getMessage());
  }

  @DisplayName("cancelSaleSuccess - Should return ticket with status=AVAILABLE")
  @Test
  void cancelSaleSuccess() {
    ticketModel.setStatus(TicketStatusEnum.PENDING);
    ticketModel.cancelSale();

    assertEquals(TicketStatusEnum.AVAILABLE, ticketModel.getStatus());
  }

  @DisplayName("cancelSaleError - Should return error: Status must be PENDING.")
  @Test
  void cancelSaleError() {
    ticketModel.setStatus(TicketStatusEnum.SOLD);
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> ticketModel.confirmSale()
    );

    assertEquals("Status must be PENDING.", result.getMessage());
  }

  @DisplayName("refundSuccess - Should return ticket with status=AVAILABLE")
  @Test
  void refundSuccess() {
    ticketModel.setStatus(TicketStatusEnum.SOLD);
    ticketModel.refund();

    assertEquals(TicketStatusEnum.AVAILABLE, ticketModel.getStatus());
  }

  @DisplayName("refundErrorCase1 - Should return error: Status must be SOLD.")
  @Test
  void refundErrorCase1() {
    ticketModel.setStatus(TicketStatusEnum.PENDING);
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> ticketModel.refund()
    );

    assertEquals("Status must be SOLD.", result.getMessage());
  }

  @DisplayName("refundErrorCase2 - Should return error: Event date has already passed, or is today.")
  @Test
  void refundErrorCase2() {
    ticketModel.setStatus(TicketStatusEnum.SOLD);
    ticketModel.setDate(Instant.now().minus(Duration.ofSeconds(1)));
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> ticketModel.refund()
    );

    assertEquals("Event date has already passed, or is today.", result.getMessage());
  }

  @DisplayName("refundErrorCase3 - Should return error: Deadline 7 day expired.")
  @Test
  void refundErrorCase3() {
    ticketModel.setStatus(TicketStatusEnum.SOLD);
    ticketModel.setUpdatedAt(Instant.now().minus(Duration.ofDays(8)));
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> ticketModel.refund()
    );

    assertEquals("Deadline 7 day expired.", result.getMessage());
  }

  @DisplayName("refundErrorCase4 - Should return error: Deadline 48 hours expired.")
  @Test
  void refundErrorCase4() {
    ticketModel.setStatus(TicketStatusEnum.SOLD);
    ticketModel.setDate(Instant.now().plus(Duration.ofDays(1)));
    ticketModel.setUpdatedAt(Instant.now());
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> ticketModel.refund()
    );

    assertEquals("Deadline 48 hours expired.", result.getMessage());
  }
}
