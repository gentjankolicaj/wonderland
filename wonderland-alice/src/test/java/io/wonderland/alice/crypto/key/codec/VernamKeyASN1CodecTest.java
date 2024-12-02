package io.wonderland.alice.crypto.key.codec;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.key.secretkey.VernamKey;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class VernamKeyASN1CodecTest {

  @Test
  void getInstance() {
    assertThat(VernamKeyASN1Codec.getInstance()).isNotNull();
  }


  @Test
  void encoderAndDecoder() {
    Function<VernamKey, byte[]> encoder = VernamKeyASN1Codec.getInstance().encoder();
    Function<byte[], VernamKey> decoder = VernamKeyASN1Codec.getInstance().decoder();
    VernamKey initial = new VernamKey(13, new byte[]{3, 21, 126});
    byte[] encoded = encoder.apply(initial);
    VernamKey decodedKey = decoder.apply(encoded);
    assertThat(decodedKey.getModulus()).isEqualTo(initial.getModulus());
    assertThat(decodedKey.getKey()).contains(initial.getKey());
    assertThat(decodedKey.getModulus()).isEqualTo(13);
  }

}