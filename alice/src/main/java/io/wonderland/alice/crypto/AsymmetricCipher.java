package io.wonderland.alice.crypto;

import io.wonderland.alice.exception.CipherException;

/**
 * the interface PK ciphers conform to.
 */
public interface AsymmetricCipher extends ICipher {

  /**
   * initialise the cipher.
   *
   * @param forEncryption if true the cipher is initialised for encryption, if false for
   *                      decryption.
   * @param param         the key and other data required by the cipher.
   */
  void init(boolean forEncryption, CipherParameters param);


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
   * @throws CipherException the input data is too large for the cipher.
   */
  byte[] processBlock(byte[] in, int inOff, int len) throws CipherException, IllegalStateException;

  /**
   * Update buffer block with plaintext.
   *
   * @param input
   * @param inputOffset
   * @param inputLen
   */
  void update(byte[] input, int inputOffset, int inputLen);

  /**
   * Update buffer block with plaintext
   *
   * @param input
   * @param inputOffset
   * @param inputLen
   * @param output
   * @param outputOffset
   */
  void update(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset);

  /**
   * reset the cipher. This leaves it in the same state it was at after the last init (if there was
   * one).
   */
  void reset();

}