package io.wonderland.alice.jca.asymmetric.rsa;

import io.wonderland.alice.crypto.AsymmetricCipher;
import io.wonderland.alice.crypto.CipherParameter;
import io.wonderland.alice.crypto.padding.Padding;
import io.wonderland.alice.crypto.padding.Paddings;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.spec.SpecUtils;
import io.wonderland.alice.crypto.stream.ResettableOutputStream;
import io.wonderland.alice.exception.GenericPaddingException;
import io.wonderland.alice.exception.RuntimeCipherException;
import io.wonderland.alice.jca.asymmetric.AsymmetricCipherSpi;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class RSACipherSpi extends AsymmetricCipherSpi {

  private final AsymmetricCipher cipher;
  private final ResettableOutputStream buffer = new ResettableOutputStream();
  protected Class<AlgorithmParameterSpec>[] availableSpecs = SpecUtils.rsaAlgParamSpecs();
  private AlgorithmParameterSpec paramSpec;
  private AlgorithmParameters algParams;
  private CipherParameter cipherParam;
  private Padding padding;
  private boolean publicKeyOnly = false;
  private boolean privateKeyOnly = false;

  public RSACipherSpi(AsymmetricCipher cipher) {
    this.cipher = cipher;
    this.padding = Paddings.NO_PADDING.create();
  }

  public RSACipherSpi(AsymmetricCipher cipher, Padding padding) {
    this.cipher = cipher;
    this.padding = padding;
  }

  @Override
  protected int engineGetBlockSize() {
    return cipher.getInputBlockSize();
  }

  @Override
  protected int engineGetKeySize(Key key) {
    if (key instanceof RSAPublicKey) {
      RSAPublicKey rsaPublicKey = (RSAPublicKey) key;
      return rsaPublicKey.getModulus().bitLength();
    } else if (key instanceof RSAPrivateKey) {
      RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) key;
      return rsaPrivateKey.getModulus().bitLength();
    } else {
      throw new IllegalArgumentException(cipher.invalidKeyTypeParamMessage());
    }
  }

  @Override
  protected int engineGetOutputSize(int inputLen) {
    return cipher.getOutputBlockSize();
  }

  @Override
  protected byte[] engineGetIV() {
    return null;
  }

  @Override
  protected AlgorithmParameters engineGetParameters() {
    return algParams;
  }

  @Override
  protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
    if (StringUtils.isEmpty(mode)) {
      throw new NoSuchAlgorithmException("Can't support mode " + mode);
    }
    String modeUpperCase = mode.trim().toUpperCase();
    switch (modeUpperCase) {
      case "NONE":
      case "ECB":
        return;
      case "1":
        privateKeyOnly = true;
        publicKeyOnly = false;
        break;
      case "2":
        privateKeyOnly = false;
        publicKeyOnly = true;
        break;
    }
    throw new NoSuchAlgorithmException("Can't support mode " + mode);
  }

  @Override
  protected void engineSetPadding(String padding) throws NoSuchPaddingException {
    if (StringUtils.isEmpty(padding)) {
      throw new NoSuchPaddingException("Padding not valid " + padding);
    }
    String padName = padding.trim().toUpperCase();
    if (padName.equals(Paddings.NO_PADDING.name())) {
      this.padding = Paddings.NO_PADDING.create();
    } else if (padName.equals(Paddings.ONE_PADDING.name())) {
      this.padding = Paddings.ONE_PADDING.create();
    } else if (padName.equals(Paddings.ZERO_PADDING.name())) {
      this.padding = Paddings.ZERO_PADDING.create();
    } else {
      throw new NoSuchPaddingException(padName + " unsupported for RSA.");
    }
  }

  @Override
  protected void engineInit(int opmode, Key key, AlgorithmParameterSpec algorithmParameterSpec,
      SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    ParameterList cipherParams = new ParameterList();
    if (key instanceof RSAPublicKey) {
      if (privateKeyOnly && opmode == Cipher.ENCRYPT_MODE) {
        throw new InvalidKeyException("mode 1 requires RSAPrivateKey");
      }
      cipherParams.add(new KeyParameter<>(key));
    } else if (key instanceof RSAPrivateKey) {
      if (publicKeyOnly && opmode == Cipher.ENCRYPT_MODE) {
        throw new InvalidKeyException("mode 2 requires RSAPublicKey");
      }
      cipherParams.add(new KeyParameter<>(key));
    } else {
      throw new InvalidKeyException("Unknown key " + key + " passed to RSA init.");
    }
    //reset cipher buffer before init
    buffer.reset();
    switch (opmode) {
      case Cipher.ENCRYPT_MODE:
        cipher.init(true, cipherParams);
        break;
      case Cipher.DECRYPT_MODE:
        cipher.init(false, cipherParams);
        break;
      default:
        throw new IllegalArgumentException("Unknown opmode " + opmode + " passed to RSA init.");
    }
  }

  @Override
  protected void engineInit(int opmode, Key key, AlgorithmParameters algorithmParameters,
      SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    if (algorithmParameters == null) {
      engineInit(opmode, key, (AlgorithmParameterSpec) null, secureRandom);
    } else {
      AlgorithmParameterSpec paramSpec = null;
      if (!ArrayUtils.isEmpty(availableSpecs)) {
        for (Class<AlgorithmParameterSpec> spec : availableSpecs) {
          try {
            paramSpec = algorithmParameters.getParameterSpec(spec);
          } catch (Exception e) {
            //do nothing
          }
        }
      }
      this.paramSpec = paramSpec;
      this.algParams = algorithmParameters;
      this.engineInit(opmode, key, paramSpec, secureRandom);
    }
  }

  @Override
  protected void engineInit(int opmode, Key key, SecureRandom secureRandom)
      throws InvalidKeyException {
    try {
      engineInit(opmode, key, (AlgorithmParameterSpec) null, secureRandom);
    } catch (InvalidAlgorithmParameterException e) {
      throw new InvalidKeyException("Exception : " + e.getMessage(), e);
    }
  }

  @Override
  protected byte[] engineUpdate(byte[] input, int inputOffset, int inputLen) {
    final int bufferSize = this.buffer.size();
    if (inputLen > cipher.getInputBlockSize() - bufferSize) {
      throw new ArrayIndexOutOfBoundsException("To much input for RSA.");
    }
    this.buffer.write(input, inputOffset, inputLen);
    return EMPTY_ARRAY;
  }

  @Override
  protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output,
      int outputOffset)
      throws ShortBufferException {
    final int bufferSize = this.buffer.size();
    if (inputLen > cipher.getInputBlockSize() - bufferSize) {
      throw new ShortBufferException("To much input for RSA.");
    }
    this.buffer.write(input, inputOffset, inputLen);
    return ZERO;
  }

  @Override
  protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen)
      throws BadPaddingException {
    try {
      engineUpdate(input, inputOffset, inputLen);
      byte[] processedBlock = cipher.processBlock(this.buffer.getBuffer(), 0, this.buffer.size());
      return processedBlock;
    } catch (RuntimeCipherException e) {
      throw new GenericPaddingException(e.getMessage(), e);
    } finally {
      //reset cipher & buffers
      buffer.reset();
      cipher.reset();
    }
  }

  @Override
  protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output,
      int outputOffset)
      throws ShortBufferException, BadPaddingException {
    try {
      engineUpdate(input, inputOffset, inputLen, output, outputOffset);
      byte[] processedBlock = cipher.processBlock(this.buffer.getBuffer(), 0, this.buffer.size());

      //copy processed block into output
      for (int i = 0, len = processedBlock.length; i < len; i++) {
        output[outputOffset + i] = processedBlock[i];
      }
      //reset arrays
      Arrays.fill(input, (byte) 0);
      Arrays.fill(processedBlock, (byte) 0);

      return ZERO;
    } catch (RuntimeCipherException e) {
      throw new GenericPaddingException("Failed cipher.", e);
    } finally {
      //reset cipher & buffers
      buffer.reset();
      cipher.reset();
    }
  }


}
