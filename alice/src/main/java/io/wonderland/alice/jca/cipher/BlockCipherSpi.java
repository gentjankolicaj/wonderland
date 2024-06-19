package io.wonderland.alice.jca.cipher;

import io.wonderland.alice.crypto.BlockCipher;
import io.wonderland.base.Arrays;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

public abstract class BlockCipherSpi extends CipherSpi {

  private final BlockCipher blockCipher;
  private final byte[] iv;
  private AlgorithmParameters algParams;

  public BlockCipherSpi(BlockCipher blockCipher) {
    this.blockCipher = blockCipher;
    this.iv = org.apache.commons.lang3.ArrayUtils.EMPTY_BYTE_ARRAY;
  }


  public BlockCipherSpi(BlockCipher blockCipher, byte[] iv) {
    this.blockCipher = blockCipher;
    this.iv = iv;
  }

  @Override
  protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
    throw new NoSuchAlgorithmException("Can't support mode " + mode);
  }

  @Override
  protected void engineSetPadding(String padding) throws NoSuchPaddingException {
    throw new NoSuchPaddingException("Can't support padding " + padding);
  }

  @Override
  protected int engineGetBlockSize() {
    return 0;
  }

  @Override
  protected int engineGetOutputSize(int i) {
    return -1;
  }

  @Override
  protected byte[] engineGetIV() {
    return Arrays.clone(iv);
  }

  @Override
  protected int engineGetKeySize(Key key) {
    return key.getEncoded().length * 8;
  }

  @Override
  protected AlgorithmParameters engineGetParameters() {
    if (algParams == null) {
      if (iv != null) {
        try {
          String algorithmName = blockCipher.getAlgorithmName();
          algParams = createAlgParams(algorithmName);
          algParams.init(new IvParameterSpec(iv));
        } catch (Exception e) {
          throw new RuntimeException(e.toString());
        }
      }
    }
    return algParams;
  }

  @Override
  protected void engineInit(int opmode, Key key, SecureRandom secureRandom)
      throws InvalidKeyException {

  }

  @Override
  protected void engineInit(int opmode, Key key, AlgorithmParameterSpec algorithmParameterSpec,
      SecureRandom secureRandom)
      throws InvalidKeyException, InvalidAlgorithmParameterException {

  }

  @Override
  protected void engineInit(int opmode, Key key, AlgorithmParameters algorithmParameters,
      SecureRandom secureRandom)
      throws InvalidKeyException, InvalidAlgorithmParameterException {

  }

  @Override
  protected byte[] engineUpdate(byte[] bytes, int i, int i1) {
    return new byte[0];
  }

  @Override
  protected int engineUpdate(byte[] bytes, int i, int i1, byte[] bytes1, int i2)
      throws ShortBufferException {
    return 0;
  }

  @Override
  protected byte[] engineDoFinal(byte[] bytes, int i, int i1)
      throws IllegalBlockSizeException, BadPaddingException {
    return new byte[0];
  }

  @Override
  protected int engineDoFinal(byte[] bytes, int i, int i1, byte[] bytes1, int i2)
      throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
    return 0;
  }

  private AlgorithmParameters createAlgParams(String algorithmName) {
    return null;
  }
}
