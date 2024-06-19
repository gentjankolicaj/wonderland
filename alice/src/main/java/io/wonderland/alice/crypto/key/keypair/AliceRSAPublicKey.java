package io.wonderland.alice.crypto.key.keypair;


import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.RSAKeyPairASN1Codec;
import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;
import java.util.function.Function;
import lombok.Getter;

@Getter
public class AliceRSAPublicKey implements RSAPublicKey {

  private static final Function<AliceRSAPublicKey, byte[]> PK_ENCODER = RSAKeyPairASN1Codec.getInstance()
      .publicKeyCodec().encoder();
  private final BigInteger e;
  private final BigInteger n;

  public AliceRSAPublicKey(long e, long n) {
    this.e = BigInteger.valueOf(e);
    this.n = BigInteger.valueOf(n);
  }

  public AliceRSAPublicKey(BigInteger e, BigInteger n) {
    this.e = e;
    this.n = n;
  }

  @Override
  public String getAlgorithm() {
    return Algorithms.RSA.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.RSA.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    return PK_ENCODER.apply(this);
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
