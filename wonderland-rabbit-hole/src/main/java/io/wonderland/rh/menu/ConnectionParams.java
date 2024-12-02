package io.wonderland.rh.menu;

import java.util.LinkedHashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class ConnectionParams extends LinkedHashMap<String, Object> {

  private static final String CLIENT_TYPE_KEY = "clientType";
  private static final String CONNECTION_NAME_KEY = "connectionName";
  private static final String HOST_KEY = "host";
  private static final String PORT_KEY = "port";
  private static final String USERNAME_KEY = "username";
  private static final String PASSWORD_KEY = "password";
  private static final int DEFAULT_PORT = 9000;

  public ConnectionParams(String clientType, String connectionName, String host, String port,
      String username,
      String password) {
    put(CLIENT_TYPE_KEY, clientType);
    put(CONNECTION_NAME_KEY, connectionName);
    put(HOST_KEY, host);
    put(PORT_KEY, parsePort(port));
    put(USERNAME_KEY, username);
    put(PASSWORD_KEY, password);
  }

  static int parsePort(String port) {
    try {
      return Integer.parseInt(port);
    } catch (NumberFormatException e) {
      log.error("", e);
    }
    return DEFAULT_PORT;
  }

  public String getClientType() {
    Object value = get(CLIENT_TYPE_KEY);
    if (value != null) {
      return (String) value;
    }
    return "";
  }

  public String getConnectionName() {
    Object value = get(CONNECTION_NAME_KEY);
    if (value != null) {
      return (String) value;
    }
    return "";
  }

  public String getHost() {
    Object value = get(HOST_KEY);
    if (value != null) {
      return (String) value;
    }
    return "";
  }

  public int getPort() {
    Object value = get(PORT_KEY);
    if (value != null) {
      return (int) value;
    }
    return DEFAULT_PORT;
  }

  public String getUsername() {
    Object value = get(USERNAME_KEY);
    if (value != null) {
      return (String) value;
    }
    return "";
  }

  public String getPassword() {
    Object value = get(PASSWORD_KEY);
    if (value != null) {
      return (String) value;
    }
    return "";
  }

}
