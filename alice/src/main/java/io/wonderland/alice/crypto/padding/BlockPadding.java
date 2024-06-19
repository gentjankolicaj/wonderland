package io.wonderland.alice.crypto.padding;

import io.wonderland.alice.exception.PaddingException;
import java.security.SecureRandom;

/**
 * Block padding is supposed to conform to this interface
 */
public interface BlockPadding<T> {

  /**
   * Initialise the padder.
   *
   * @param random the source of randomness for the padding, if required.
   */
  void init(SecureRandom random) throws IllegalArgumentException;

  /**
   * Return the name of the algorithm the cipher implements.
   *
   * @return the name of the algorithm the cipher implements.
   */
  String getPaddingName();

  /**
   * add the pad bytes to the passed in block, returning the number of bytes added.
   * <p>
   * Note: this assumes that the last block of plain text is always passed to it inside in. i.e. if
   * inOff is zero, indicating the entire block is to be overwritten with padding the value of in
   * should be the same as the last block of plain text. The reason for this is that some modes such
   * as "trailing bit compliment" base the padding on the last byte of plain text.
   * </p>
   */
  int addPadding(byte[] in, int inOff);

  /**
   * return the number of pad bytes present in the block.
   *
   * @throws PaddingException if the padding is badly formed or invalid.
   */
  int padCount(byte[] in) throws PaddingException;


  T getPad();


}
