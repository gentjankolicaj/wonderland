package io.wonderland.base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class Client {

  private final Map<String, Object> props = new LinkedHashMap<>();

  protected Client(String host, int port) {
    this.props.put("host", host);
    this.props.put("port", port);
  }

  protected Client(Map<String, Object> props) {
    this.props.putAll(props);
  }

  public <T, R> FunctionArg<T, R> api(Function<T, R> func) {
    return t -> () -> func.apply(t);
  }

  public abstract <T> T ping();

}
