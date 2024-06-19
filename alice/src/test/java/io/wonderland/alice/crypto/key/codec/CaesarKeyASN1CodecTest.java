package io.wonderland.alice.crypto.key.codec;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class CaesarKeyASN1CodecTest {

  @Test
  void getInstance() {
    assertThat(CaesarKeyASN1Codec.getInstance()).isNotNull();
  }


  @Test
  void encoderAndDecoder() {
    Function<CaesarKey, byte[]> encoder = CaesarKeyASN1Codec.getInstance().encoder();
    Function<byte[], CaesarKey> decoder = CaesarKeyASN1Codec.getInstance().decoder();
    CaesarKey initial = new CaesarKey(13);
    byte[] encoded = encoder.apply(initial);
    CaesarKey decodedKey = decoder.apply(encoded);
    assertThat(decodedKey.getShift()).isEqualTo(initial.getShift());

  }

}