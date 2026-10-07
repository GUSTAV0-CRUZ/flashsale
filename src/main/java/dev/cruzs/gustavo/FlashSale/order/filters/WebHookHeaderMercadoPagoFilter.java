package dev.cruzs.gustavo.FlashSale.order.filters;

import com.mercadopago.exceptions.MPInvalidWebhookSignatureException;
import com.mercadopago.webhook.WebhookSignatureValidator;
import dev.cruzs.gustavo.FlashSale.order.exceptions.MercadoPagoWebhookSignatureException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@WebFilter("/order/webHook/payment")
public class WebHookHeaderMercadoPagoFilter implements Filter {
  @Value("${payment.mercado_pago.secret_signature}")
  private String secretSignature;

  @Override
  public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    try {
      WebhookSignatureValidator.validate(
          httpRequest.getHeader("x-signature"),
          httpRequest.getHeader("x-request-id"),
          httpRequest.getParameter("data.id"),
          secretSignature);

      chain.doFilter(httpRequest, response);
    } catch (MPInvalidWebhookSignatureException e) {
      throw new MercadoPagoWebhookSignatureException();
    }
  }
}
