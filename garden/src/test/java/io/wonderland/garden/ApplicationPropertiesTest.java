package io.wonderland.garden;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.base.ConfigurationException;
import io.wonderland.base.YamlConfigurations;
import org.junit.jupiter.api.Test;

class ApplicationPropertiesTest {


  @Test
  void testApplicationYml() throws ConfigurationException {
    ApplicationProperties applicationProperties = YamlConfigurations.load(
        ApplicationProperties.class,
        "/application.yml");
    assertThat(applicationProperties).isNotNull();
    assertThat(applicationProperties.getGrpcServer()).hasSize(1);
    assertThat(applicationProperties.getGrpcServer().getPort()).isEqualTo(9001);
  }

}