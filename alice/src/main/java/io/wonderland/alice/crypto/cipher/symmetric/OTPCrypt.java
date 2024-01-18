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
 * * OTP cipher implementation, works only for UTF-8.
 *
 * <br>Notation:
 * <br> Key: K
 * <br> Plaintext: P
 * <br> Ciphertext: C
 * <br> Keyspace: #
 * <br> Alphabet size: m
 *
 * <br>
 * <br> OTP cipher function:
 * <br> CONDITIONS:
 * <br> 1.|K|>=|P| , key length must bigger/equal to plaintext length
 * <br> 2.Key elements must be randomly uniformly distributed
 * <br> 3.Key must never be reused.
 * <br> 4.Key must be kept secret by communicating parties
 * <br>
 * <br> Keyspace : # = m * m * m * ...m multiplied |K| times.
 * <br> Encryption : Ci = (Pi + Ki) mod(m)
 * <br> Decryption : Pi = (Ci - Ki) mod(m)
 * <br>
 * <br>
 * For more check <a href="https://en.wikipedia.org/wiki/One-time_pad">otp cipher link</a>
 */
@Slf4j
public final class OTPCrypt implements StreamCipher {
  private final int m = CharsetsUtils.getDefaultAlphabetSize();
  private byte[] key;
  private boolean encryption;

  public OTPCrypt() {
    log.warn("OTP cipher implemented for charset : {}", Charset.defaultCharset());
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
    if (key.length < in.length) {
      throw new IllegalArgumentException("OTP cipher requires key length bigger/equal to plaintext length.");
    }
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte p = in[inOff + i];
      int c = (p + key[inOff + i]) % m;
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
    if (key.length < in.length) {
      throw new IllegalArgumentException("OTP cipher requires key length bigger/equal to ciphertext length.");
    }
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte c = in[inOff + i];
      int p = (c - key[inOff + i]) % m;
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
      throw new IllegalArgumentException("Invalid parameter passed to OTP init - key parameter not found.");
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
      throw new IllegalArgumentException("Invalid parameter passed to OTP init - " + params.getClass().getName());
    }
  }

  @Override
  public String getAlgorithmName() {
    return AlgNames.OTP;
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
    //nothing to be done to reset otp
  }
}
