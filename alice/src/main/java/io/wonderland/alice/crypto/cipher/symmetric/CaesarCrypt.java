package io.wonderland.alice.crypto.cipher.symmetric;

import static io.wonderland.alice.exception.ExceptionMessages.ARRAY_OFFSET_NOT_VALID;
import static io.wonderland.alice.exception.ExceptionMessages.PLAINTEXT_NOT_VALID;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.params.RawKeyParameter;
import io.wonderland.alice.crypto.params.RawKeyWithIVParameter;
import io.wonderland.alice.exception.CipherException;
import io.wonderland.alice.exception.DataLengthException;
import io.wonderland.base.IntUtils;
import java.nio.charset.Charset;
import java.security.Key;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Caesar cipher implementation, works only for UTF-8.
 *
 * <br>Notation:
 * <br> Key: K
 * <br> Plaintext: P
 * <br> Ciphertext: C
 * <br> Keyspace: #
 * <br> Alphabet size: m
 *
 * <br>
 * <br> Caesar cipher function:
 * <br> K ∈ {0,1,2,3...26}
 * <br> Keyspace : # = 26
 * <br> Encryption : Ci = (Pi + shift) mod(m)
 * <br> Decryption : Pi = (Ci - shift) mod(m)
 * <br>
 * <br>
 * For more read <a href="https://en.wikipedia.org/wiki/Caesar_cipher">Caesar cipher link</a>
 */
@Slf4j
public final class CaesarCrypt implements StreamCipher {

  private final int m = CharsetsUtils.getDefaultAlphabetSize();
  private int shift;
  private boolean encryption;

  public CaesarCrypt() {
    log.debug("Caesar cipher implemented for charset : {}", Charset.defaultCharset());
  }


  public int encrypt(byte[] in, int inOff, byte[] out, int outOff) throws CipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(PLAINTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ARRAY_OFFSET_NOT_VALID);
    }

    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte p = in[inOff + i];
      int c = (p + shift) % m;
      out[outOff + i] = (byte) c;
    }

    return len;
  }

  public int decrypt(byte[] in, int inOff, byte[] out, int outOff) throws CipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(PLAINTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ARRAY_OFFSET_NOT_VALID);

    }

    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte c = in[inOff + i];
      int p = (c - shift) % m;
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
          if (key instanceof CaesarKey) {
            this.shift = ((CaesarKey) key).getShift();
            this.encryption = encryption;
            return;
          } else {
            throw new IllegalArgumentException(invalidKeyTypeParamMessage());
          }
        } else if (param instanceof KeyWithIVParameter) {
          Key key = ((KeyWithIVParameter<?>) param).getKey();
          if (key instanceof CaesarKey) {
            this.shift = ((CaesarKey) key).getShift();
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
      if (key instanceof CaesarKey) {
        this.shift = ((CaesarKey) key).getShift();
        this.encryption = encryption;
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
    } else if (params instanceof KeyWithIVParameter) {
      Key key = ((KeyWithIVParameter<?>) params).getKey();
      if (key instanceof CaesarKey) {
        this.shift = ((CaesarKey) key).getShift();
        this.encryption = encryption;
        log.warn(String.format(DISREGARDED_IV, getAlgorithmName()));
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
    } else if (params instanceof RawKeyWithIVParameter) {
      RawKeyWithIVParameter keyIvParam = (RawKeyWithIVParameter) params;
      this.shift = IntUtils.parseInt(keyIvParam.getKey());
      this.encryption = encryption;
      log.warn(String.format(DISREGARDED_IV, getAlgorithmName()));
    } else if (params instanceof RawKeyParameter) {
      RawKeyParameter rawKeyParameter = (RawKeyParameter) params;
      this.shift = IntUtils.parseInt(rawKeyParameter.getKey());
      this.encryption = encryption;
    } else {
      throw new IllegalArgumentException(invalidParamMessage());
    }
  }

  @Override
  public String getAlgorithmName() {
    return Algorithms.CAESAR.getName();
  }

  @Override
  public String[] getKeyTypeNames() {
    return new String[]{CaesarKey.class.getName()};
  }


  @Override
  public byte processByte(byte in) {
    if (encryption) {
      return (byte) ((in + shift) % m);
    } else {
      return (byte) ((in - shift) % m);
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
    //No need to reset Caesar cipher
  }

}
