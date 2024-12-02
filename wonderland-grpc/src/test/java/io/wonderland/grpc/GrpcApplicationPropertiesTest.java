package io.wonderland.grpc;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.base.YamlUtils;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class GrpcApplicationPropertiesTest {

  @Test
  void testGrpcAppYml() throws IOException {
    GrpcApplicationProperties properties = YamlUtils.read(GrpcApplicationProperties.class,
        "application.yml");
    assertThat(properties).isNotNull();
    assertThat(properties.getClient()).isNotEmpty().hasSize(2);
    assertThat(properties.getServer()).isNotEmpty().hasSize(1);
  }

  @Test
  void testGrpcAppNegYml() throws IOException {
    GrpcApplicationProperties properties = YamlUtils.read(GrpcApplicationProperties.class,
        "application_neg.yml");
    assertThat(properties).isNotNull();
    assertThat(properties.getClient()).isNotEmpty().hasSize(2);
    assertThat(properties.getServer()).isNotEmpty().hasSize(1);
    assertThat(properties.getClient().getPort()).isEqualTo(-1);
    assertThat(properties.getClient().getHost()).isEqualTo(StringUtils.EMPTY);
    assertThat(properties.getServer().getPort()).isEqualTo(-1);
  }

}