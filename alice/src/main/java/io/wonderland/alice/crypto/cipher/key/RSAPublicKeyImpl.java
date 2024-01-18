package io.wonderland.alice.crypto.cipher.key;


import io.wonderland.alice.crypto.cipher.AlgNames;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RSAPublicKeyImpl implements RSAPublicKey {

  private final BigInteger n;
  private final BigInteger e;

  public RSAPublicKeyImpl(long n, long e) {
    this.n = BigInteger.valueOf(n);
    this.e = BigInteger.valueOf(e);
  }


  @Override
  public String getAlgorithm() {
    return AlgNames.RSA;
  }

  @Override
  public byte[] getEncoded() {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public String getFormat() {
    throw new UnsupportedOperationException("Not implemented.");
  }


  @Override
  public BigInteger getPublicExponent() {
    return e;
  }

  @Override
  public BigInteger getModulus() {
    return n;
  }
}
