package io.wonderland.alice.jca.symmetric;

import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.params.IVWithParameter;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.spec.SpecUtils;
import io.wonderland.alice.exception.RuntimeCipherException;
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
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class StreamCipherSpi extends CipherSpi {

  protected static final byte[] EMPTY_ARRAY = new byte[0];
  private final Class<AlgorithmParameterSpec>[] availableSpecs = SpecUtils.availableAlgParamSpecs();
  private final int bitKeySize;
  private final StreamCipher cipher;
  private final IVWithParameter ivParam;
  private AlgorithmParameters algParams;

  protected StreamCipherSpi(StreamCipher streamCipher, AlgorithmParameters algParams) {
    this(streamCipher, -1, null);
    this.algParams = algParams;
  }

  protected StreamCipherSpi(StreamCipher cipher, int bitKeySize, IVWithParameter ivParam) {
    this.cipher = cipher;
    this.bitKeySize = bitKeySize;
    this.ivParam = ivParam;
  }

  protected StreamCipherSpi(StreamCipher cipher, int keySizeInBits) {
    this(cipher, keySizeInBits, null);
  }

  protected StreamCipherSpi(StreamCipher cipher) {
    this(cipher, -1);
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
  protected int engineGetOutputSize(int inputLen) {
    return inputLen;
  }

  @Override
  protected byte[] engineGetIV() {
    if (ivParam != null) {
      return ivParam.getIv();
    }
    return EMPTY_ARRAY;
  }

  @Override
  protected int engineGetKeySize(Key key) {
    return key.getEncoded().length * 8;
  }

  @Override
  protected AlgorithmParameters engineGetParameters() {
    return algParams;
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
  protected void engineInit(int opmode, Key key, AlgorithmParameterSpec paramSpec,
      SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    ParameterList parameterList = new ParameterList();
    if (key == null) {
      throw new InvalidKeyException("Key empty");
    } else if (!(key instanceof SecretKey)) {
      throw new InvalidKeyException(
          String.format("Key for algorithm %s not suitable for symmetric encryption",
              key.getAlgorithm()));
    }

    parameterList.add(new KeyParameter<>(key));

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
    AlgorithmParameterSpec paramSpec = null;
    if (algorithmParameters != null && !ArrayUtils.isEmpty(availableSpecs)) {
      for (Class<AlgorithmParameterSpec> spec : availableSpecs) {
        try {
          paramSpec = algorithmParameters.getParameterSpec(spec);
        } catch (Exception e) {
          //do nothing
        }
      }
    }

    this.algParams = algorithmParameters;
    this.engineInit(opmode, key, paramSpec, secureRandom);
  }

  @Override
  protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
    byte[] output = new byte[inputLen];
    cipher.processBytes(input, inputOffset, inputLen, output, 0);
    return output;
  }

  @Override
  protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output,
      int outputOffset) throws ShortBufferException {
    if (inputLen + outputOffset > output.length) {
      throw new ShortBufferException("output buffer to short for input.");
    }
    try {
      this.cipher.processBytes(input, inputOffset, inputLen, output, outputOffset);
      return inputLen;
    } catch (RuntimeCipherException e) {
      throw new ShortBufferException(e.getMessage());
    }
  }

  @Override
  protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) {
    if (inputLen != 0) {
      byte[] processedBytes = this.engineUpdate(input, inputOffset, inputLen);

      //reset cipher
      cipher.reset();
      return processedBytes;
    } else {
      this.cipher.reset();
      return EMPTY_ARRAY;
    }
  }

  @Override
  protected int engineDoFinal(byte[] input, int inputOffset, int inputLen,
      byte[] output, int outputOffset) throws ShortBufferException {
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
