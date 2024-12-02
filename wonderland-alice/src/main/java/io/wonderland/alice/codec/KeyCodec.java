package io.wonderland.alice.codec;

import java.util.function.Function;

public interface KeyCodec<K> extends Codec<Function<K, byte[]>, Function<byte[], K>> {

  default String getName() {
    return this.getClass().getSimpleName();
  }

  Function<K, byte[]> encoder();

  Function<byte[], K> decoder();
}
