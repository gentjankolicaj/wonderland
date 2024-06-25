package io.wonderland.alice.crypto;

import io.wonderland.alice.exception.DataLengthException;

/**
 * the interface stream ciphers conform to.
 */
public interface StreamCipher extends ICipher {

  /**
   * Initialise the cipher.
   *
   * @param encryption if true, the cipher is initialized for encryption, if false for decryption.
   * @param params     the key and other data required by the cipher.
   * @throws IllegalArgumentException if the params argument is inappropriate.
   */
  void init(boolean encryption, CipherParameters params)
      throws IllegalArgumentException;


  /**
   * encrypt/decrypt a single byte returning the result.
   *
   * @param in the byte to be processed.
   * @return the result of processing the input byte.
   */
  byte processByte(byte in);

  /**
   * process a block of bytes from in putting the result into out.
   *
   * @param in     the input byte array.
   * @param inOff  the offset into the in array where the data to be processed starts.
   * @param len    the number of bytes to be processed.
   * @param out    the output buffer the processed bytes go into.
   * @param outOff the offset into the output byte array the processed data starts at.
   * @return the number of bytes produced - should always be len.
   * @throws DataLengthException if the output buffer is too small.
   */
  int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff)
      throws DataLengthException;

  /**
   * reset the cipher. This leaves it in the same state it was at after the last init (if there was
   * one).
   */
  void reset();


}