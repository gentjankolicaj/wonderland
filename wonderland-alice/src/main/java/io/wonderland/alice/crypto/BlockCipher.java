package io.wonderland.alice.crypto;

import io.wonderland.alice.exception.CipherException;
import io.wonderland.alice.exception.RuntimeCipherException;

/**
 * Block cipher implementations expected conform to this interface.
 */
public interface BlockCipher extends ICipher {

  /**
   * Initialise the cipher.
   *
   * @param encryption if true, the cipher is initialized for encryption, if false for decryption.
   * @param params     the key and other data required by the cipher.
   * @throws IllegalArgumentException if the params argument is inappropriate.
   */
  void init(boolean encryption, CipherParameter params) throws IllegalArgumentException;


  /**
   * Return the block size for this cipher (in bytes).
   *
   * @return the block size for this cipher in bytes.
   */
  int getBlockSize();

  /**
   * Process one block of input from the array in and write it to the out array.
   *
   * @param in     the array containing the input data.
   * @param inOff  offset into the in array the data starts at.
   * @param out    the array the output data will be copied into.
   * @param outOff the offset into the out array the output will start at.
   * @return the number of bytes processed and produced.
   * @throws CipherException       other exceptions cipher might throw
   * @throws IllegalStateException if the cipher isn't initialized.
   */
  int processBlock(byte[] in, int inOff, byte[] out, int outOff)
      throws RuntimeCipherException, IllegalStateException;

  /**
   * Reset the cipher. After resetting, the cipher is in the same state as it was after the last
   * init (if there was one).
   */
  void reset();

  /**
   * Calculate block cipher output size
   *
   * @param inputLen
   * @return
   */
  int getOutputSize(int inputLen);

  @Override
  default String[] getKeyTypeNames() {
    return new String[0];
  }


}
