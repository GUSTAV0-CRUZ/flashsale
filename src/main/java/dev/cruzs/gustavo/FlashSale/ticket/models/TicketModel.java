package dev.cruzs.gustavo.FlashSale.ticket.models;

import dev.cruzs.gustavo.FlashSale.ticket.enums.TicketStatus;
import dev.cruzs.gustavo.FlashSale.ticket.valueObjects.QrCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "tb_ticket")
public class TicketModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "qr_code")
  private QrCode qrCode;

  @Column(name = "cpf_holder")
  private String cpfHolder;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Instant date;

  @Column(nullable = false)
  private String location;

  @Column(nullable = false)
  private String sector;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TicketStatus status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public void sell(String cpfHolder ) {
    if (status != TicketStatus.AVAILABLE) throw new IllegalArgumentException("Status must not be AVAILABLE");

    this.cpfHolder = cpfHolder;
    status = TicketStatus.PENDING;
  }

  public void confirmSale(String qrCode) {
    if (status != TicketStatus.PENDING) throw new IllegalArgumentException("Status must not be PENDING");

    this.qrCode = new QrCode(qrCode);
    status = TicketStatus.SOLD;
  }

  public void refund() {
    if (status != TicketStatus.SOLD) throw new IllegalArgumentException("Status must not be SOLD");

    Instant now = Instant.now();

    if (now.compareTo(date) >= 0)
      throw new IllegalArgumentException("Event date has already passed, or is today.");

    if (now.isAfter(updatedAt.plus(Duration.ofDays(7))))
      throw new IllegalArgumentException("Deadline 7 day expired");

    if (
        now.isAfter(date.minus(Duration.ofDays(7))) &&
        now.isAfter(updatedAt.plus(Duration.ofHours(48)))
    )
      throw new IllegalArgumentException("Deadline 48 hours expired");

    cpfHolder = null;
    qrCode = null;
    status = TicketStatus.AVAILABLE;
  }
}
