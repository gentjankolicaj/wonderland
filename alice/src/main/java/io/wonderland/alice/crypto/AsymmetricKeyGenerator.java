package io.wonderland.alice.crypto;


import java.security.KeyPair;

public interface AsymmetricKeyGenerator {


  KeyPair generate(long p, long q, long e);

  KeyPair generate(long p, long q);

  KeyPair generate();

}
