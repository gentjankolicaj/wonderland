package io.wonderland.alice.jca.asymmetric;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.CipherSpi;
import javax.crypto.NoSuchPaddingException;


public abstract class AsymmetricCipherSpi extends CipherSpi {

  protected static final byte[] EMPTY_ARRAY = new byte[0];
  protected static final int ZERO = 0;


  /**
   * Cipher block modes supported.
   *
   * @param mode cipher opmode
   * @throws NoSuchAlgorithmException exception thrown is block modes are not ECB & NONE
   */
  @Override
  protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
    throw new NoSuchAlgorithmException("Can't support mode " + mode);
  }

  /**
   * Cipher block padding supported.
   *
   * @param padding type of padding
   * @throws NoSuchPaddingException exception is thrown is padding!=NoPadding
   */
  @Override
  protected void engineSetPadding(String padding) throws NoSuchPaddingException {
    throw new NoSuchPaddingException("Can't support padding " + padding);
  }

  @Override
  protected int engineGetBlockSize() {
    return 0;
  }

  @Override
  protected int engineGetOutputSize(int inputLen) {
    return inputLen;
  }

  @Override
  protected byte[] engineGetIV() {
    return EMPTY_ARRAY;
  }

  @Override
  protected int engineGetKeySize(Key key) {
    return key.getEncoded().length;
  }

}
