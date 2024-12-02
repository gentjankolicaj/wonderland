package io.wonderland.struct;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class QuadcharTest {

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

    String strD = "D";
    char cD = strD.charAt(0);
    byte bD = (byte) cD;
    int iD = cD;
    byte[] arrD = strD.getBytes();

    assertThat((new Quadchar(new Monochar(strA), new Monochar(strB), new Monochar(strC),
        new Monochar(strD))).toString()).isEqualTo(strA + strB + strC + strD);
    assertThat((new Quadchar(strA, strB, strC, strD)).toString()).isEqualTo(
        strA + strB + strC + strD);
    assertThat((new Quadchar(cA, cB, cC, cD)).toString()).isEqualTo(strA + strB + strC + strD);
    assertThat((new Quadchar(bA, bB, bC, bD)).toString()).isEqualTo(strA + strB + strC + strD);
    assertThat((new Quadchar(iA, iB, iC, iD)).toString()).isEqualTo(strA + strB + strC + strD);
    assertThat((new Quadchar(arrA, arrB, arrC, arrD)).toString()).isEqualTo(
        strA + strB + strC + strD);
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

    String strD = "D";
    char cD = strD.charAt(0);
    byte bD = (byte) cD;
    int iD = cD;
    byte[] arrD = strD.getBytes();

    assertThat(new Quadchar(new Monochar(strA), new Monochar(strB), new Monochar(strC),
        new Monochar(strD)))
        .isEqualTo(new Quadchar(new Monochar(strA), new Monochar(strB), new Monochar(strC),
            new Monochar(strD)))
        .isEqualTo(new Quadchar(strA, strB, strC, strD))
        .isEqualTo(new Quadchar(cA, cB, cC, cD))
        .isEqualTo(new Quadchar(bA, bB, bC, bD))
        .isEqualTo(new Quadchar(iA, iB, iC, iD))
        .isEqualTo(new Quadchar(arrA, arrB, arrC, arrD));
  }

  @Test
  void getValue() {
    Quadchar trichar = new Quadchar(new byte[]{1, 2, 3}, new byte[]{0, 9, 8, 7, 6, 5, 4, 2, 3, 1},
        new byte[]{},
        new byte[]{1});
    assertThat(trichar.getValue()[0]).hasSize(3);
    assertThat(trichar.getValue()[1]).hasSize(10);
    assertThat(trichar.getValue()[2]).hasSize(0);
    assertThat(trichar.getValue()[3]).hasSize(1);
  }
}