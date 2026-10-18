package dev.cruzs.gustavo.FlashSale.order.adapters;

import dev.cruzs.gustavo.FlashSale.order.gateways.OrderWebHookGateway;
import dev.cruzs.gustavo.FlashSale.order.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class OrderWebHookGatewayAdapter implements OrderWebHookGateway {
  private final Logger logger = LoggerFactory.getLogger(OrderWebHookGatewayAdapter.class);
  private final OrderService orderService;

  public OrderWebHookGatewayAdapter(OrderService orderService) {
    this.orderService = orderService;
  }

  public void orderPayment(Map<String, Object> mercadoPagoOrderResponseDto) {
    Map<String, Object> data = (Map<String, Object>) mercadoPagoOrderResponseDto.get("data");
    UUID idOrder = UUID.fromString((String) data.get("external_reference"));
    String action = (String) mercadoPagoOrderResponseDto.get("action");

    processOrder(action, idOrder);
  }

  private void processOrder(String action, UUID idOrder) {
     Map<String, Consumer<UUID>> actionMap = Map.of(
        "order.processed", orderService::confirmSale,
        "order.failed", orderService::cancelled,
        "order.canceled", orderService::cancelled,
        "order.action_required", orderService::actionRequired
     );

     Consumer<UUID> command = actionMap.get(action);

     if (command == null) {
       logger.warn("Order with id={} and action={} not collected.", idOrder, action);
       return;
     }

     command.accept(idOrder);
  }
}
