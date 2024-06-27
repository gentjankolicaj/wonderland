package io.wonderland.alice.crypto;

import io.wonderland.alice.exception.RuntimeCipherException;

/**
 * Asymmetric (Public Key) ciphers implementations are expected conform to this interface.
 */
public interface AsymmetricCipher extends ICipher {

  /**
   * initialise the cipher.
   *
   * @param forEncryption if true the cipher is initialised for encryption, if false for
   *                      decryption.
   * @param param         the key and other data required by the cipher.
   */
  void init(boolean forEncryption, CipherParameter param);


  /**
   * returns the largest size an input block can be.
   *
   * @return maximum size for an input block.
   */
  int getInputBlockSize();


  /**
   * returns the maximum size of the block produced by this cipher.
   *
   * @return maximum size of the output block produced by the cipher.
   */
  int getOutputBlockSize();


  /**
   * process the block of len bytes stored in from offset inOff.
   *
   * @param in    the input data
   * @param inOff offset into the in array where the data starts
   * @param len   the length of the block to be processed.
   * @return the resulting byte array of the encryption/decryption process.
   * @throws RuntimeCipherException   other exceptions cipher might throw
   * @throws IllegalStateException if the cipher isn't initialized.
   */
  byte[] processBlock(byte[] in, int inOff, int len)
      throws RuntimeCipherException, IllegalStateException;


  /**
   * reset the cipher. This leaves it in the same state it was at after the last init (if there was
   * one).
   */
  void reset();

}