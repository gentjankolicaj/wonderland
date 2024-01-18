package io.wonderland.alice.crypto;

import io.wonderland.common.PrimitiveUtils;
import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;

/**
 * Tabula recta implementation for UTF-8;
 * <br> For more check : <a href="https://en.wikipedia.org/wiki/Tabula_recta">Tabula recta</a>
 */
@Slf4j
public abstract class TabulaRecta {

  private static final int UTF_8_CHARSET_BASE = 1_112_064;

  public static int getCharacterDec(byte[] array) {
    return ByteBuffer.wrap(array).getInt();
  }

  public static int getCrossCharacterDec(int row, int column) {
    return (row + column) % UTF_8_CHARSET_BASE;
  }

  public static int getCrossCharacterDec(int row, int column, int shift, int charsetBase) {
    return (row + column + shift) % charsetBase;
  }

  /**
   * Prints tabula recta keyContent on console without row & column coordinates.
   *
   * @param leftShift  shifting
   * @param lowerBound UTF-8 encoding lower bound
   * @param upperBound UTF-8 encoding upper bound
   */
  public static void printTable(int leftShift, int lowerBound, int upperBound) {
    System.out.println("\\");
    int charsetBase = UTF_8_CHARSET_BASE;
    for (int i = lowerBound; i < upperBound; i++) {
      for (int j = lowerBound; j < upperBound; j++) {
        System.out.print(
            " " + (new String(PrimitiveUtils.getBytes(getCrossCharacterDec(i, j, leftShift, charsetBase)))) + " |");
      }
      System.out.println();
    }
  }


}
