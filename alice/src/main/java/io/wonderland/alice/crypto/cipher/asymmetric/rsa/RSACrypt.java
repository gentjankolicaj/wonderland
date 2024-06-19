package io.wonderland.alice.crypto.cipher.asymmetric.rsa;


import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.AsymmetricCipher;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.crypto.padding.RSAPadding;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.exception.CipherException;
import io.wonderland.alice.exception.PaddingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

public class RSACrypt implements AsymmetricCipher {

  public static final String PARAMETER_NOT_FOUND = "Invalid parameter passed to RSA init - key parameter not found.";
  private static final String INVALID_PARAMETER_TYPE_KEY = "Invalid parameter key passed to RSA init - key parameter must be (RSAPublicKey or RSAPrivateKey)";

  @Getter
  private final RSAPadding rsaPadding;
  private RSAPublicKey rsaPublicKey;
  private RSAPrivateKey rsaPrivateKey;
  private boolean encryption;
  private byte[] buffer;

  public RSACrypt(RSAPadding rsaPadding) {
    this.rsaPadding = rsaPadding;
  }

  public byte[] encrypt(byte[] in, int inOff, int len) throws CipherException {
    BigInteger exp = null;
    BigInteger mod = null;
    if (rsaPublicKey != null) {
      exp = rsaPublicKey.getPublicExponent();
      mod = rsaPublicKey.getModulus();
    } else if (rsaPrivateKey != null) {
      exp = rsaPrivateKey.getPrivateExponent();
      mod = rsaPrivateKey.getModulus();
    }
    int size = len - inOff;
    byte[] message = new byte[size];
    System.arraycopy(in, inOff, message, 0, size);

    //rsa encrypt
    BigInteger p = parseMsg(in, mod);
    BigInteger c = p.modPow(exp, mod);

    //refill plaintext arrays
    Arrays.fill(in, (byte) 0);
    Arrays.fill(message, (byte) 0);
    return c.toByteArray();
  }


  public byte[] decrypt(byte[] in, int inOff, int len) throws CipherException {
    BigInteger exp = null;
    BigInteger mod = null;
    if (rsaPublicKey != null) {
      exp = rsaPublicKey.getPublicExponent();
      mod = rsaPublicKey.getModulus();
    } else if (rsaPrivateKey != null) {
      exp = rsaPrivateKey.getPrivateExponent();
      mod = rsaPrivateKey.getModulus();
    }
    int size = len - inOff;
    byte[] message = new byte[size];
    System.arraycopy(in, inOff, message, 0, size);

    //rsa decrypt
    BigInteger c = parseMsg(in, mod);
    BigInteger p = c.modPow(exp, mod);

    //refill plaintext arrays
    Arrays.fill(in, (byte) 0);
    Arrays.fill(message, (byte) 0);
    return p.toByteArray();
  }


  @Override
  public void init(boolean encryption, CipherParameters params) throws IllegalArgumentException {
    if (params instanceof ParameterList) {
      ParameterList parameterList = (ParameterList) params;
      for (CipherParameters param : parameterList) {
        if (param instanceof KeyParameter) {
          Key key = ((KeyParameter<?>) param).getKey();
          if (key instanceof RSAPrivateKey) {
            this.rsaPrivateKey = (RSAPrivateKey) key;
          } else if (key instanceof RSAPublicKey) {
            this.rsaPublicKey = (RSAPublicKey) key;
          } else {
            throw new IllegalArgumentException(INVALID_PARAMETER_TYPE_KEY);
          }
          this.encryption = encryption;
          return;
        } else if (param instanceof KeyWithIVParameter) {
          Key key = ((KeyWithIVParameter<?>) param).getKey();
          if (key instanceof RSAPrivateKey) {
            this.rsaPrivateKey = (RSAPrivateKey) key;
          } else if (key instanceof RSAPublicKey) {
            this.rsaPublicKey = (RSAPublicKey) key;
          } else {
            throw new IllegalArgumentException(INVALID_PARAMETER_TYPE_KEY);
          }
          this.encryption = encryption;
          return;
        } else {
          throw new IllegalArgumentException(PARAMETER_NOT_FOUND);
        }
      }
    } else if (params instanceof KeyParameter) {
      Key key = ((KeyParameter<Key>) params).getKey();
      if (key instanceof RSAPrivateKey) {
        this.rsaPrivateKey = (RSAPrivateKey) key;
      } else if (key instanceof RSAPublicKey) {
        this.rsaPublicKey = (RSAPublicKey) key;
      } else {
        throw new IllegalArgumentException(INVALID_PARAMETER_TYPE_KEY);
      }
      this.encryption = encryption;
    } else {
      throw new IllegalArgumentException(
          "Invalid parameter passed to RSA init - " + params.getClass().getName());
    }
  }

  @Override
  public int getInputBlockSize() {
    return 0;
  }

  @Override
  public int getOutputBlockSize() {
    return 0;
  }


  @Override
  public String getAlgorithmName() {
    return Algorithms.RSA.getName();
  }


  @Override
  public byte[] processBlock(byte[] in, int inOff, int len)
      throws CipherException, IllegalStateException {
    if (encryption) {
      return encrypt(in, inOff, len);
    } else {
      return decrypt(in, inOff, len);
    }
  }


  @Override
  public void updateBlock(byte[] input, int inputOffset, int inputLen, byte[] output,
      int outputOffset) {

  }

  @Override
  public void updateBlock(byte[] input, int inputOffset, int inputLen) {

  }

  @Override
  public void reset() {
    if (ArrayUtils.isEmpty(buffer)) {
      Arrays.fill(buffer, (byte) 0);
    }
  }

  private BigInteger parseMsg(byte[] msg, BigInteger m) throws PaddingException {
    BigInteger x = new BigInteger(1, msg);
    if (x.compareTo(m) >= 0) {
      throw new PaddingException("Message bigger than modulus.");
    }
    return x;
  }

}
