package io.wonderland.alice.crypto;

import io.wonderland.alice.exception.CipherException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.crypto.Cipher;
import org.apache.commons.lang3.ArrayUtils;

//todo to re-implement & test
public final class ProductCipherImpl implements ProductCipher {

  private final List<Cipher> ciphers = new ArrayList<>();
  private byte[] inputBuffer;


  @SafeVarargs
  @Override
  public final void init(boolean encryption, Entry<Cipher, Key>... entries) {
    if (ArrayUtils.isEmpty(entries)) {
      throw new IllegalArgumentException("Cipher-Key entries must not be empty");
    }
    //if previous ciphers exist, clear them.
    if (!this.ciphers.isEmpty()) {
      this.ciphers.clear();
    }

    //Initialize new ciphers
    Cipher cipher = null;
    try {
      for (Entry<Cipher, Key> entry : entries) {
        cipher = entry.getKey();
        int opmode = encryption ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        cipher.init(opmode, entry.getValue());
        ciphers.add(cipher);
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Failed to init cipher - " + cipher + " with exception msg : " + e.getMessage());
    }
  }

  @Override
  public List<Cipher> getCiphers() {
    return this.ciphers;
  }

  @Override
  public byte[] update(byte[] input) {
    if (ArrayUtils.isEmpty(input)) {
      throw new IllegalArgumentException("Input array is empty.");
    }
    int newBufferLength = inputBuffer == null ? input.length : inputBuffer.length + input.length;
    byte[] newInputBuffer = new byte[newBufferLength];

    if (inputBuffer != null) {
      System.arraycopy(inputBuffer, 0, newInputBuffer, 0, inputBuffer.length);
      System.arraycopy(input, 0, newInputBuffer, inputBuffer.length, input.length);
    } else {
      System.arraycopy(input, 0, newInputBuffer, 0, input.length);
    }
    this.inputBuffer = newInputBuffer;
    return this.inputBuffer;
  }

  @Override
  public byte[] doFinal() throws CipherException, IllegalStateException {
    //Cipher output/input chaining
    //Start from second cipher[0] to cipher[last], 0-th ciphers ciphertext i-th ciphers plaintext
    //Last cipher doFinal method is called outside
    byte[] ciphertext = inputBuffer;
    for (Cipher cipher : ciphers) {
      ciphertext = cipher.update(ciphertext);
    }
    this.reset();
    return ciphertext;
  }

  @Override
  public byte[] doFinal(byte[] input) throws CipherException, IllegalStateException {
    this.update(input);

    //Cipher output/input chaining
    //Start from second cipher[0] to cipher[last], 0-th ciphers ciphertext i-th ciphers plaintext
    //Last cipher doFinal method is called outside
    byte[] ciphertext = inputBuffer;
    for (Cipher cipher : ciphers) {
      ciphertext = cipher.update(ciphertext);
    }
    this.reset();
    return ciphertext;
  }

  @Override
  public void reset() {
    //reset input buffer
    this.inputBuffer = null;
  }
}
