package io.wonderland.rq.cryptanalysis;

import io.wonderland.struct.Char;
import io.wonderland.struct.Monochar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IndexCoincidence {

  private IndexCoincidence() {
  }

  public static double calcIC(Byte[] ciphertext, int encodingBytes) {
    Map<Monochar, Integer> freq = FrequencyAnalysis.monocharFreq(ciphertext, encodingBytes);
    double characterNumber = freq.values().stream().mapToDouble(e -> e).sum();
    return freq.values().stream().mapToDouble(e -> e)
        .map(e -> (e / characterNumber) * ((e - 1) / (characterNumber - 1))).sum();
  }


  public static double calcAverageIC(List<Byte>[] matrix, int encodingBytes) {
    double ic = 0.0;
    for (List<Byte> list : matrix) {
      ic = ic + calcIC(list.toArray(Byte[]::new), encodingBytes);
    }
    return ic / matrix.length;
  }

  public static double calcCharIC(Map<? extends Char, Integer> freq) {
    double characterNumber = freq.values().stream().mapToDouble(e -> e).sum();
    return freq.values().stream().mapToDouble(e -> e)
        .map(e -> (e / characterNumber) * ((e - 1) / (characterNumber - 1))).sum();
  }

  public static Map<Integer, Double> calcKeyPeriodIC(byte[] ciphertext, int encodingBytes,
      int minKeyLength,
      int maxKeyLength) {
    if (maxKeyLength > (ciphertext.length / encodingBytes)) {
      throw new IllegalArgumentException(
          "Max key length search can't be bigger than ciphertext/encodingBytes");
    }
    Map<Integer, Double> icMap = new HashMap<>();
    for (int i = minKeyLength; i <= maxKeyLength; i++) {
      List<Byte>[] ciphertextMat = createCiphertextMat(ciphertext, encodingBytes, i);
      icMap.put(i, calcAverageIC(ciphertextMat, encodingBytes));
    }
    return icMap;
  }

  public static List<Byte>[] createCiphertextMat(byte[] ciphertext, int encodingBytes,
      int keyLength) {
    List<Byte>[] mat = new ArrayList[keyLength];
    for (int i = 0; i < mat.length; i++) {
      mat[i] = new ArrayList<>();
    }
    int rowIndex = 0;
    int startIndex = 0;
    boolean unfinished = true;
    while (unfinished) {
      List<Byte> list = mat[rowIndex % keyLength];
      int iterations = getIterations(ciphertext.length, startIndex, encodingBytes);
      for (int j = startIndex; j < startIndex + iterations; j++) {
        list.add(ciphertext[j]);
      }
      rowIndex++;
      startIndex = startIndex + iterations;
      unfinished = startIndex < ciphertext.length;
    }
    return mat;
  }


  static int getIterations(int ciphertextLength, int startIndex, int encodingBytes) {
    return ((ciphertextLength - startIndex) / encodingBytes) > 0 ? encodingBytes
        : (ciphertextLength - startIndex) % encodingBytes;
  }


  static byte[][] createMat(int ciphertextLength, int keyLength) {
    if (keyLength > ciphertextLength) {
      throw new IllegalArgumentException("Key length can't be bigger than ciphertext length");
    }
    if ((ciphertextLength / keyLength) == 0) {
      return new byte[keyLength][ciphertextLength / keyLength];
    } else {
      int r = ciphertextLength % keyLength;
      byte[][] matrix = new byte[keyLength][];
      for (int i = 0; i < r; i++) {
        matrix[i] = new byte[ciphertextLength / keyLength + 1];
      }
      for (int i = r; i < keyLength; i++) {
        matrix[i] = new byte[ciphertextLength / keyLength];
      }
      return matrix;
    }
  }

}
