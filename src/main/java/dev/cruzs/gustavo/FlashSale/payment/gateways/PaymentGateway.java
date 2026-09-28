package dev.cruzs.gustavo.FlashSale.payment.gateways;

import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessCreditCartCommand;
import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessPixCommand;

public interface PaymentGateway {
  void processCreditCard(PaymentProcessCreditCartCommand paymentProcessCreditCartCommand);
  void processPix(PaymentProcessPixCommand paymentprocessPixCommand);
}
