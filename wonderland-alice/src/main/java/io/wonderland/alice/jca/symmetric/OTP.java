package io.wonderland.alice.jca.symmetric;

import io.wonderland.alice.crypto.symmetric.OTPCrypt;

public final class OTP extends StreamCipherSpi {

  public OTP() {
    super(new OTPCrypt());
  }

}
