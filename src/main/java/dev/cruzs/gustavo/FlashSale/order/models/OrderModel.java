package dev.cruzs.gustavo.FlashSale.order.models;

import dev.cruzs.gustavo.FlashSale.order.enums.OrderStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_order")
public class OrderModel {
  @Id
  private UUID id;

  @Column(name = "ticket_id",  nullable = false)
  private UUID ticketId;

  @Column(name = "idempotency_key",  nullable = false)
  private String idempotencyKey;

  @CPF
  @Column(name = "cpf_holder",  nullable = false)
  private String cpfHolder;

  @Email(message = "Email invalid")
  @Column(name = "email_holder", nullable = false)
  private String emailHolder;

  @Column(name = "ticket_value",  nullable = false)
  private BigDecimal ticketValue;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatusEnum status;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private Instant updatedAt;

  public static OrderModel create(UUID ticketId, String cpfHolder, String emailHolder, BigDecimal ticketValue) {
    Instant now = Instant.now();

    return new OrderModel(
        UUID.randomUUID(),
        ticketId,
        generateIdempotencyKey(ticketId, cpfHolder, now),
        cpfHolder,
        emailHolder,
        ticketValue,
        OrderStatusEnum.PENDING,
        now,
        now
    );
  }

   private static String generateIdempotencyKey(UUID ticketId, String cpfHolder, Instant instant) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm'Z'").withZone(ZoneOffset.UTC);
    String date = formatter.format(instant);

    return ticketId.toString() + "-" + cpfHolder + "-" + date;
  }

  public void changeStatus(OrderStatusEnum status) {
    if (status == null) throw new IllegalArgumentException("Status cannot be null.");
    if (status == OrderStatusEnum.PENDING)
      throw new IllegalArgumentException("Status cannot be PENDING.");
    if (this.status != OrderStatusEnum.PENDING)
      throw new IllegalArgumentException("Status is already different from PENDING.");

    this.status = status;
  }
}
