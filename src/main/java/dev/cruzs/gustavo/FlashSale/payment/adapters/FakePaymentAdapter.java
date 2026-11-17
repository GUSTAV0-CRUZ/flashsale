package dev.cruzs.gustavo.FlashSale.payment.adapters;

import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessCreditCartCommand;
import dev.cruzs.gustavo.FlashSale.payment.commands.PaymentProcessPixCommand;
import dev.cruzs.gustavo.FlashSale.payment.dtos.QrCodeDto;
import dev.cruzs.gustavo.FlashSale.payment.gateways.PaymentGateway;
import dev.cruzs.gustavo.FlashSale.utils.LoadingTimeUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Primary
@Profile("test")
@Component
public class FakePaymentAdapter implements PaymentGateway {

  @Lazy
  @Override
  public void processCreditCard(PaymentProcessCreditCartCommand paymentProcessCreditCartCommand) {
    LoadingTimeUtil.load();
}

  @Lazy
  @Override
  public QrCodeDto processPix(PaymentProcessPixCommand paymentprocessPixCommand) {
    LoadingTimeUtil.load();
    return new QrCodeDto("QRCodeCopyAndPasteString", "UVJDb2RlQ29weUFuZFBhc3RlU3RyaW5n");
  }
}
