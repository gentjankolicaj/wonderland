package io.wonderland.alice.jca.cipher;

import io.wonderland.alice.crypto.cipher.symmetric.OTPCrypt;

public final class OTP extends StreamCipherSpi {

  public OTP() {
    super(new OTPCrypt());
  }

}
