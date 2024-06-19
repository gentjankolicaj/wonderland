package io.wonderland.struct;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DicharTest {

  @Test
  void testToString() {
    String strA = "A";
    char cA = strA.charAt(0);
    byte bA = (byte) cA;
    int iA = cA;
    byte[] arrA = strA.getBytes();

    String strB = "B";
    char cB = strB.charAt(0);
    byte bB = (byte) cB;
    int iB = cB;
    byte[] arrB = strB.getBytes();

    assertThat((new Dichar(new Monochar(strA), new Monochar(strB))).toString()).isEqualTo(
        strA + strB);
    assertThat((new Dichar(strA, strB)).toString()).isEqualTo(strA + strB);
    assertThat((new Dichar(cA, cB)).toString()).isEqualTo(strA + strB);
    assertThat((new Dichar(bA, bB)).toString()).isEqualTo(strA + strB);
    assertThat((new Dichar(iA, iB)).toString()).isEqualTo(strA + strB);
    assertThat((new Dichar(arrA, arrB)).toString()).isEqualTo(strA + strB);
  }

  @Test
  void testToStringCharset() {
  }

  @Test
  void testEquals() {
    String strA = "A";
    char cA = strA.charAt(0);
    byte bA = (byte) cA;
    int iA = cA;
    byte[] arrA = strA.getBytes();

    String strB = "B";
    char cB = strB.charAt(0);
    byte bB = (byte) cB;
    int iB = cB;
    byte[] arrB = strB.getBytes();

    assertThat(new Dichar(new Monochar(strA), new Monochar(strB)))
        .isEqualTo(new Dichar(new Monochar(strA), new Monochar(strB)))
        .isEqualTo(new Dichar(strA, strB))
        .isEqualTo(new Dichar(cA, cB))
        .isEqualTo(new Dichar(bA, bB))
        .isEqualTo(new Dichar(iA, iB))
        .isEqualTo(new Dichar(arrA, arrB));

  }

  @Test
  void getValue() {
    Dichar dichar = new Dichar(new byte[]{1, 2, 3}, new byte[]{0, 9, 8, 7, 6, 5, 4, 2, 3, 1});
    assertThat(dichar.getValue()[0]).hasSize(3);
    assertThat(dichar.getValue()[1]).hasSize(10);
  }
}