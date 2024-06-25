package io.wonderland.alice.crypto.cipher.symmetric;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.OTPKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.exception.CipherException;
import io.wonderland.alice.exception.DataLengthException;
import io.wonderland.alice.exception.ExceptionMessages;
import io.wonderland.base.IntUtils;
import java.security.Key;
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

  private int modulus;
  private int[] keys;
  private boolean encryption;

  public OTPCrypt() {
  }


  public int encrypt(byte[] in, int inOff, byte[] out, int outOff) throws CipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(ExceptionMessages.PLAINTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ExceptionMessages.ARRAY_OFFSET_NOT_VALID);

    }
    if (ArrayUtils.isEmpty(keys)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    if (keys.length < in.length) {
      throw new IllegalArgumentException(
          "OTP cipher requires key length bigger/equal to plaintext length.");
    }
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte p = in[inOff + i];
      int c = (p + keys[inOff + i]) % modulus;
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
    if (ArrayUtils.isEmpty(keys)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    if (keys.length < in.length) {
      throw new IllegalArgumentException(
          "OTP cipher requires key length bigger/equal to ciphertext length.");
    }
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte c = in[inOff + i];
      int p = (c - keys[inOff + i]) % modulus;
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
          if (key instanceof OTPKey) {
            OTPKey otpKey = (OTPKey) key;
            this.keys = IntUtils.array(otpKey.getCodeKeys());
            this.modulus = otpKey.getModulus();
            this.encryption = encryption;
            return;
          } else {
            throw new IllegalArgumentException(invalidKeyTypeParamMessage());
          }
        } else if (param instanceof KeyWithIVParameter) {
          Key key = ((KeyWithIVParameter<?>) param).getKey();
          if (key instanceof OTPKey) {
            OTPKey otpKey = (OTPKey) key;
            this.keys = IntUtils.array(otpKey.getCodeKeys());
            this.modulus = otpKey.getModulus();
            this.encryption = encryption;
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
      if (key instanceof OTPKey) {
        OTPKey otpKey = (OTPKey) key;
        this.keys = IntUtils.array(otpKey.getCodeKeys());
        this.modulus = otpKey.getModulus();
        this.encryption = encryption;
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
    } else if (params instanceof KeyWithIVParameter) {
      Key key = ((KeyWithIVParameter<?>) params).getKey();
      if (key instanceof OTPKey) {
        OTPKey otpKey = (OTPKey) key;
        this.keys = IntUtils.array(otpKey.getCodeKeys());
        this.modulus = otpKey.getModulus();
        this.encryption = encryption;
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
    } else {
      throw new IllegalArgumentException(invalidParamMessage());
    }
  }

  @Override
  public String getAlgorithmName() {
    return Algorithms.OTP.getName();
  }

  @Override
  public String[] getKeyTypeNames() {
    return new String[]{OTPKey.class.getName()};
  }

  @Override
  public byte processByte(byte in) {
    if (ArrayUtils.isEmpty(keys)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    if (encryption) {
      return (byte) ((in + keys[0]) % modulus);
    } else {
      return (byte) ((in - keys[0]) % modulus);
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
    //nothing to be done to reset otp
  }

}
