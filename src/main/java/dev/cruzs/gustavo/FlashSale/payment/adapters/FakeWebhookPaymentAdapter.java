package dev.cruzs.gustavo.FlashSale.payment.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Profile("test")
@Component
public class FakeWebhookPaymentAdapter {
  private final Logger logger = LoggerFactory.getLogger(FakeWebhookPaymentAdapter.class);
  private final RestClient restClient;

  public FakeWebhookPaymentAdapter() {
    this.restClient = RestClient.builder().baseUrl("http://api:8080").build();
  }

  private String randomAction() {
    List<String> actions = List.of(
        "order.processed",
        "order.failed",
        "order.canceled",
        "order.action_required"
    );
    int indexRandom = ThreadLocalRandom.current().nextInt(actions.size());

    return actions.get(indexRandom);
  }

  @Async
  public void sendToWebhook(String externalReference) {
    Map<String, Object> requestBody = Map.of(
        "data", Map.of("external_reference", externalReference),
//        "action", randomAction()
        "action", "order.failed"
    );

    ResponseEntity<Void> response = restClient.post()
        .uri(uriBuilder -> uriBuilder
            .path("/order/webHook/payment")
            .queryParam("data.id", "ORD1234")
            .build()
        )
        .contentType(MediaType.APPLICATION_JSON)
        .body(requestBody)
        .exchange((clientRequest, clientResponse) -> ResponseEntity
            .status(clientResponse.getStatusCode())
            .headers(clientResponse.getHeaders())
            .body(clientResponse.getStatusCode().is2xxSuccessful() ? clientResponse.bodyTo(Void.class) : null));

    logger.info(
        "ProcessCreditCard with response status={}",
        response.getStatusCode()
    );
  }
}
