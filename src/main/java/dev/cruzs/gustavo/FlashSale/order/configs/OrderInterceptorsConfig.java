package dev.cruzs.gustavo.FlashSale.order.configs;

import dev.cruzs.gustavo.FlashSale.order.interceptors.WebHookHeaderMercadoPagoInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class OrderInterceptorsConfig implements WebMvcConfigurer {
  private final WebHookHeaderMercadoPagoInterceptor webHookHeaderMercadoPagoInterceptor;

  public OrderInterceptorsConfig(WebHookHeaderMercadoPagoInterceptor webHookHeaderMercadoPagoInterceptor) {
    this.webHookHeaderMercadoPagoInterceptor = webHookHeaderMercadoPagoInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(webHookHeaderMercadoPagoInterceptor)
        .addPathPatterns("/order/webHook/payment");
  }
}
