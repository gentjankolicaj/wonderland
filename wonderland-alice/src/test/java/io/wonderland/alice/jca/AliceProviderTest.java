package io.wonderland.alice.jca;


import static org.assertj.core.api.Assertions.assertThat;

import java.security.Provider.Service;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AliceProviderTest extends ProviderTest {


  @Test
  void getInstance() {
    assertThat(AliceProvider.getInstance()).isNotNull();
    Set<Service> set = AliceProvider.getInstance().getServices();
    assertThat(set).hasSize(10);
    set.forEach(s -> System.out.println(s.getAlgorithm()));
  }

}