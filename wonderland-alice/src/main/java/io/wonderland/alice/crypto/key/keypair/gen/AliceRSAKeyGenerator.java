package io.wonderland.alice.crypto.key.keypair.gen;


import io.wonderland.alice.crypto.AsymmetricKeyGenerator;
import io.wonderland.alice.crypto.asymmetric.rsa.RSAUtils;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPrivateKey;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPublicKey;
import java.security.KeyPair;

public class AliceRSAKeyGenerator implements AsymmetricKeyGenerator {


  public KeyPair generate(long p, long q, long e) {
    return new KeyPair(new AliceRSAPublicKey(p * q, e), new AliceRSAPrivateKey(p, q, e));
  }

  public KeyPair generate(long p, long q) {
    long e = RSAUtils.randomCoprime((p - 1) * (q - 1));
    return new KeyPair(new AliceRSAPublicKey(p * q, e), new AliceRSAPrivateKey(p, q, e));
  }

  @Override
  public KeyPair generate() {
    throw new UnsupportedOperationException("Not implemented.");
  }

}
