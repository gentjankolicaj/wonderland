package io.wonderland.grpc;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.base.YamlUtils;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class GrpcClientPropertiesTest {

  @Test
  void testGrpcClientYml() throws IOException {
    GrpcClientProperties properties = YamlUtils.readUnwrappedRoot(GrpcClientProperties.class,
        "grpc_client.yml");
    assertThat(properties).isNotNull().isNotEmpty().hasSize(2);
  }

  @Test
  void testGrpcClientNegYml() throws IOException {
    GrpcClientProperties properties = YamlUtils.readUnwrappedRoot(GrpcClientProperties.class,
        "grpc_client_neg.yml");
    assertThat(properties).isNotNull().isNotEmpty().hasSize(2);
    assertThat(properties.getPort()).isEqualTo(-1);
    assertThat(properties.getHost()).isEqualTo(StringUtils.EMPTY);
  }

}