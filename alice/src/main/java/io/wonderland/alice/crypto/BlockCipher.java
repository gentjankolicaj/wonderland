package io.wonderland.alice.crypto;

import io.wonderland.alice.exception.DataLengthException;

/**
 * Block cipher engines are expected to conform to this interface.
 */
public interface BlockCipher extends ICipher {

  /**
   * Initialise the cipher.
   *
   * @param encryption if true, the cipher is initialized for encryption, if false for decryption.
   * @param params     the key and other data required by the cipher.
   * @throws IllegalArgumentException if the params argument is inappropriate.
   */
  void init(boolean encryption, CipherParameters params) throws IllegalArgumentException;

  /**
   * Return the name of the algorithm the cipher implements.
   *
   * @return the name of the algorithm the cipher implements.
   */
  String getAlgorithmName();

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
   * @throws DataLengthException   if there isn't enough data in, or space in out.
   * @throws IllegalStateException if the cipher isn't initialized.
   */
  int processBlock(byte[] in, int inOff, byte[] out, int outOff)
      throws DataLengthException, IllegalStateException;

  /**
   * Reset the cipher. After resetting, the cipher is in the same state as it was after the last
   * init (if there was one).
   */
  void reset();
}
