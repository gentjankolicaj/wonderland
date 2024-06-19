package io.wonderland.grpc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.wonderland.base.YamlUtils;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class GrpcServerTest {

  @Test
  void setupServer() throws IOException {
    GrpcApplicationProperties properties = YamlUtils.read(GrpcApplicationProperties.class,
        "application.yml");
    GrpcServer grpcServer = GrpcServer.setupServer(properties.getServer());
    assertThat(grpcServer).isNotNull();
    assertThat(grpcServer.isActive()).isTrue();
    assertThatCode(grpcServer::stop).doesNotThrowAnyException();
  }
}