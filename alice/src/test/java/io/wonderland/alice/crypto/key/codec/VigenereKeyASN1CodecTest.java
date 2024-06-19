package io.wonderland.alice.crypto.key.codec;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.key.secretkey.VigenereKey;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class VigenereKeyASN1CodecTest {

  @Test
  void getInstance() {
    assertThat(VigenereKeyASN1Codec.getInstance()).isNotNull();
  }


  @Test
  void encoderAndDecoder() {
    Function<VigenereKey, byte[]> encoder = VigenereKeyASN1Codec.getInstance().encoder();
    Function<byte[], VigenereKey> decoder = VigenereKeyASN1Codec.getInstance().decoder();
    VigenereKey initial = new VigenereKey(13, new byte[]{3, 21, 126});
    byte[] encoded = encoder.apply(initial);
    VigenereKey decodedKey = decoder.apply(encoded);
    assertThat(decodedKey.getModulus()).isEqualTo(initial.getModulus());
    assertThat(decodedKey.getKey()).contains(initial.getKey());
    assertThat(decodedKey.getModulus()).isEqualTo(13);

  }

}