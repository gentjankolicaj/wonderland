package io.wonderland.alice.jca;


import static org.assertj.core.api.Assertions.assertThat;

import java.security.Provider.Service;
import java.security.Security;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AliceProviderTest {

  static {
    Security.addProvider(new AliceProvider());
  }

  @Test
  void getInstance() {
    assertThat(AliceProvider.getInstance()).isNotNull();
    Set<Service> set = AliceProvider.getInstance().getServices();
    set.forEach(s -> System.out.println(s.getAlgorithm()));
  }


}