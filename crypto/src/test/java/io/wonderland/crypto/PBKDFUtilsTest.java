package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import org.bouncycastle.crypto.util.PBKDF2Config;
import org.junit.jupiter.api.Test;

class PBKDFUtilsTest {

  @Test
  void createPbkdfConfigWithScrypt() {
    //block size => 8*8 bit=>64 bit
    assertThat(PBKDFUtils.createPbkdfConfigWithScrypt(1024, 8, 1, 10))
        .isNotNull();
  }

  @Test
  void createPbkdf2Config() {
    assertThat(PBKDFUtils.createPbkdf2Config(PBKDF2Config.PRF_SHA256, 2, 10)).isNotNull();
  }

}