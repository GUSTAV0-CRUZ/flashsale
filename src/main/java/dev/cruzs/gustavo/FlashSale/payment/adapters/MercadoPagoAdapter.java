package dev.cruzs.gustavo.FlashSale.payment.adapters;

import com.mercadopago.client.order.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.order.*;
import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessCreditCartCommand;
import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessPixCommand;
import dev.cruzs.gustavo.FlashSale.payment.dtos.QrCodeDto;
import dev.cruzs.gustavo.FlashSale.payment.exceptions.MercadoPagoException;
import dev.cruzs.gustavo.FlashSale.payment.gateways.PaymentGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MercadoPagoAdapter implements PaymentGateway {
  private final Logger logger = LoggerFactory.getLogger(MercadoPagoAdapter.class);

  @Value("${payment.mercado_pago.access_token}")
  private String accessToken;

  private Order requestPayment(
      OrderPaymentRequest payment,
      String amount,
      String externalReference,
      String IdempotencyKey,
      String email
  ) {
    OrderClient client = new OrderClient();
    List<OrderPaymentRequest> payments = new ArrayList<>();
    payments.add(payment);

    OrderCreateRequest request = OrderCreateRequest.builder()
        .type("online")
        .totalAmount(amount)
        .externalReference(externalReference)
        .payer(OrderPayerRequest.builder().email(email).build())
        .transactions(OrderTransactionRequest.builder()
            .payments(payments)
            .build())
        .processingMode("automatic")
        .build();

    Map<String, String> headers =  new HashMap<>();
    headers.put("X-Idempotency-Key", IdempotencyKey);

    MPRequestOptions requestOptions = MPRequestOptions.builder()
        .accessToken(accessToken)
        .customHeaders(headers)
        .build();

    try {
      Order order = client.create(request, requestOptions);
      logger.info("Order of Mercado Pago created with id: {}", order.getId());
      return order;
    } catch (MPException | MPApiException e) {
      throw new MercadoPagoException(e.getMessage(), e);
    }
  }

  @Override
  public void processCreditCard(PaymentProcessCreditCartCommand command) {
     OrderPaymentRequest payment = OrderPaymentRequest.builder()
        .amount(command.amount().toPlainString())
        .paymentMethod(OrderPaymentMethodRequest.builder()
            .id(command.paymentMethodId())
            .type("credit_card")
            .token(command.token())
            .installments(command.installments())
            .build())
        .build();

    requestPayment(
        payment,
        command.amount().toPlainString(),
        command.externalReference(),
        command.idempotencyKey(),
        command.email()
    );
  }

  @Override
  public QrCodeDto processPix(PaymentProcessPixCommand command) {
    OrderPaymentRequest payment = OrderPaymentRequest.builder()
        .amount(command.amount().toPlainString())
        .paymentMethod(OrderPaymentMethodRequest.builder()
            .id("pix")
            .type("bank_transfer")
            .build())
        .expirationTime("PT15M")
        .build();

    Order order = requestPayment(
        payment,
        command.amount().toPlainString(),
        command.externalReference(),
        command.idempotencyKey(),
        command.email()
    );

    OrderTransaction orderTransaction = order.getTransactions();
    List<OrderPayment> orderPayments = orderTransaction.getPayments();
    OrderPayment fistOrderPayment = orderPayments.getFirst();
    OrderPaymentMethod orderPaymentMethod = fistOrderPayment.getPaymentMethod();

    return new QrCodeDto(
        orderPaymentMethod.getQrCode(),
        orderPaymentMethod.getQrCodeBase64()
    );
  }
}
