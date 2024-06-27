package io.wonderland.alice.crypto.asymmetric.rsa;


import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.AsymmetricCipher;
import io.wonderland.alice.crypto.CipherParameter;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.exception.RuntimeCipherException;
import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RSACrypt implements AsymmetricCipher {

  private RSAPublicKey publicKey;
  private RSAPrivateKey privateKey;
  private boolean encryption;

  public RSACrypt() {
  }

  public byte[] encrypt(byte[] in, int inOff, int inLen) throws RuntimeCipherException {
    BigInteger exp = null;
    BigInteger mod = null;
    if (publicKey != null) {
      exp = publicKey.getPublicExponent();
      mod = publicKey.getModulus();
    } else if (privateKey != null) {
      exp = privateKey.getPrivateExponent();
      mod = privateKey.getModulus();
    }

    byte[] message = new byte[inLen];
    System.arraycopy(in, inOff, message, 0, inLen);

    //rsa encrypt
    BigInteger p = assertMessage(message, mod);
    BigInteger c = p.modPow(exp, mod);

    //refill plaintext arrays
    Arrays.fill(message, (byte) 0);

    return c.toByteArray();
  }


  public byte[] decrypt(byte[] in, int inOff, int inLen) throws RuntimeCipherException {
    BigInteger exp = null;
    BigInteger mod = null;
    if (publicKey != null) {
      exp = publicKey.getPublicExponent();
      mod = publicKey.getModulus();
    } else if (privateKey != null) {
      exp = privateKey.getPrivateExponent();
      mod = privateKey.getModulus();
    }
    byte[] message = new byte[inLen];
    System.arraycopy(in, inOff, message, 0, inLen);

    //rsa decrypt
    BigInteger c = assertMessage(message, mod);
    BigInteger p = c.modPow(exp, mod);

    //refill plaintext arrays
    Arrays.fill(message, (byte) 0);

    return p.toByteArray();
  }


  @Override
  public void init(boolean encryption, CipherParameter params) throws IllegalArgumentException {
    if (params instanceof ParameterList) {
      ParameterList parameterList = (ParameterList) params;
      for (CipherParameter param : parameterList) {
        if (param instanceof KeyParameter) {
          Key key = ((KeyParameter<?>) param).getKey();
          if (key instanceof RSAPrivateKey) {
            this.privateKey = (RSAPrivateKey) key;
          } else if (key instanceof RSAPublicKey) {
            this.publicKey = (RSAPublicKey) key;
          } else {
            throw new IllegalArgumentException(invalidKeyTypeParamMessage());
          }
          this.encryption = encryption;
          return;
        } else if (param instanceof KeyWithIVParameter) {
          Key key = ((KeyWithIVParameter<?>) param).getKey();
          if (key instanceof RSAPrivateKey) {
            this.privateKey = (RSAPrivateKey) key;
          } else if (key instanceof RSAPublicKey) {
            this.publicKey = (RSAPublicKey) key;
          } else {
            throw new IllegalArgumentException(invalidKeyTypeParamMessage());
          }
          this.encryption = encryption;
          log.warn(String.format(DISREGARDED_IV, getAlgorithmName()));
          return;
        } else {
          throw new IllegalArgumentException(invalidParamMessage());
        }
      }
    } else if (params instanceof KeyWithIVParameter) {
      Key key = ((KeyWithIVParameter<?>) params).getKey();
      if (key instanceof RSAPrivateKey) {
        this.privateKey = (RSAPrivateKey) key;
      } else if (key instanceof RSAPublicKey) {
        this.publicKey = (RSAPublicKey) key;
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
      this.encryption = encryption;
      log.warn(String.format(DISREGARDED_IV, getAlgorithmName()));
    } else if (params instanceof KeyParameter) {
      Key key = ((KeyParameter<?>) params).getKey();
      if (key instanceof RSAPrivateKey) {
        this.privateKey = (RSAPrivateKey) key;
      } else if (key instanceof RSAPublicKey) {
        this.publicKey = (RSAPublicKey) key;
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
      this.encryption = encryption;
    } else {
      throw new IllegalArgumentException(invalidParamMessage());
    }
  }

  @Override
  public int getInputBlockSize() {
    return getBlockSize();
  }

  @Override
  public int getOutputBlockSize() {
    return getBlockSize();
  }


  @Override
  public String getAlgorithmName() {
    return Algorithms.RSA.getName();
  }

  @Override
  public String[] getKeyTypeNames() {
    return new String[]{
        RSAPrivateKey.class.getSimpleName() + " or " + RSAPublicKey.class.getSimpleName()};
  }


  @Override
  public byte[] processBlock(byte[] in, int inOff, int len)
      throws RuntimeCipherException, IllegalStateException {
    if (encryption) {
      return encrypt(in, inOff, len);
    } else {
      return decrypt(in, inOff, len);
    }
  }


  @Override
  public void reset() {
  }

  private BigInteger assertMessage(byte[] message, BigInteger modulus) {
    BigInteger x = new BigInteger(1, message);
    if (x.compareTo(modulus) >= 0) {
      throw new RuntimeCipherException("Message bigger than modulus, information will be lost.");
    }
    return x;
  }

  private int getBlockSize() {
    if (privateKey != null) {
      int len = privateKey.getModulus().bitLength();
      return len;
    } else if (publicKey != null) {
      int len = publicKey.getModulus().bitLength();
      return len;
    } else {
      throw new IllegalStateException("Can't determine block size because cipher keys are null.");
    }
  }

}
