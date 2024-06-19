package io.wonderland.alice.crypto.key.codec;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.key.secretkey.RailfenceKey;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class RailfenceKeyASN1CodecTest {

  @Test
  void getInstance() {
    assertThat(RailfenceKeyASN1Codec.getInstance()).isNotNull();
  }


  @Test
  void encoderAndDecoder() {
    Function<RailfenceKey, byte[]> encoder = RailfenceKeyASN1Codec.getInstance().encoder();
    Function<byte[], RailfenceKey> decoder = RailfenceKeyASN1Codec.getInstance().decoder();
    RailfenceKey initial = new RailfenceKey(122);
    byte[] encoded = encoder.apply(initial);
    RailfenceKey decodedKey = decoder.apply(encoded);
    assertThat(decodedKey.getRails()).isEqualTo(initial.getRails());
  }

}