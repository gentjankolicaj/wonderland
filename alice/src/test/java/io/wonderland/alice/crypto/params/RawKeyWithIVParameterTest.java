package io.wonderland.alice.crypto.params;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.key.secretkey.AffineKey;
import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import java.security.Key;
import org.junit.jupiter.api.Test;

class RawKeyWithIVParameterTest {

  @Test
  void getKey() {
    Key caesarKey = new CaesarKey(13);
    Key affineKey = new AffineKey(2, 4, 5);
    RawKeyWithIVParameter caesarParam = new RawKeyWithIVParameter(caesarKey.getEncoded(),
        new byte[0]);
    RawKeyWithIVParameter affineParam = new RawKeyWithIVParameter(affineKey.getEncoded(),
        new byte[0]);

    //assertions
    assertThat(caesarParam.getKey()).isEqualTo(caesarKey.getEncoded());
    assertThat(caesarParam.getIv()).isEqualTo(new byte[0]);
    assertThat(affineParam.getKey()).isEqualTo(affineKey.getEncoded());
  }

}