package dev.cruzs.gustavo.FlashSale.notification.adapters;

import dev.cruzs.gustavo.FlashSale.notification.exceptions.NotificationFailException;
import dev.cruzs.gustavo.FlashSale.notification.gateways.NotificationGateway;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationGatewayAdapter implements NotificationGateway {
  private final Logger logger = LoggerFactory.getLogger(NotificationGatewayAdapter.class);

  @Value("${notification.mailtrap.token}")
  private String TOKEN;

  @Value("${notification.mailtrap.sender_email}")
  private String SENDER_EMAIL;

  @Override
  public void sendToEmail(String title, String message, String email) throws NotificationFailException {
    MailtrapConfig config = new MailtrapConfig.Builder()
        .sandbox(true)
        .inboxId(4960815L)
        .token(TOKEN)
        .build();
    MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);
    MailtrapMail mail = MailtrapMail.builder()
        .from(new Address(SENDER_EMAIL))
        .to(List.of(new Address(email)))
        .subject(title)
        .text(message)
        .build();

    try {
      client.send(mail);

      logger.info("Mailtrap send to email={}, title={}", email, title);
    } catch (RuntimeException e) {
      throw new NotificationFailException(e.getMessage());
    }
  }
}
