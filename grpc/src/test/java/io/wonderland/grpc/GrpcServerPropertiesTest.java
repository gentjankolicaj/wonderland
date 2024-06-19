package io.wonderland.grpc;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.base.YamlUtils;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class GrpcServerPropertiesTest {

  @Test
  void testGrpcServerYml() throws IOException {
    GrpcServerProperties properties = YamlUtils.readUnwrappedRoot(GrpcServerProperties.class,
        "grpc_server.yml");
    assertThat(properties).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  void testGrpcServerNegYml() throws IOException {
    GrpcServerProperties properties = YamlUtils.readUnwrappedRoot(GrpcServerProperties.class,
        "grpc_server_neg.yml");
    assertThat(properties).isNotNull().isNotEmpty().hasSize(1);
    assertThat(properties.getPort()).isEqualTo(-1);
  }


}