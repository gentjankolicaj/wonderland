package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.crypto.CSPContext.CSPContextImpl;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ContextTest {

  @Test
  void components() {
    Context context = new CSPContextImpl(CSP.BC, Map.of(Integer.class, 1));
    assertThat(context).isNotNull();
    assertThat(context.components()).hasSize(1);
    assertThat(context.components()).containsEntry(Integer.class, 1);
  }
}