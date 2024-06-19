package io.wonderland.grpc;

import io.atlassian.fugue.Pair;
import java.util.concurrent.TimeUnit;

public final class GrpcConstants {

  public static final String DEFAULT_HOST = "localhost";
  public static final int DEFAULT_PORT = 9000;
  public static final Pair<Integer, TimeUnit> GRPC_SERVER_AWAIT_TERMINATION = new Pair<>(30,
      TimeUnit.SECONDS);
  static final String PORT_FIELD = "port";
  static final String HOST_FIELD = "host";

  private GrpcConstants() {
  }
}