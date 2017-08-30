package org.shelajev.concurrencydemo;

public interface Problem {

  String[] urls = {
    "https://raw.githubusercontent.com/zeroturnaround/jf-hw-classloaders/master/plugins-remote/ChickenPlugin/ChickenPlugin.png",
    "https://raw.githubusercontent.com/zeroturnaround/jf-hw-classloaders/master/plugins-remote/NomNomNomPlugin/NomNomNomPlugin.png",
    "https://raw.githubusercontent.com/zeroturnaround/jf-hw-classloaders/master/plugins-remote/HeadAndShouldersPlugin/HeadAndShouldersPlugin.png"
  };

  byte[] fetchFirst(String... urls);

  default byte[] getFirst() {
    return fetchFirst(urls);
  }
}
