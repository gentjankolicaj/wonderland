package io.wonderland.rq.client;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.wonderland.base.Client;
import io.wonderland.rq.api.Apis;
import io.wonderland.rq.api.RQClient;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class RQClientTest {

  @Test
  void api() throws Exception {
    Client client = new RQClient("localhost", 8000);
    var callable = client.api(Apis.LANG_FREQ.monogram())
        .arg(null);

    assertThatCode(callable::call).doesNotThrowAnyException();
    assertThat(callable.call()).isNotEmpty();
  }

  @Test
  void api2() throws Exception {
    Map<String, Object> props = Map.of("host", "localhost", "port", 8000);
    Client client = new RQClient(props);
    var callable = client.api(Apis.LANG_FREQ.monogram())
        .arg(null);

    assertThatCode(callable::call).doesNotThrowAnyException();
    assertThat(callable.call()).isNotEmpty();
  }
}