package io.wonderland.alice.crypto.cipher.key;

import io.wonderland.alice.crypto.cipher.AlgNames;
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
    return AlgNames.Monoalphabet;
  }

  @Override
  public String getFormat() {
    return KeyFormats.INT_MAP.name();
  }

  @Override
  public byte[] getEncoded() {
    return new byte[0];
  }
}
