package io.wonderland.alice.crypto.cipher.symmetric;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.AffineKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.exception.CipherException;
import io.wonderland.alice.exception.DataLengthException;
import io.wonderland.alice.exception.ExceptionMessages;
import java.math.BigInteger;
import java.security.Key;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Affine cipher implementation.
 *
 * <br> Notation:
 * <br> Key : K
 * <br> Ciphertext : C
 * <br> Plaintext : P
 * <br> Keyspace : #
 * <br> Alphabet size: m
 *
 * <br>
 * <br> Affine cipher  :
 * <br> Key : K = (a & b) where a must be relatively prime with m => gcd(a,m)=1.
 * <br> Encryption : Ci= Pi * a + b mod(m)
 * <br> Decryption : Pi=(Ci - b) * a^-1 mod(m)
 *
 * <pre>
 * <ol>
 *   <li>#b=m</li>
 *   <li>#a=euler totient(m)</li>
 *   <li>total keyspace #k = #b * #a </li>
 *   <li>Example if alphabet is English: #b=26 & #a=tot(26)=12 => #k=#b*#a=26*12=312 total keyspace</li>
 *   </ol>
 *   </pre>
 */
@Slf4j
public class AffineCrypt implements StreamCipher {

  private BigInteger m;
  private BigInteger a;
  private BigInteger b;
  private boolean encryption;

  public AffineCrypt() {
  }


  public int encrypt(byte[] in, int inOff, byte[] out, int outOff) throws CipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(ExceptionMessages.PLAINTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ExceptionMessages.ARRAY_OFFSET_NOT_VALID);

    }
    if (a.gcd(m).intValue() != 1) {
      throw new IllegalArgumentException(
          ExceptionMessages.KEY_NOT_VALID + ", a must be coprime to m (alphabet size)");
    }
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte p = in[inOff + i];
      int c = a.multiply(BigInteger.valueOf(p)).add(b).mod(m).intValue();
      out[outOff + i] = (byte) c;
    }
    return len;
  }


  public int decrypt(byte[] in, int inOff, byte[] out, int outOff) throws CipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(ExceptionMessages.CIPHERTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ExceptionMessages.ARRAY_OFFSET_NOT_VALID);

    }
    if (a.gcd(m).intValue() != 1) {
      throw new IllegalArgumentException(
          ExceptionMessages.KEY_NOT_VALID + ", a must be coprime to m (alphabet size)");
    }
    BigInteger aInverse = a.modInverse(m);
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte c = in[inOff + i];
      int p = BigInteger.valueOf(c).subtract(b).multiply(aInverse).mod(m).intValue();
      out[outOff + i] = (byte) p;
    }
    return len;
  }

  @Override
  public void init(boolean encryption, CipherParameters params) throws IllegalArgumentException {
    if (params instanceof ParameterList) {
      ParameterList parameterList = (ParameterList) params;
      for (CipherParameters param : parameterList) {
        if (param instanceof KeyParameter) {
          Key key = ((KeyParameter<?>) param).getKey();
          if (key instanceof AffineKey) {
            AffineKey affineKey = (AffineKey) key;
            this.a = affineKey.getA();
            this.b = affineKey.getB();
            this.m = affineKey.getM();
            this.encryption = encryption;
            return;
          } else {
            throw new IllegalArgumentException(invalidKeyTypeParamMessage());
          }
        } else if (param instanceof KeyWithIVParameter) {
          Key key = ((KeyWithIVParameter<?>) param).getKey();
          if (key instanceof AffineKey) {
            AffineKey affineKey = (AffineKey) key;
            this.a = affineKey.getA();
            this.b = affineKey.getB();
            this.m = affineKey.getM();
            this.encryption = encryption;
            log.warn(String.format(DISREGARDED_IV, getAlgorithmName()));
            return;
          } else {
            throw new IllegalArgumentException(invalidKeyTypeParamMessage());
          }
        } else {
          throw new IllegalArgumentException(invalidParamMessage());
        }
      }

    } else if (params instanceof KeyParameter) {
      Key key = ((KeyParameter<?>) params).getKey();
      if (key instanceof AffineKey) {
        AffineKey affineKey = (AffineKey) key;
        this.a = affineKey.getA();
        this.b = affineKey.getB();
        this.m = affineKey.getM();
        this.encryption = encryption;
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
    } else if (params instanceof KeyWithIVParameter) {
      Key key = ((KeyWithIVParameter<?>) params).getKey();
      if (key instanceof AffineKey) {
        AffineKey affineKey = (AffineKey) key;
        this.a = affineKey.getA();
        this.b = affineKey.getB();
        this.m = affineKey.getM();
        this.encryption = encryption;
        log.warn(String.format(DISREGARDED_IV, getAlgorithmName()));
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
    } else {
      throw new IllegalArgumentException(invalidParamMessage());
    }
  }

  @Override
  public String getAlgorithmName() {
    return Algorithms.AFFINE.getName();
  }

  @Override
  public String[] getKeyTypeNames() {
    return new String[]{AffineKey.class.getName()};
  }

  @Override
  public byte processByte(byte in) {
    if (encryption) {
      return (byte) BigInteger.valueOf(in).multiply(a).add(b).mod(m).intValue();
    } else {
      BigInteger aInverse = a.modInverse(m);
      return (byte) BigInteger.valueOf(in).subtract(b).multiply(aInverse).mod(m).intValue();
    }
  }

  @Override
  public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff)
      throws DataLengthException {
    if (encryption) {
      return encrypt(in, inOff, out, outOff);
    } else {
      return decrypt(in, inOff, out, outOff);
    }
  }

  @Override
  public void reset() {
    //nothing to be done to reset Affine
  }

}
