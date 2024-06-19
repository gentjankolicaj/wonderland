package io.wonderland.rh;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class ApplicationPropertiesTest {

  @Test
  void fromClasspath() throws IOException {
    ApplicationProperties properties = ApplicationProperties.fromClasspath();
    assertThat(properties).isNotNull();
    assertThat(properties.getGrpcClient()).isNotEmpty();
    assertThat(properties.getGrpcClient().getPort()).isEqualTo(9000);
  }
}