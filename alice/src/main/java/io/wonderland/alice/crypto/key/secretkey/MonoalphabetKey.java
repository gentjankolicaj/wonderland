package io.wonderland.alice.crypto.key.secretkey;

import io.wonderland.alice.crypto.Algorithms;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

@ToString
@Getter
public final class MonoalphabetKey implements SecretKey {

  private final Map<Integer, Integer> key;

  public MonoalphabetKey(Map<Integer, Integer> map) {
    if (MapUtils.isEmpty(map)) {
      throw new IllegalArgumentException("Monoalphabet key map can't be null.");
    }
    this.key = map;
  }

  @SafeVarargs
  public MonoalphabetKey(Map.Entry<Integer, Integer>... entries) {
    if (ArrayUtils.isNotEmpty(entries)) {
      throw new IllegalArgumentException("Monoalphabet key map entries can't be null.");
    }
    this.key = new HashMap<>();
    for (Map.Entry<Integer, Integer> entry : entries) {
      key.putIfAbsent(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public String getAlgorithm() {
    return Algorithms.MONOALPHABET.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.MONOALPHABET.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    throw new UnsupportedOperationException(
        "Todo: to implement util methods to encode int map into byte array.");
  }
}
