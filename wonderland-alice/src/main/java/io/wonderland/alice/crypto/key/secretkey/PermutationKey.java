package io.wonderland.alice.crypto.key.secretkey;

import io.wonderland.alice.crypto.Algorithms;
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
    return Algorithms.PERMUTATION.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.PERMUTATION.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    throw new UnsupportedOperationException(
        "Todo: to implement util methods to encode int[] into byte array.");
  }
}
