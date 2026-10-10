package dev.cruzs.gustavo.FlashSale.order.interceptors;

import com.mercadopago.exceptions.MPInvalidWebhookSignatureException;
import com.mercadopago.webhook.WebhookSignatureValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class WebHookHeaderMercadoPagoInterceptor implements HandlerInterceptor {
  private final Logger logger = LoggerFactory.getLogger(WebHookHeaderMercadoPagoInterceptor.class);

  @Value("${payment.mercado_pago.secret_signature}")
  private String secretSignature;
  @Value("${spring.profiles.active:prod}")
  private String activeProfile;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String dataId = request.getParameter("data.id");

    if ("dev".equals(activeProfile) && dataId != null && dataId.startsWith("ORD")) {
      logger.info("Bypass in development mode in webhook header of Mercado Pago");
      return true;
    }

    try {
      WebhookSignatureValidator.validate(
          request.getHeader("x-signature"),
          request.getHeader("x-request-id"),
          dataId,
          secretSignature
      );

      logger.info("Successfully validated webhook header");
      return true;
    } catch (MPInvalidWebhookSignatureException e) {
      logger.warn(e.getMessage());
      return false;
    }
  }
}
