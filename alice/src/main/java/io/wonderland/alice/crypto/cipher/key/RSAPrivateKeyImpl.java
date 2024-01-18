package io.wonderland.alice.crypto.cipher.key;

import io.jmathematics.algorithm.EuclideanAlgorithm;
import io.jmathematics.algorithm.ExtendedEuclideanAlgorithm;
import io.wonderland.alice.crypto.cipher.AlgNames;
import io.wonderland.alice.crypto.cipher.asymmetric.rsa.RSAUtils;
import io.wonderland.alice.exception.KeyException;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;
import lombok.Getter;

@Getter
public class RSAPrivateKeyImpl implements RSAPrivateCrtKey {

  private final BigInteger n;
  private final BigInteger p;
  private final BigInteger q;
  private final BigInteger e;
  private final BigInteger d;
  private final BigInteger pe;
  private final BigInteger qe;


  //todo: to replace all long type with biginteger,including method invocations
  public RSAPrivateKeyImpl(BigInteger p, BigInteger q) {
    this.p = p;
    this.q = q;
    this.n = this.p.multiply(this.q);
    long phi = (p.longValue() - 1) * (q.longValue() - 1);
    this.e = BigInteger.valueOf(RSAUtils.randPublicExponent(phi));
    this.d = BigInteger.valueOf(ExtendedEuclideanAlgorithm.mulInv(e.longValue(), phi));
    this.pe = this.p.multiply(this.e);
    this.qe = this.q.multiply(this.e);
  }


  //todo: to replace all long type with biginteger,including method invocations
  public RSAPrivateKeyImpl(long p, long q) {
    if (EuclideanAlgorithm.gcd(p, q) != 1) {
      throw new KeyException("Co-primality exception,q & q are not co-prime.");
    }
    this.p = BigInteger.valueOf(p);
    this.q = BigInteger.valueOf(q);
    this.n = this.p.multiply(this.q);
    this.e = BigInteger.valueOf(RSAUtils.randPublicExponent((p - 1) * (q - 1)));
    this.d = BigInteger.valueOf(ExtendedEuclideanAlgorithm.mulInv(e.longValue(), (p - 1) * (q - 1)));
    this.pe = this.p.multiply(this.e);
    this.qe = this.q.multiply(this.e);
  }

  //todo: to replace all long type with biginteger,including method invocations
  public RSAPrivateKeyImpl(long p, long q, long e) {
    if (EuclideanAlgorithm.gcd(e, (p - 1) * (q - 1)) != 1) {
      throw new KeyException("Co-primality exception, e is not co-prime to (p-1)*(q-1)");
    }
    this.p = BigInteger.valueOf(p);
    this.q = BigInteger.valueOf(q);
    this.n = this.p.multiply(this.q);
    this.e = BigInteger.valueOf(e);
    this.d = BigInteger.valueOf(ExtendedEuclideanAlgorithm.mulInv(e, (p - 1) * (q - 1)));
    this.pe = this.p.multiply(this.e);
    this.qe = this.q.multiply(this.e);
  }

  @Override
  public String getAlgorithm() {
    return AlgNames.RSA;
  }

  @Override
  public String getFormat() {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public byte[] getEncoded() {
    throw new UnsupportedOperationException("Not implemented.");
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
    return pe;
  }

  @Override
  public BigInteger getPrimeExponentQ() {
    return qe;
  }

  @Override
  public BigInteger getCrtCoefficient() {
    return null;
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
