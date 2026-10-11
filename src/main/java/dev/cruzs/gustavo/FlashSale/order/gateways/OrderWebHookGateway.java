package dev.cruzs.gustavo.FlashSale.order.gateways;

import java.util.Map;

public interface OrderWebHookGateway {
  void orderPayment(Map<String, Object> requestBody);
}
