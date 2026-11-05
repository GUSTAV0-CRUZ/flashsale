package dev.cruzs.gustavo.FlashSale.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class LoadingTimeUtil {
  private static final Logger logger = LoggerFactory.getLogger(LoadingTimeUtil.class);

  public static void load() {
    long timeMin = 1000;
    long timeMax = 4000;
    long timeLoad = ThreadLocalRandom.current().nextLong(timeMin, timeMax + 1);
    try {
      logger.info("Process load with timeLoad={}", timeLoad);

      Thread.sleep(timeLoad);

      logger.info("Time processed");
    } catch (InterruptedException e) {
      logger.warn("Time interrupted");
      Thread.currentThread().interrupt();
    }
  }
}
