package io.wonderland.alice.crypto;

import io.wonderland.alice.exception.CipherException;
import java.security.Key;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;

public interface ProductCipher {

  /**
   * Initialise the cipher.
   *
   * @param encryption if true, the cipher is initialized for encryption, of false for decryption.
   * @param entries    cipher-key pair entries.
   * @throws IllegalArgumentException if the params argument is inappropriate.
   */
  void init(boolean encryption, Map.Entry<Cipher, Key>... entries) throws CipherException;

  List<Cipher> getCiphers();

  byte[] update(byte[] input);

  byte[] doFinal() throws CipherException, IllegalStateException;

  byte[] doFinal(byte[] input) throws CipherException, IllegalStateException;

  void reset();

}
