package dev.cruzs.gustavo.FlashSale.payment.services;

import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessCreditCartCommand;
import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessPixCommand;
import dev.cruzs.gustavo.FlashSale.payment.dtos.ProcessPaymentDto;
import dev.cruzs.gustavo.FlashSale.payment.dtos.QrCodeDto;
import dev.cruzs.gustavo.FlashSale.payment.enums.PaymentTypeEnum;
import dev.cruzs.gustavo.FlashSale.payment.gateways.PaymentGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
  private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
  private final PaymentGateway paymentGateway;

  public PaymentService(PaymentGateway paymentGateway) {
    this.paymentGateway = paymentGateway;
  }

  public QrCodeDto processPayment(ProcessPaymentDto processPaymentDto) {
    if (processPaymentDto.paymentType() == null) throw new IllegalArgumentException("PaymentType cannot be null");

    logger.info(
        "Processing Payment of type={} with externalReference={} , value={} and email={}",
        processPaymentDto.paymentType().getValue(),
        processPaymentDto.externalReference(),
        processPaymentDto.amount(),
        processPaymentDto.email()
    );

    if (processPaymentDto.paymentType() == PaymentTypeEnum.PIX) {
      return paymentGateway.processPix(
          new PaymentProcessPixCommand(
            processPaymentDto.externalReference(),
            processPaymentDto.amount(),
            processPaymentDto.IdempotencyKey(),
            processPaymentDto.email()
          )
      );
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

    return null;
  }
}
