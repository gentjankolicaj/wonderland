package io.wonderland.rq.cryptanalysis;

import io.wonderland.struct.GramType;
import io.wonderland.struct.Monochar;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;

public final class FrequencyAnalysis {

  public static final String CONTENT_CAN_T_BE_EMPTY = "Content can't be empty.";
  public static final String NOT_IMPLEMENTED_FOR = "Character byte encoding not implemented for ";

  private FrequencyAnalysis() {
  }

  /**
   * @param text          plaintext | ciphertext
   * @param encodingBytes number of bytes encoding a character  {1,2,3,4}
   * @return map of monochar frequencies
   */
  public static Map<Monochar, Integer> monocharFreq(Byte[] text, int encodingBytes) {
    if (ArrayUtils.isEmpty(text)) {
      throw new IllegalArgumentException(CONTENT_CAN_T_BE_EMPTY);
    }

    if (encodingBytes != 1 && encodingBytes != 2 && encodingBytes != 3 && encodingBytes != 4) {
      throw new IllegalArgumentException(NOT_IMPLEMENTED_FOR + encodingBytes);
    }
    Map<Monochar, Integer> freq = new HashMap<>();
    if (encodingBytes == 1) {
      for (byte b : text) {
        Monochar monoCharKey = new Monochar(b);
        freq.compute(monoCharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 2) {
      for (int i = 0, len = text.length; i + 2 <= len; i = i + 2) {
        Monochar monoCharKey = new Monochar(text[i], text[i + 1]);
        freq.compute(monoCharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 3) {
      for (int i = 0, len = text.length; i + 3 <= len; i = i + 3) {
        Monochar monoCharKey = new Monochar(text[i], text[i + 1], text[i + 2]);
        freq.compute(monoCharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else {
      for (int i = 0, len = text.length; i + 4 <= len; i = i + 4) {
        Monochar monoCharKey = new Monochar(text[i], text[i + 1], text[i + 2], text[i + 3]);
        freq.compute(monoCharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    }
    return freq;
  }


  public static Map<String, Double> calcFreq(byte[] bytes, GramType gramType) {
    if (ArrayUtils.isEmpty(bytes)) {
      throw new IllegalArgumentException("Bytes array is empty");
    }
    Map<String, Double> freq = new HashMap<>();
    if (GramType.MONOGRAM.equals(gramType)) {
      for (byte b : bytes) {
        freq.compute(new String(new byte[]{b}), (k, v) -> v == null ? 0 : v + 1);
      }
    } else if (GramType.DIGRAM.equals(gramType)) {
      for (byte a : bytes) {
        for (byte b : bytes) {
          freq.compute(new String(new byte[]{a, b}), (k, v) -> v == null ? 0 : v + 1);
        }
      }
      //O(n^3)
    } else if (GramType.TRIGRAM.equals(gramType)) {
      for (byte a : bytes) {
        for (byte b : bytes) {
          for (byte c : bytes) {
            freq.compute(new String(new byte[]{a, b, c}),
                (key, value) -> value == null ? 0 : value + 1);
          }
        }
      }
    }
    return freq;
  }

  public static Map<String, Double> calcFreqPct(byte[] bytes, GramType gramType) {
    Map<String, Double> freq = calcFreq(bytes, gramType);
    double sum = freq.values().stream().mapToDouble(v -> v).sum();
    Map<String, Double> pct = new HashMap<>();
    freq.forEach((key, v) -> {
      if (v != null && v != 0) {
        pct.put(key, v / sum);
      } else {
        pct.put(key, v);
      }
    });
    return pct;
  }


  public static Map<String, Double> calcPct(Map<String, Double> gramFreqMap) {
    double sum = gramFreqMap.values().stream().mapToDouble(v -> v).sum();
    Map<String, Double> pct = new HashMap<>();
    gramFreqMap.forEach((key, v) -> {
      if (v != null && v != 0) {
        pct.put(key, v / sum);
      } else {
        pct.put(key, v);
      }
    });
    return pct;
  }

}
