package org.shelajev.concurrencydemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class Misc {
  private static final Logger log = LoggerFactory.getLogger(Misc.class);

  static void sleep(long n) {
    try {
      Thread.sleep(n);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  static void stall() {
    try {
      long timeout = ThreadLocalRandom.current().nextLong(500);
      log.info("Sleeping {} ms", timeout);
      Thread.sleep(timeout);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void rethrow(Exception e) {
    if(e instanceof RuntimeException) {
      throw (RuntimeException) e;
    }
    throw new RuntimeException(e);

  }
}
