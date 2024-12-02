package io.wonderland.alice.jca.symmetric;


import io.wonderland.alice.crypto.BlockCipher;
import io.wonderland.alice.crypto.mode.BlockMode;
import io.wonderland.alice.crypto.mode.CBCBlockCipher;
import io.wonderland.alice.crypto.mode.CTRBlockCipher;
import io.wonderland.alice.crypto.padding.Padding;
import io.wonderland.alice.crypto.padding.Paddings;
import io.wonderland.alice.crypto.params.IVWithParameter;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.spec.SpecUtils;
import io.wonderland.alice.crypto.stream.ResettableOutputStream;
import io.wonderland.alice.exception.GenericPaddingException;
import io.wonderland.alice.exception.RuntimeCipherException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class BlockCipherSpi extends CipherSpi {

  protected static final byte[] EMPTY_ARRAY = new byte[0];
  protected static final int ZERO = 0;
  private final ResettableOutputStream buffer = new ResettableOutputStream();
  protected Class<AlgorithmParameterSpec>[] availableSpecs = SpecUtils.blockCipherAlgParamSpecs();
  private BlockCipher cipher;
  private AlgorithmParameterSpec paramSpec;
  private AlgorithmParameters algParams;
  private IVWithParameter ivParam;
  private Padding padding;
  private int bitKeySize;
  private int ivLen = 0;

  public BlockCipherSpi(BlockCipher cipher) {
    this.cipher = cipher;
    this.padding = Paddings.NO_PADDING.create();
  }

  public BlockCipherSpi(BlockCipher cipher, IVWithParameter ivParam, int bitKeySize, int ivLen) {
    this.cipher = cipher;
    this.ivParam = ivParam;
    this.bitKeySize = bitKeySize;
    this.ivLen = ivLen;
  }

  @Override
  protected int engineGetBlockSize() {
    return cipher.getBlockSize();
  }

  @Override
  protected byte[] engineGetIV() {
    if (ivParam != null) {
      return ivParam.getIv();
    }
    return null;
  }

  @Override
  protected int engineGetKeySize(Key key) {
    if (key == null) {
      throw new IllegalArgumentException("Key argument is null");
    }
    return key.getEncoded().length * 8;

  }

  @Override
  protected int engineGetOutputSize(int inputLen) {
    return cipher.getOutputSize(inputLen);
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
    String modeName = mode.trim().toUpperCase();
    BlockMode blockMode = BlockMode.parse(modeName);
    switch (blockMode) {
      case ECB:
        ivLen = 0;
        break;
      case CBC:
        this.ivLen = cipher.getBlockSize();
        this.cipher = new CBCBlockCipher(cipher);
        break;
      case CTR:
        this.ivLen = cipher.getBlockSize();
        this.cipher = new CTRBlockCipher(cipher);
        break;
      default:
        throw new NoSuchAlgorithmException("Block cipher mode " + blockMode + " not supported");
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
    ParameterList parameterList = new ParameterList();
    if (key == null) {
      throw new InvalidKeyException("Key empty");
    } else if (!(key instanceof SecretKey)) {
      throw new InvalidKeyException(
          String.format("Key for algorithm %s not suitable for symmetric encryption",
              key.getAlgorithm()));
    }
    parameterList.add(new KeyParameter<>(key));

    //reset cipher buffer before init
    buffer.reset();
    switch (opmode) {
      case Cipher.ENCRYPT_MODE:
        cipher.init(true, parameterList);
        break;
      case Cipher.DECRYPT_MODE:
        cipher.init(false, parameterList);
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
    this.buffer.write(input, inputOffset, inputLen);
    return EMPTY_ARRAY;
  }

  @Override
  protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output,
      int outputOffset) {
    this.buffer.write(input, inputOffset, inputLen);
    return ZERO;
  }

  @Override
  protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen)
      throws BadPaddingException {
    try {
      engineUpdate(input, inputOffset, inputLen);
      int outputSize = engineGetOutputSize(inputLen);
      byte[] output = new byte[outputSize];
      cipher.processBlock(this.buffer.getBuffer(), 0, output, 0);

      //reset input array
      Arrays.fill(input, (byte) 0);

      return output;
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
    if (outputOffset + engineGetOutputSize(inputLen) > output.length) {
      throw new ShortBufferException("Output array too short for processed input.");
    }
    try {
      engineUpdate(input, inputOffset, inputLen, output, outputOffset);

      cipher.processBlock(this.buffer.getBuffer(), 0, output, outputOffset);

      //reset arrays
      Arrays.fill(input, (byte) 0);

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
