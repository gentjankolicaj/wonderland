package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.wonderland.crypto.CSPContext.CSPContextImpl;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CSPContextTest {

  @Test
  void getProvider() {
    CSPContext context = new CSPContextImpl(CSP.BC);
    assertThat(context).isNotNull();
    assertThat(context.getProvider()).isEqualTo(CSP.BC);
    assertThat(context.components()).hasSize(0);
  }

  @Test
  void componentCast() {
    CSPContext context = new CSPContextImpl(CSP.BC,
        Map.of(Integer.class, 1, String.class, "testing", Double.class, 3.4));
    assertThat(context).isNotNull();
    assertThat(context.getProvider()).isEqualTo(CSP.BC);
    assertThat(context.components()).hasSize(3);
    assertThat(context.components()).containsEntry(Integer.class, 1);

    //test by casting each component in map with class reflection casting.
    Map<Class<?>, Object> components = context.components();
    assertThatCode(() -> components.forEach(Class::cast)).doesNotThrowAnyException();
  }
}