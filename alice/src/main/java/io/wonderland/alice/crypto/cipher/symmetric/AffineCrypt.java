package io.wonderland.alice.crypto.cipher.symmetric;

import io.jmathematics.algorithm.EuclideanAlgorithm;
import io.jmathematics.algorithm.ExtendedEuclideanAlgorithm;
import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.crypto.DataLengthException;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.cipher.AlgNames;
import io.wonderland.alice.crypto.cipher.ExceptionMessages;
import io.wonderland.alice.crypto.cipher.key.AffineKey;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.params.WrappedKeyParameter;
import io.wonderland.alice.exception.CipherException;
import java.nio.charset.Charset;
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
 * <ol>
 *   <li>#b=m</li>
 *   <li>#a=euler_totient(m)</li>
 *   <li>total keyspace #k = #b * #a </li>
 *   <li>Example if alphabet is English: #b=26 & #a=tot(26)=12 => #k=#b*#a=26*12=312 total keyspace</li>
 *   </ol>
 */
@Slf4j
public class AffineCrypt implements StreamCipher {

  private final int m = CharsetsUtils.getDefaultAlphabetSize();
  private int a;
  private int b;
  private boolean encryption;

  public AffineCrypt() {
    log.warn("Affine cipher implemented for charset : {}", Charset.defaultCharset());
  }


  public int encrypt(byte[] in, int inOff, byte[] out, int outOff) throws CipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(ExceptionMessages.PLAINTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ExceptionMessages.ARRAY_OFFSET_NOT_VALID);

    }
    if (EuclideanAlgorithm.gcd(a, m) != 1) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID + ", a must be coprime to m (alphabet size)");
    }
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte p = in[inOff + i];
      int c = (p * a + b) % m;
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
    if (EuclideanAlgorithm.gcd(a, m) != 1) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID + ", a must be coprime to m (alphabet size)");
    }
    int aInverse = ExtendedEuclideanAlgorithm.mulInv(a, m);
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte c = in[inOff + i];
      int p = ((c - b) * aInverse) % m;
      out[outOff + i] = (byte) p;
    }
    return len;
  }

  @Override
  public void init(boolean encryption, CipherParameters params) throws IllegalArgumentException {
    if (params instanceof ParameterList) {
      ParameterList parameterList = (ParameterList) params;
      for (CipherParameters param : parameterList) {
        if (param instanceof WrappedKeyParameter) {
          AffineKey affineKey = ((WrappedKeyParameter<AffineKey>) param).getWrappedKey();
          this.a = affineKey.getA();
          this.b = affineKey.getB();
          this.encryption = encryption;
          return;
        }
      }
      throw new IllegalArgumentException("Invalid parameter passed to Affine init - key parameter not found.");
    } else if (params instanceof WrappedKeyParameter) {
      AffineKey affineKey = ((WrappedKeyParameter<AffineKey>) params).getWrappedKey();
      this.a = affineKey.getA();
      this.b = affineKey.getB();
      this.encryption = encryption;
    } else {
      throw new IllegalArgumentException("Invalid parameter passed to Affine init - " + params.getClass().getName());
    }
  }

  @Override
  public String getAlgorithmName() {
    return AlgNames.Affine;
  }

  @Override
  public byte processByte(byte in) {
    if (encryption) {
      return (byte) ((in * a + b) % m);
    } else {
      int aInverse = ExtendedEuclideanAlgorithm.mulInv(a, m);
      return (byte) (((in - b) * aInverse) % m);
    }
  }

  @Override
  public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) throws DataLengthException {
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
