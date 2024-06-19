package io.wonderland.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;

class YamlConfigurationsTest {

  @Test
  void load() throws ConfigurationException {
    URL url = YamlConfigurationsTest.class.getClassLoader().getResource("application.yml");
    assert url != null;
    TestProps testProps = YamlConfigurations.load(TestProps.class, url);
    assertThat(testProps.getName()).isEqualTo("test-prop");
    assertThat(testProps.getClient()).isNotNull();
    assertThat(testProps.getClient().getDb()).isZero();
    assertThat(testProps.getClient().getHost()).isEqualTo("127.0.0.1");
  }

  @Test
  void load2() throws ConfigurationException {
    TestProps testProps = YamlConfigurations.load(TestProps.class, "/application.yml");
    assertThat(testProps.getName()).isEqualTo("test-prop");
    assertThat(testProps.getClient()).isNotNull();
    assertThat(testProps.getClient().getDb()).isZero();
    assertThat(testProps.getClient().getPassword()).isEqualTo("P4ssword");
    assertThat(testProps.getClient().getHost()).isEqualTo("127.0.0.1");
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class TestProps {

    private String name;
    private TestClientProps client;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class TestClientProps {

    private String host;
    private int port;
    private String username;
    private String password;
    private int db;
  }
}