package io.wonderland.struct;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class MonocharTest {


  @Test
  void testEquals() {
    String str = "A";
    char c = str.charAt(0);
    byte b = (byte) c;
    int i = c;
    byte[] arr = str.getBytes();

    assertThat(new Monochar(b))
        .isEqualTo(new Monochar(b)).
        isEqualTo(new Monochar(c))
        .isEqualTo(new Monochar(i))
        .isEqualTo(new Monochar(arr))
        .isEqualTo(new Monochar(str));
  }

  @Test
  void testToString() {
    String str = "A";
    char c = str.charAt(0);
    byte b = (byte) c;
    int i = c;
    byte[] arr = str.getBytes();

    assertThat((new Monochar(str).toString())).isEqualTo(str);
    assertThat((new Monochar(b).toString())).isEqualTo(str);
    assertThat((new Monochar(c).toString())).isEqualTo(str);
    assertThat((new Monochar(i).toString())).isEqualTo(str);
    assertThat((new Monochar(arr).toString())).isEqualTo(str);
  }

  @Test
  void testGetValue() {
    Monochar monochar = new Monochar(new byte[]{1, 2, 3, 4, 5});
    assertThat(monochar.getValue()).hasSize(5);
  }
}