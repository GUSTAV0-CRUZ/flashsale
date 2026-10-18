package dev.cruzs.gustavo.FlashSale.notification.models;

import dev.cruzs.gustavo.FlashSale.notification.enums.NotificationStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {
  private NotificationModel notificationModel;

  @BeforeEach
  void setUp() {
    String title = "Title Test";
    String message = "Message Test";
    String toEmail = "test@gmail.com";

    notificationModel = NotificationModel.create(title, message, toEmail);
  }

  @Test
  void create() {
    assertNotNull(notificationModel);
    assertNotNull(notificationModel.getId());
    assertNotNull(notificationModel.getCreatedAt());
    assertNotNull(notificationModel.getUpdatedAt());
    assertEquals("Title Test", notificationModel.getTitle());
    assertEquals("Message Test", notificationModel.getMessage());
    assertEquals("test@gmail.com",  notificationModel.getToEmail());
    assertEquals(NotificationStatusEnum.PENDING, notificationModel.getStatus());
  }

  @DisplayName("changeStatusSuccess - Should changed order per status=CONFIRMED")
  @Test
  void changeStatusSuccess() {
    notificationModel.changeStatus(NotificationStatusEnum.SENT);

    assertEquals(NotificationStatusEnum.SENT, notificationModel.getStatus());
  }

  @DisplayName("changeStatusErrorCase1 - Should return error: Status cannot be null.")
  @Test
  void changeStatusErrorCase1() {
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> notificationModel.changeStatus(null)
    );

    assertEquals("Status cannot be null.", result.getMessage());
  }

  @DisplayName("changeStatusErrorCase2 - Should return error: Status cannot be PENDING.")
  @Test
  void changeStatusErrorCase2() {
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> notificationModel.changeStatus(NotificationStatusEnum.PENDING)
    );

    assertEquals("Status cannot be PENDING.", result.getMessage());
  }

  @DisplayName("changeStatusErrorCase3 - Should return error: Status is already different from PENDING.")
  @Test
  void changeStatusErrorCase3() {
    notificationModel.setStatus(NotificationStatusEnum.SENT);
    IllegalArgumentException result = assertThrows(
        IllegalArgumentException.class,
        () -> notificationModel.changeStatus(NotificationStatusEnum.FAILED)
    );

    assertEquals("Status is already different from PENDING.", result.getMessage());
  }
}