package io.wonderland.alice.jca.cipher;

//todo: extends CipherSpi

/**
 * When requesting a block cipher in stream cipher mode (for example; DES in CFB or OFB mode), a
 * client may optionally specify the number of bits to be processed at a time, by appending this
 * number to the mode name as shown in the following sample transformations:
 * <p>
 * Cipher c1 = Cipher.getInstance("DES/CFB8/NoPadding"); Cipher c2 =
 * Cipher.getInstance("DES/OFB32/PKCS5Padding");
 */
public class DES {


}
