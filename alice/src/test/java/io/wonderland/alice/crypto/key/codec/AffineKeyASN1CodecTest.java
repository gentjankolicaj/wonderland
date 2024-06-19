package io.wonderland.alice.crypto.key.codec;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.key.secretkey.AffineKey;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class AffineKeyASN1CodecTest {

  @Test
  void getInstance() {
    assertThat(AffineKeyASN1Codec.getInstance()).isNotNull();
  }


  @Test
  void encoderAndDecoder() {
    Function<AffineKey, byte[]> encoder = AffineKeyASN1Codec.getInstance().encoder();
    Function<byte[], AffineKey> decoder = AffineKeyASN1Codec.getInstance().decoder();
    AffineKey initial = new AffineKey(13, 3, 21);
    byte[] encoded = encoder.apply(initial);
    AffineKey decodedKey = decoder.apply(encoded);
    assertThat(decodedKey.getA()).isEqualTo(initial.getA());
    assertThat(decodedKey.getB()).isEqualTo(initial.getB());
    assertThat(decodedKey.getM()).isEqualTo(initial.getM());
  }

}