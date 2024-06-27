package io.wonderland.rh.monitor;


public interface BaseMBean {

  String domain = "io.wonderland.rh";


  default String createName(String type, String name) {
    return domain + ":" + type + "=" + name;
  }


}
