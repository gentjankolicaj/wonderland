package io.wonderland.alice.crypto.key.codec;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.key.secretkey.OTPKey;
import io.wonderland.base.IntUtils;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class OTPKeyASN1CodecTest {

  @Test
  void getInstance() {
    assertThat(OTPKeyASN1Codec.getInstance()).isNotNull();
  }


  @Test
  void encoderAndDecoder() {
    Function<OTPKey, byte[]> encoder = OTPKeyASN1Codec.getInstance().encoder();
    Function<byte[], OTPKey> decoder = OTPKeyASN1Codec.getInstance().decoder();
    OTPKey initial = new OTPKey(13, 3, 21);
    byte[] encoded = encoder.apply(initial);
    OTPKey decodedKey = decoder.apply(encoded);
    assertThat(decodedKey.getModulus()).isEqualTo(initial.getModulus());
    assertThat(decodedKey.getCodeKeys()).contains(IntUtils.arrayWrapped(initial.getCodeKeys()));
  }

}