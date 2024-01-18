package io.wonderland.rq.cryptanalysis;

import io.wonderland.rq.type.Dichar;
import io.wonderland.rq.type.Monochar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;

public class FrequencyAnalysis {

  public static final String CONTENT_CAN_T_BE_EMPTY = "Content can't be empty.";
  public static final String NOT_IMPLEMENTED_FOR = "Character byte encoding not implemented for ";
  private FrequencyAnalysis() {
  }

  /**
   * @param text          plaintext | ciphertext
   * @param encodingBytes number of bytes encoding a character  {1,2,3,4}
   * @return map of monochar frequencies
   */
  public static Map<Monochar, Integer> monocharFreq(byte[] text, int encodingBytes) {
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
    } else if (encodingBytes == 4) {
      for (int i = 0, len = text.length; i + 4 <= len; i = i + 4) {
        Monochar monoCharKey = new Monochar(text[i], text[i + 1], text[i + 2], text[i + 3]);
        freq.compute(monoCharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    }
    return freq;
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
    } else if (encodingBytes == 4) {
      for (int i = 0, len = text.length; i + 4 <= len; i = i + 4) {
        Monochar monoCharKey = new Monochar(text[i], text[i + 1], text[i + 2], text[i + 3]);
        freq.compute(monoCharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    }
    return freq;
  }

  /**
   * @param text          plaintext | ciphertext
   * @param encodingBytes number of bytes encoding a character  {1,2,3,4}
   * @return map of monochar frequencies percentage
   */
  public static Map<Monochar, Double> monocharFreqPct(byte[] text, int encodingBytes) {
    if (ArrayUtils.isEmpty(text)) {
      throw new IllegalArgumentException(CONTENT_CAN_T_BE_EMPTY);
    }

    if (encodingBytes != 1 && encodingBytes != 2 && encodingBytes != 3 && encodingBytes != 4) {
      throw new IllegalArgumentException(NOT_IMPLEMENTED_FOR + encodingBytes);
    }
    int textLength = text.length;
    Map<Monochar, Integer> freq = new HashMap<>();
    if (encodingBytes == 1) {
      for (byte b : text) {
        Monochar monoCharKey = new Monochar(b);
        freq.compute(monoCharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 2) {
      for (int i = 0; i + 2 <= textLength; i = i + 2) {
        Monochar monoCharKey = new Monochar(text[i], text[i + 1]);
        freq.compute(monoCharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 3) {
      for (int i = 0; i + 3 <= textLength; i = i + 3) {
        Monochar monoCharKey = new Monochar(text[i], text[i + 1], text[i + 2]);
        freq.compute(monoCharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 4) {
      for (int i = 0; i + 4 <= textLength; i = i + 4) {
        Monochar monoCharKey = new Monochar(text[i], text[i + 1], text[i + 2], text[i + 3]);
        freq.compute(monoCharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    }
    return freq.entrySet().stream().map(e -> Map.entry(e.getKey(), (double) e.getValue() / textLength))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }


  /**
   * @param text          plaintext | ciphertext
   * @param encodingBytes number of bytes encoding a character  {1,2,3,4}
   * @return
   */
  public static Map<Dichar, Integer> dicharFreq(byte[] text, int encodingBytes) {
    if (ArrayUtils.isEmpty(text)) {
      throw new IllegalArgumentException(CONTENT_CAN_T_BE_EMPTY);
    }

    if (encodingBytes != 1 && encodingBytes != 2 && encodingBytes != 3 && encodingBytes != 4) {
      throw new IllegalArgumentException(NOT_IMPLEMENTED_FOR + encodingBytes);
    }
    Map<Dichar, Integer> freq = new HashMap<>();
    if (encodingBytes == 1) {
      for (int i = 0, len = text.length; i + 1 < len; i++) {
        Dichar dicharKey = new Dichar(text[i], text[i + 1]);
        freq.compute(dicharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 2) {
      for (int i = 0, len = text.length; i + 3 < len; i++) {
        Dichar dicharKey = new Dichar(new byte[]{text[i], text[i + 1]},
            new byte[]{text[i + 2], text[i + 3]});
        freq.compute(dicharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 3) {
      for (int i = 0, len = text.length; i + 5 < len; i++) {
        Dichar dicharKey = new Dichar(new byte[]{text[i], text[i + 1], text[i + 2]},
            new byte[]{text[i + 3], text[i + 4], text[i + 5]});
        freq.compute(dicharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 4) {
      for (int i = 0, len = text.length; i + 7 < len; i++) {
        Dichar dicharKey = new Dichar(new byte[]{text[i], text[i + 1], text[i + 2], text[i + 3]},
            new byte[]{text[i + 4], text[i + 5], text[i + 6], text[i + 7]});
        freq.compute(dicharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    }
    return freq;
  }

  /**
   * @param text          plaintext | ciphertext
   * @param encodingBytes number of bytes encoding a character  {1,2,3,4}
   * @return
   */
  public static Map<Dichar, Double> dicharFreqPct(byte[] text, int encodingBytes) {
    if (ArrayUtils.isEmpty(text)) {
      throw new IllegalArgumentException(CONTENT_CAN_T_BE_EMPTY);
    }

    if (encodingBytes != 1 && encodingBytes != 2 && encodingBytes != 3 && encodingBytes != 4) {
      throw new IllegalArgumentException(NOT_IMPLEMENTED_FOR + encodingBytes);
    }
    int textLength = text.length;
    Map<Dichar, Integer> freq = new HashMap<>();
    if (encodingBytes == 1) {
      for (int i = 0; i + 1 < textLength; i++) {
        Dichar dicharKey = new Dichar(text[i], text[i + 1]);
        freq.compute(dicharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 2) {
      for (int i = 0; i + 3 < textLength; i++) {
        Dichar dicharKey = new Dichar(new byte[]{text[i], text[i + 1]},
            new byte[]{text[i + 2], text[i + 3]});
        freq.compute(dicharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 3) {
      for (int i = 0; i + 5 < textLength; i++) {
        Dichar dicharKey = new Dichar(new byte[]{text[i], text[i + 1], text[i + 2]},
            new byte[]{text[i + 3], text[i + 4], text[i + 5]});
        freq.compute(dicharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    } else if (encodingBytes == 4) {
      for (int i = 0; i + 7 < textLength; i++) {
        Dichar dicharKey = new Dichar(new byte[]{text[i], text[i + 1], text[i + 2], text[i + 3]},
            new byte[]{text[i + 4], text[i + 5], text[i + 6], text[i + 7]});
        freq.compute(dicharKey, (key, value) -> value == null ? 1 : value + 1);
      }
    }
    return freq.entrySet().stream().map(e -> Map.entry(e.getKey(), (double) e.getValue() / textLength))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

  }


}
