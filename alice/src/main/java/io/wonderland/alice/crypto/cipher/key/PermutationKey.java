package io.wonderland.alice.crypto.cipher.key;

import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;

@ToString
@Getter
public final class PermutationKey implements SecretKey {

  private final int[] columnOrder;

  public PermutationKey(int... columnOrder) {
    if (ArrayUtils.isEmpty(columnOrder)) {
      throw new IllegalArgumentException("PermutationKey column orders must not be null.");
    }
    this.columnOrder = columnOrder;
  }


  @Override
  public String getAlgorithm() {
    return null;
  }

  @Override
  public String getFormat() {
    return null;
  }

  @Override
  public byte[] getEncoded() {
    return new byte[0];
  }
}
