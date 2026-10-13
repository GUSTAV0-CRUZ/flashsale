package dev.cruzs.gustavo.FlashSale.notification.models;

import dev.cruzs.gustavo.FlashSale.notification.enums.NotificationStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_notification")
public class NotificationModel {
  @Id
  private UUID id;

  @NotEmpty
  @Column(nullable = false)
  private String title;

  @NotEmpty
  @Column(nullable = false)
  private String message;

  @Email
  @Column(nullable = false, name = "to_email")
  private String toEmail;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationStatusEnum status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public static NotificationModel create(String title, String message, String toEmail) {
    return new NotificationModel(
        UUID.randomUUID(),
        title,
        message,
        toEmail,
        NotificationStatusEnum.PENDING,
        Instant.now(),
        Instant.now()
    );
  }
  
  public void changeStatus(NotificationStatusEnum status) {
    if (this.status == status) throw new IllegalArgumentException("Status already updated.");
    if (status == null) throw new IllegalArgumentException("Status cannot be null.");

    this.status = status;
  }
}
