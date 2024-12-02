package io.wonderland.struct;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TricharTest {

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

    String strC = "C";
    char cC = strC.charAt(0);
    byte bC = (byte) cC;
    int iC = cC;
    byte[] arrC = strC.getBytes();

    assertThat((new Trichar(new Monochar(strA), new Monochar(strB),
        new Monochar(strC))).toString()).isEqualTo(
        strA + strB + strC);
    assertThat((new Trichar(strA, strB, strC)).toString()).isEqualTo(strA + strB + strC);
    assertThat((new Trichar(cA, cB, cC)).toString()).isEqualTo(strA + strB + strC);
    assertThat((new Trichar(bA, bB, bC)).toString()).isEqualTo(strA + strB + strC);
    assertThat((new Trichar(iA, iB, iC)).toString()).isEqualTo(strA + strB + strC);
    assertThat((new Trichar(arrA, arrB, arrC)).toString()).isEqualTo(strA + strB + strC);
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

    String strC = "C";
    char cC = strC.charAt(0);
    byte bC = (byte) cC;
    int iC = cC;
    byte[] arrC = strC.getBytes();

    assertThat(new Trichar(new Monochar(strA), new Monochar(strB), new Monochar(strC)))
        .isEqualTo(new Trichar(new Monochar(strA), new Monochar(strB), new Monochar(strC)))
        .isEqualTo(new Trichar(strA, strB, strC))
        .isEqualTo(new Trichar(cA, cB, cC))
        .isEqualTo(new Trichar(bA, bB, bC))
        .isEqualTo(new Trichar(iA, iB, iC))
        .isEqualTo(new Trichar(arrA, arrB, arrC));
  }

  @Test
  void getValue() {
    Trichar trichar = new Trichar(new byte[]{1, 2, 3}, new byte[]{0, 9, 8, 7, 6, 5, 4, 2, 3, 1},
        new byte[]{});
    assertThat(trichar.getValue()[0]).hasSize(3);
    assertThat(trichar.getValue()[1]).hasSize(10);
    assertThat(trichar.getValue()[2]).hasSize(0);
  }
}