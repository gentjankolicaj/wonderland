package io.wonderland.alice.crypto.keygen;


import io.wonderland.alice.crypto.AsymmetricKeyGenerator;
import io.wonderland.alice.crypto.cipher.asymmetric.rsa.RSAUtils;
import io.wonderland.alice.crypto.cipher.key.RSAPrivateKeyImpl;
import io.wonderland.alice.crypto.cipher.key.RSAPublicKeyImpl;
import java.security.KeyPair;

public class RSAKeyGenerator implements AsymmetricKeyGenerator {


  @Override
  public KeyPair generate(long p, long q, long e) {
    return new KeyPair(new RSAPublicKeyImpl(p * q, e), new RSAPrivateKeyImpl(p, q, e));
  }

  @Override
  public KeyPair generate(long p, long q) {
    long e = RSAUtils.randPublicExponent((p - 1) * (q - 1));
    return new KeyPair(new RSAPublicKeyImpl(p * q, e), new RSAPrivateKeyImpl(p, q, e));
  }

  @Override
  public KeyPair generate() {
    throw new UnsupportedOperationException("Not implemented.");
  }
}
