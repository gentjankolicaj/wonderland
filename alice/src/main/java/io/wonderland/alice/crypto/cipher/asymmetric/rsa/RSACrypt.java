package io.wonderland.alice.crypto.cipher.asymmetric.rsa;

import io.jmathematics.modular.ModExp;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.crypto.DataLengthException;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.cipher.AlgNames;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.params.WrappedKeyParameter;
import io.wonderland.alice.exception.CipherException;
import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSACrypt implements StreamCipher {


  private RSAPublicKey rsaPublicKey;
  private RSAPrivateKey rsaPrivateKey;

  private boolean encryption;

  public void encrypt(byte[] in, int inOff, byte[] out, int outOff) throws CipherException {
    BigInteger exponent = null;
    BigInteger modulus = null;
    if (rsaPublicKey != null) {
      exponent = rsaPublicKey.getPublicExponent();
      modulus = rsaPublicKey.getModulus();
    } else if (rsaPrivateKey != null) {
      exponent = rsaPrivateKey.getPrivateExponent();
      modulus = rsaPrivateKey.getModulus();
    }
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte c = (byte) ModExp.squareMultiply(in[inOff + i], exponent.longValue(), modulus.longValueExact());
      out[outOff + i] = c;
    }

  }


  public void decrypt(byte[] in, int inOff, byte[] out, int outOff) throws CipherException {
    BigInteger exponent = null;
    BigInteger modulus = null;
    if (rsaPublicKey != null) {
      exponent = rsaPublicKey.getPublicExponent();
      modulus = rsaPublicKey.getModulus();
    } else if (rsaPrivateKey != null) {
      exponent = rsaPrivateKey.getPrivateExponent();
      modulus = rsaPrivateKey.getModulus();
    }
    int len = in.length - inOff;
    for (int i = 0; i < len; i++) {
      byte c = (byte) ModExp.squareMultiply(in[inOff + i], exponent.longValue(), modulus.longValueExact());
      out[outOff + i] = c;
    }
  }


  @Override
  public void init(boolean encryption, CipherParameters params) throws IllegalArgumentException {
    if (params instanceof ParameterList) {
      ParameterList parameterList = (ParameterList) params;
      for (CipherParameters param : parameterList) {
        if (param instanceof WrappedKeyParameter) {
          Key key = ((WrappedKeyParameter<Key>) param).getWrappedKey();
          if (key instanceof RSAPrivateKey) {
            this.rsaPrivateKey = (RSAPrivateKey) key;
          } else if (key instanceof RSAPublicKey) {
            this.rsaPublicKey = (RSAPublicKey) key;
          } else {
            throw new IllegalArgumentException(
                "Invalid parameter type key passed to RSA init - key parameter must be (RSAPublicKey or RSAPrivateKey)");
          }
          this.encryption = encryption;
          return;
        }
      }
      throw new IllegalArgumentException("Invalid parameter passed to RSA init - key parameter not found.");
    } else if (params instanceof WrappedKeyParameter) {
      Key key = ((WrappedKeyParameter<Key>) params).getWrappedKey();
      if (key instanceof RSAPrivateKey) {
        this.rsaPrivateKey = (RSAPrivateKey) key;
      } else if (key instanceof RSAPublicKey) {
        this.rsaPublicKey = (RSAPublicKey) key;
      } else {
        throw new IllegalArgumentException(
            "Invalid parameter type key passed to RSA init - key parameter must be (RSAPublicKey or RSAPrivateKey)");
      }
      this.encryption = encryption;
    } else {
      throw new IllegalArgumentException("Invalid parameter passed to RSA init - " + params.getClass().getName());
    }
  }

  @Override
  public String getAlgorithmName() {
    return AlgNames.RSA;
  }

  @Override
  public byte processByte(byte in) {
    return 0;
  }

  @Override
  public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff) throws DataLengthException {
    if (encryption) {
      encrypt(in, inOff, out, outOff);
    } else {
      decrypt(in, inOff, out, outOff);
    }
    return len;
  }


  @Override
  public void reset() {
    //do nothing
  }
}
