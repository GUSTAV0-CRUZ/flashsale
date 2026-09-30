package dev.cruzs.gustavo.FlashSale.payment.services;

import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessCreditCartCommand;
import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessPixCommand;
import dev.cruzs.gustavo.FlashSale.payment.dtos.ProcessPaymentDto;
import dev.cruzs.gustavo.FlashSale.payment.enums.PaymentTypeEnum;
import dev.cruzs.gustavo.FlashSale.payment.gateways.PaymentGateway;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
  private final PaymentGateway paymentGateway;

  public PaymentService(PaymentGateway paymentGateway) {
    this.paymentGateway = paymentGateway;
  }

  public void processPayment(ProcessPaymentDto processPaymentDto) {
    if (processPaymentDto.paymentType() == null) throw new IllegalArgumentException("PaymentType cannot be null");
    if (processPaymentDto.paymentType() == PaymentTypeEnum.PIX) {
      paymentGateway.processPix(
          new PaymentProcessPixCommand(
            processPaymentDto.externalReference(),
            processPaymentDto.amount(),
            processPaymentDto.IdempotencyKey(),
            processPaymentDto.email()
          )
      );

      return;
    }

    paymentGateway.processCreditCard(
        new PaymentProcessCreditCartCommand(
          processPaymentDto.externalReference(),
          processPaymentDto.amount(),
          processPaymentDto.IdempotencyKey(),
          processPaymentDto.email(),
          processPaymentDto.token(),
          processPaymentDto.paymentMethodId(),
          processPaymentDto.installments()
        )
    );
  }
}
