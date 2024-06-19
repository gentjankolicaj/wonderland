package io.wonderland.rq.api;

import io.wonderland.base.Client;
import java.util.Map;


public class RQClient extends Client {

  public RQClient(String host, int port) {
    super(host, port);
  }

  public RQClient(Map<String, Object> props) {
    super(props);
  }

  @Override
  public <T> T ping() {
    //todo: to implement ping based on protocol
    return null;
  }

}
