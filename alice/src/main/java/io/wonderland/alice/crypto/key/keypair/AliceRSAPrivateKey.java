package io.wonderland.alice.crypto.key.keypair;


import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.cipher.asymmetric.rsa.RSAUtils;
import io.wonderland.alice.crypto.key.codec.RSAKeyPairASN1Codec;
import io.wonderland.alice.exception.KeyException;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.function.Function;


public class AliceRSAPrivateKey implements RSAPrivateCrtKey {

  private static final Function<AliceRSAPrivateKey, byte[]> PK_ENCODER = RSAKeyPairASN1Codec.getInstance()
      .privateKeyCodec().encoder();

  private final BigInteger n;
  private final BigInteger p;
  private final BigInteger q;
  private final BigInteger e;
  private final BigInteger d;
  private final BigInteger dp;
  private final BigInteger dq;
  private final BigInteger qInv;

  public AliceRSAPrivateKey(BigInteger n, BigInteger p, BigInteger q, BigInteger e, BigInteger d,
      BigInteger dp, BigInteger dq, BigInteger qInv) {
    this.n = n;
    this.p = p;
    this.q = q;
    this.e = e;
    this.d = d;
    this.dp = dp;
    this.dq = dq;
    this.qInv = qInv;
  }

  public AliceRSAPrivateKey(long p, long q) {
    this.p = BigInteger.valueOf(p);
    this.q = BigInteger.valueOf(q);
    if (this.p.gcd(this.q).intValueExact() != 1) {
      throw new KeyException("Co-primality exception, p & q are not co-prime.Must be coprime");
    }
    this.n = this.p.multiply(this.q);
    BigInteger phi = this.p.subtract(BigInteger.ONE).multiply(this.q.subtract(BigInteger.ONE));
    this.e = RSAUtils.randCoprime(phi);
    this.d = e.modInverse(phi);
    this.dp = this.d.mod(this.p.subtract(BigInteger.ONE));
    this.dq = this.d.mod(this.q.subtract(BigInteger.ONE));
    this.qInv = RSAUtils.qInv(this.p, this.q);
  }

  public AliceRSAPrivateKey(BigInteger p, BigInteger q) {
    if (p.gcd(q).intValueExact() != 1) {
      throw new KeyException("Co-primality exception, p & q are not co-prime.Must be coprime");
    }
    this.p = p;
    this.q = q;
    this.n = this.p.multiply(this.q);
    BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    this.e = RSAUtils.randCoprime(phi);
    this.d = e.modInverse(phi);
    this.dp = this.d.mod(this.p.subtract(BigInteger.ONE));
    this.dq = this.d.mod(this.q.subtract(BigInteger.ONE));
    this.qInv = RSAUtils.qInv(this.p, this.q);
  }

  public AliceRSAPrivateKey(long p, long q, long e) {
    this.p = BigInteger.valueOf(p);
    this.q = BigInteger.valueOf(q);
    this.e = BigInteger.valueOf(e);
    this.n = this.p.multiply(this.q);
    BigInteger phi = this.p.subtract(BigInteger.ONE).multiply(this.q.subtract(BigInteger.ONE));
    if (this.e.gcd(phi).intValueExact() != 1) {
      throw new KeyException("Co-primality exception, p & q are not co-prime.Must be coprime");
    }
    this.d = this.e.modInverse(phi);
    this.dp = this.d.mod(this.p.subtract(BigInteger.ONE));
    this.dq = this.d.mod(this.q.subtract(BigInteger.ONE));
    this.qInv = RSAUtils.qInv(this.p, this.q);
  }

  public AliceRSAPrivateKey(BigInteger p, BigInteger q, BigInteger e) {
    if (p.gcd(q).intValueExact() != 1) {
      throw new KeyException("Co-primality exception, p & q are not co-prime.Must be coprime");
    }
    this.p = p;
    this.q = q;
    this.e = e;
    this.n = this.p.multiply(this.q);
    BigInteger phi = this.p.subtract(BigInteger.ONE).multiply(this.q.subtract(BigInteger.ONE));
    this.d = this.e.modInverse(phi);
    this.dp = this.d.mod(this.p.subtract(BigInteger.ONE));
    this.dq = this.d.mod(this.q.subtract(BigInteger.ONE));
    this.qInv = RSAUtils.qInv(this.p, this.q);
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
  public BigInteger getPrimeP() {
    return p;
  }

  @Override
  public BigInteger getPrimeQ() {
    return q;
  }

  @Override
  public BigInteger getPrimeExponentP() {
    return dp;
  }

  @Override
  public BigInteger getPrimeExponentQ() {
    return dq;
  }

  @Override
  public BigInteger getCrtCoefficient() {
    return qInv;
  }

  @Override
  public BigInteger getPrivateExponent() {
    return d;
  }

  @Override
  public BigInteger getModulus() {
    return n;
  }

}
