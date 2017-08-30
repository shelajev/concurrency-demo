package org.shelajev.concurrencydemo;

public class Misc {

  static void sleep(long n) {
    try {
      Thread.sleep(n);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
