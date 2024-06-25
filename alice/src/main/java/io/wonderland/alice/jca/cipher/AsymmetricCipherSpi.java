package io.wonderland.alice.jca.cipher;

import io.wonderland.alice.crypto.AsymmetricCipher;
import io.wonderland.alice.crypto.padding.Padding;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.spec.CipherSpecUtils;
import io.wonderland.alice.exception.DataLengthException;
import io.wonderland.base.ArgUtils;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.CipherSpi;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class AsymmetricCipherSpi extends CipherSpi {

  private static final byte[] EMPTY = new byte[0];
  private static final int ZERO = 0;
  private final Class<AlgorithmParameterSpec>[] availableSpecs;
  private final AsymmetricCipher cipher;
  private final KeyWithIVParameter keyWithIVParameter;
  private Padding padding;
  private AlgorithmParameters engineParams;

  protected AsymmetricCipherSpi(AsymmetricCipher cipher) {
    this(cipher, null);
  }

  protected AsymmetricCipherSpi(AsymmetricCipher cipher, KeyWithIVParameter keyWithIVParameter) {
    this.availableSpecs = CipherSpecUtils.availableAlgParamSpecs();
    this.cipher = cipher;
    this.keyWithIVParameter = keyWithIVParameter;
  }

  protected AsymmetricCipherSpi(AsymmetricCipher cipher, KeyWithIVParameter keyWithIVParameter,
      Padding padding) {
    this(cipher, keyWithIVParameter);
    this.padding = padding;
  }

  protected AsymmetricCipherSpi(AsymmetricCipher cipher, KeyWithIVParameter keyWithIVParameter,
      Padding padding, AlgorithmParameters engineParams) {
    this.availableSpecs = CipherSpecUtils.availableAlgParamSpecs();
    this.cipher = cipher;
    this.keyWithIVParameter = keyWithIVParameter;
    this.padding = padding;
    this.engineParams = engineParams;
  }


  /**
   * Cipher block modes supported for cipher streams are: ECB & NONE because stream is not block.
   *
   * @param mode cipher opmode
   * @throws NoSuchAlgorithmException exception thrown is block modes are not ECB & NONE
   */
  @Override
  protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(mode)) {
      throw new NoSuchAlgorithmException("Can't support mode " + mode);
    }
    if (!mode.equalsIgnoreCase("ECB") && !mode.equalsIgnoreCase("NONE")) {
      throw new NoSuchAlgorithmException("Can't support mode " + mode);
    }
  }

  /**
   * Cipher block padding supported for cipher streams are: NoPadding because streams don't need
   * padding.
   *
   * @param padding type of padding
   * @throws NoSuchPaddingException exception is thrown is padding!=NoPadding
   */
  @Override
  protected void engineSetPadding(String padding) throws NoSuchPaddingException {
    if (StringUtils.isEmpty(padding)) {
      throw new NoSuchPaddingException("Can't support padding " + padding);
    }

    if (!padding.equalsIgnoreCase("NoPadding")) {
      throw new NoSuchPaddingException("Can't support padding " + padding);
    }
  }

  @Override
  protected int engineGetBlockSize() {
    return -1;
  }

  @Override
  protected int engineGetOutputSize(int i) {
    return -1;
  }

  @Override
  protected byte[] engineGetIV() {
    if (keyWithIVParameter != null) {
      return keyWithIVParameter.getIv();
    }
    return new byte[0];
  }

  @Override
  protected AlgorithmParameters engineGetParameters() {
    return engineParams;
  }

  @Override
  protected void engineInit(int opmode, Key key, SecureRandom secureRandom)
      throws InvalidKeyException {
    try {
      this.engineInit(opmode, key, (AlgorithmParameterSpec) null, secureRandom);
    } catch (InvalidAlgorithmParameterException e) {
      throw new InvalidKeyException(e.getMessage());
    }
  }

  @Override
  protected void engineInit(int opmode, Key key, AlgorithmParameterSpec algorithmParameterSpec,
      SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {

    if (!(key instanceof Key)) {
      throw new InvalidKeyException(ArgUtils.stringArg(' ',
          "Key for algorithm", key.getAlgorithm(), "not suitable for symmetric encryption"));
    }
    ParameterList parameterList = new ParameterList(new KeyParameter<>(key));

    try {
      switch (opmode) {
        case 1:
          cipher.init(true, parameterList);
          break;
        case 2:
          cipher.init(false, parameterList);
          break;
        default:
          throw new InvalidParameterException("Unknown opmode " + opmode + " passed.");
      }

    } catch (Exception e) {
      throw new InvalidKeyException(e.getMessage());
    }

  }

  @Override
  protected void engineInit(int opmode, Key key, AlgorithmParameters algorithmParameters,
      SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    AlgorithmParameterSpec algParamSpec = null;
    if (algorithmParameters != null && !ArrayUtils.isEmpty(availableSpecs)) {
      for (Class<AlgorithmParameterSpec> spec : availableSpecs) {
        try {
          algParamSpec = algorithmParameters.getParameterSpec(spec);
        } catch (Exception e) {
          //do nothing
        }
      }
    }
    this.engineInit(opmode, key, algParamSpec, secureRandom);
    this.engineParams = algorithmParameters;
  }

  @Override
  protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
    this.cipher.update(input, inputOffset, inputLen);
    return EMPTY;
  }

  @Override
  protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output,
      int outputOffset)
      throws ShortBufferException {
    if (inputLen + outputOffset > output.length) {
      throw new ShortBufferException("output buffer to short for input.");
    }
    try {
      this.cipher.update(input, inputOffset, inputLen, output, outputOffset);
      return ZERO;
    } catch (Exception e) {
      throw new DataLengthException(e.getMessage());
    }
  }

  @Override
  protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) {
    if (inputLen != 0) {
      return this.cipher.processBlock(input, inputOffset, inputLen);
    } else {
      this.cipher.reset();
      return new byte[0];
    }
  }

  @Override
  protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output,
      int outputOffset) throws ShortBufferException {
    if (inputLen + outputOffset > output.length) {
      throw new ShortBufferException("output buffer to short for input.");
    } else {
      if (inputLen != 0) {
        this.engineUpdate(input, inputOffset, inputLen, output, outputOffset);
      }
      this.cipher.reset();
      return inputLen;
    }
  }


}
