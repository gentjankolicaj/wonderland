package io.wonderland.grpc;

import io.grpc.BindableService;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.health.v1.HealthCheckResponse.ServingStatus;
import io.grpc.protobuf.services.HealthStatusManager;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
@RequiredArgsConstructor
public class GrpcServer {

  private final HealthStatusManager healthStatusManager;
  private final Server server;

  private GrpcServer(GrpcServerProperties properties, BindableService... services) {
    this.healthStatusManager = new HealthStatusManager();
    this.server = createServer(healthStatusManager, properties, services);
  }

  public static GrpcServer setupServer(GrpcServerProperties properties,
      BindableService... services) {
    GrpcServer grpcServer = new GrpcServer(properties, services);
    grpcServer.start();
    return grpcServer;
  }

  private Server createServer(HealthStatusManager healthStatusManager,
      GrpcServerProperties properties,
      BindableService... services) {
    int port = properties.getPort() == -1 ? GrpcConstants.DEFAULT_PORT : properties.getPort();
    ServerBuilder<?> serverBuilder = Grpc.newServerBuilderForPort(port,
        InsecureServerCredentials.create());

    //add health service
    serverBuilder.addService(healthStatusManager.getHealthService());
    healthStatusManager.setStatus("", ServingStatus.SERVING);

    //add external services
    if (ArrayUtils.isNotEmpty(services)) {
      Arrays.stream(services).forEach(serverBuilder::addService);
    }

    //add shutdown hooks
    shutdownHooks();
    return serverBuilder.build();
  }

  /**
   * Start gRPC server Might throw a GrpcException when started
   */
  public void start() {
    try {
      this.server.start();
    } catch (IOException e) {
      throw new GrpcException("Failed to start gRPC server instance", e);
    }
  }

  /**
   * invoke stop and await termination
   *
   * @throws InterruptedException may throw exception during shutdown
   */
  public void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown().awaitTermination(GrpcConstants.GRPC_SERVER_AWAIT_TERMINATION.left(),
          GrpcConstants.GRPC_SERVER_AWAIT_TERMINATION.right());
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  private void shutdownHooks() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      // Use stderr here since the logger may have been reset by its JVM shutdown hook.
      log.error("*** shutting down gRPC server since JVM is shutting down");
      try {
        GrpcServer.this.stop();
      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
        /* Clean up whatever needs to be handled before interrupting  */
        Thread.currentThread().interrupt();
      }
      log.error("*** gRPC server shutdown");
    }));
  }

  public boolean isActive() {
    return !server.isShutdown();
  }

}
