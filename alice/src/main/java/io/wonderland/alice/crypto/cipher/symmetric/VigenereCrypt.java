package io.wonderland.alice.crypto.cipher.symmetric;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.crypto.DataLengthException;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.cipher.AlgNames;
import io.wonderland.alice.crypto.cipher.ExceptionMessages;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.params.WrappedKeyParameter;
import io.wonderland.alice.exception.CipherException;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;


/**
 * Vigenere cipher implementation, works only for UTF-8.
 *
 * <br>Notation:
 * <br> Key: K
 * <br> Plaintext: P
 * <br> Ciphertext: C
 * <br> Keyspace: #
 * <br> Alphabet size: m
 * <br> Key size: l
 *
 * <br>
 * <br> Vigenere cipher function:
 * <br> Keyspace : # = m * m * m * ...m multiplied |P| times.
 * <br> Encryption : Ci = (Pi + K(i mod (l)) mod(m)
 * <br> Decryption : Pi = (Ci - K(i mod (l)) mod(m)
 * <br>
 * <br>
 * For more check <a href="https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher">Vigenere cipher link</a>
 */
@Slf4j
public final class VigenereCrypt implements StreamCipher {

  private final int m = CharsetsUtils.getDefaultAlphabetSize();
  private byte[] key;
  private boolean encryption;

  public VigenereCrypt() {
    log.warn("Vigenere cipher implemented for charset : {}", Charset.defaultCharset());
  }


  public int encrypt(byte[] in, int inOff, byte[] out, int outOff) throws CipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(ExceptionMessages.PLAINTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ExceptionMessages.ARRAY_OFFSET_NOT_VALID);

    }
    if (ArrayUtils.isEmpty(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    int l = key.length;
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte p = in[inOff + i];
      int c = (p + key[(inOff + i) % l]) % m;
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
    if (ArrayUtils.isEmpty(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    int l = key.length;
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte c = in[inOff + i];
      int p = (c - key[(inOff + i) % l]) % m;
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
          this.key = ((KeyParameter) param).getKey();
          this.encryption = encryption;
          return;
        }
        if (param instanceof WrappedKeyParameter) {
          this.key = ((WrappedKeyParameter) param).getWrappedKey().getEncoded();
          this.encryption = encryption;
          return;
        }
      }
      throw new IllegalArgumentException("Invalid parameter passed to Vigenere init - key parameter not found.");
    } else if (params instanceof WrappedKeyParameter) {
      this.key = ((WrappedKeyParameter) params).getWrappedKey().getEncoded();
      this.encryption = encryption;

    } else if (params instanceof KeyWithIVParameter) {
      KeyWithIVParameter keyIvParam = (KeyWithIVParameter) params;
      this.key = keyIvParam.getKey();
      this.encryption = encryption;
    } else if (params instanceof KeyParameter) {
      KeyParameter keyParameter = (KeyParameter) params;
      this.key = keyParameter.getKey();
      this.encryption = encryption;
    } else {
      throw new IllegalArgumentException("Invalid parameter passed to Vigenere init - " + params.getClass().getName());
    }
  }

  @Override
  public String getAlgorithmName() {
    return AlgNames.Vigenere;
  }

  @Override
  public byte processByte(byte in) {
    if (ArrayUtils.isEmpty(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    if (encryption) {
      return (byte) ((in + key[0]) % m);
    } else {
      return (byte) ((in - key[0]) % m);
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
    //nothing to be done to reset Vigenere
  }
}