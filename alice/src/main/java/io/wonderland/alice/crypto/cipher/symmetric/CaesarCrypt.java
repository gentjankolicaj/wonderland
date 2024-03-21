package io.wonderland.alice.crypto.cipher.symmetric;

import static io.wonderland.alice.crypto.cipher.ExceptionMessages.ARRAY_OFFSET_NOT_VALID;
import static io.wonderland.alice.crypto.cipher.ExceptionMessages.PLAINTEXT_NOT_VALID;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.crypto.DataLengthException;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.cipher.AlgNames;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.params.WrappedKeyParameter;
import io.wonderland.alice.exception.CipherException;
import io.wonderland.commons.PrimitiveUtils;
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
    log.warn("Caesar cipher implemented for charset : {}", Charset.defaultCharset());
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
          this.shift = PrimitiveUtils.getInt(((KeyParameter) param).getKey());
          this.encryption = encryption;
          return;
        } else if (param instanceof WrappedKeyParameter) {
          this.shift = PrimitiveUtils.getInt(((WrappedKeyParameter<Key>) param).getWrappedKey().getEncoded());
          this.encryption = encryption;
          return;
        }
      }
      throw new IllegalArgumentException("Invalid parameter passed to Caesar init - key parameter not found.");
    } else if (params instanceof WrappedKeyParameter) {
      this.shift = PrimitiveUtils.getInt(((WrappedKeyParameter<Key>) params).getWrappedKey().getEncoded());
      this.encryption = encryption;
    } else if (params instanceof KeyWithIVParameter) {
      KeyWithIVParameter keyIvParam = (KeyWithIVParameter) params;
      this.shift = PrimitiveUtils.getInt(keyIvParam.getKey());
      this.encryption = encryption;
    } else if (params instanceof KeyParameter) {
      KeyParameter keyParameter = (KeyParameter) params;
      this.shift = PrimitiveUtils.getInt(keyParameter.getKey());
      this.encryption = encryption;
    } else {
      throw new IllegalArgumentException("Invalid parameter passed to Caesar init - " + params.getClass().getName());
    }
  }

  @Override
  public String getAlgorithmName() {
    return AlgNames.CAESAR;
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
  public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) throws DataLengthException {
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
