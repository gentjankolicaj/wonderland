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
import io.wonderland.base.ByteArrayBuilder;
import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
public class RSACrypt implements AsymmetricCipher {

  private final List<byte[]> blocks = new LinkedList<>();
  @Getter
  private final RSAPadding rsaPadding;
  private RSAPublicKey publicKey;
  private RSAPrivateKey privateKey;
  private boolean encryption;


  public RSACrypt(RSAPadding rsaPadding) {
    this.rsaPadding = rsaPadding;
  }

  public byte[] encrypt(byte[] in, int inOff, int inLen) throws CipherException {
    BigInteger exp = null;
    BigInteger mod = null;
    if (publicKey != null) {
      exp = publicKey.getPublicExponent();
      mod = publicKey.getModulus();
    } else if (privateKey != null) {
      exp = privateKey.getPrivateExponent();
      mod = privateKey.getModulus();
    }

    update(in, inOff, inLen);
    byte[] message = ByteArrayBuilder.toByteArray(blocks);

    //rsa encrypt
    BigInteger p = assertMessage(message, mod);
    BigInteger c = p.modPow(exp, mod);

    //refill plaintext arrays
    Arrays.fill(in, (byte) 0);
    Arrays.fill(message, (byte) 0);
    resetBlocks();

    return c.toByteArray();
  }


  public byte[] decrypt(byte[] in, int inOff, int inLen) throws CipherException {
    BigInteger exp = null;
    BigInteger mod = null;
    if (publicKey != null) {
      exp = publicKey.getPublicExponent();
      mod = publicKey.getModulus();
    } else if (privateKey != null) {
      exp = privateKey.getPrivateExponent();
      mod = privateKey.getModulus();
    }
    update(in, inOff, inLen);
    byte[] message = ByteArrayBuilder.toByteArray(blocks);

    //rsa decrypt
    BigInteger c = assertMessage(in, mod);
    BigInteger p = c.modPow(exp, mod);

    //refill plaintext arrays
    Arrays.fill(in, (byte) 0);
    Arrays.fill(message, (byte) 0);
    resetBlocks();

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
  public String[] getKeyTypeNames() {
    return new String[]{
        RSAPrivateKey.class.getSimpleName() + " or " + RSAPublicKey.class.getSimpleName()};
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
  public void update(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) {
    if (inputLen > 0) {
      byte[] des = new byte[inputLen];
      System.arraycopy(input, inputOffset, des, 0, inputLen);
      System.arraycopy(des, 0, output, outputOffset, des.length);
      blocks.add(des);
    }
  }

  @Override
  public void update(byte[] input, int inputOffset, int inputLen) {
    if (inputLen > 0) {
      byte[] des = new byte[inputLen];
      System.arraycopy(input, inputOffset, des, 0, inputLen);
      blocks.add(des);
    }
  }

  @Override
  public void reset() {
    resetBlocks();
  }

  protected void resetBlocks() {
    if (CollectionUtils.isNotEmpty(blocks)) {
      blocks.forEach(arr -> Arrays.fill(arr, (byte) 0));
    }
  }

  private BigInteger assertMessage(byte[] message, BigInteger modulus) throws PaddingException {
    BigInteger x = new BigInteger(1, message);
    if (x.compareTo(modulus) >= 0) {
      throw new PaddingException("Message bigger than modulus, information will be lost.");
    }
    return x;
  }


}
