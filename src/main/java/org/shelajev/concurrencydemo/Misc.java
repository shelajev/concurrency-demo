package org.shelajev.concurrencydemo;

import java.util.concurrent.ThreadLocalRandom;

public class Misc {

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
      Thread.sleep(ThreadLocalRandom.current().nextLong(500));
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
