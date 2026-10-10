package dev.cruzs.gustavo.FlashSale.payment.gateways;

import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessCreditCartCommand;
import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessPixCommand;
import dev.cruzs.gustavo.FlashSale.payment.dtos.QrCodeDto;

public interface PaymentGateway {
  void processCreditCard(PaymentProcessCreditCartCommand paymentProcessCreditCartCommand);
  QrCodeDto processPix(PaymentProcessPixCommand paymentprocessPixCommand);
}
