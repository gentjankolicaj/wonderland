package io.wonderland.alice.crypto.symmetric;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.alice.crypto.padding.BytePadding;
import io.wonderland.alice.crypto.padding.OnePadding;
import io.wonderland.alice.crypto.padding.ZeroPadding;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class PermutationCryptTest {

  @Test
  void transposeEncryptDecryptZeroPadding() {
    byte[] plaintext = "Hello world 123456".getBytes();
    int[] columnOrder = new int[]{4, 2, 1, 3};

    PermutationCrypt permutationCrypt = new PermutationCrypt();
    permutationCrypt.setColumnOrders(columnOrder);
    byte[] plaintextPadded = permutationCrypt.pad(columnOrder, plaintext);
    byte[] ciphertext = new byte[plaintextPadded.length];

    permutationCrypt.transpose(plaintext, ciphertext);
    assertThat(ciphertext).containsExactly(72, 111, 114, 49, 53, 108, 119, 100, 51, 0, 101, 32, 108,
        50, 54, 108, 111,
        32, 52, 0);
    log.info("Plaintext '{}'", new String(plaintextPadded));
    log.info("Ciphertext '{}'", new String(ciphertext));

    PermutationCrypt decryptCipher = new PermutationCrypt(new ZeroPadding(), false);
    decryptCipher.setColumnOrders(columnOrder);
    byte[] decryptedPadded = new byte[ciphertext.length];
    decryptCipher.transpose(ciphertext, decryptedPadded);
    assertThat(decryptedPadded).containsExactly(plaintextPadded);

    log.info("Ciphertext '{}'", new String(ciphertext));
    log.info("Decrypted '{}'", new String(decryptedPadded));
  }

  @Test
  void transposeEncryptDecryptOnePadding() {
    byte[] plaintext = "Hello world 123456".getBytes();
    int[] columnOrder = new int[]{4, 2, 1, 3};

    PermutationCrypt encryptCipher = new PermutationCrypt(new OnePadding(), true);
    encryptCipher.setColumnOrders(columnOrder);
    byte[] plaintextPadded = encryptCipher.pad(columnOrder, plaintext);
    byte[] ciphertext = new byte[plaintextPadded.length];

    encryptCipher.transpose(plaintext, ciphertext);
    assertThat(ciphertext).containsExactly(72, 111, 114, 49, 53, 108, 119, 100, 51, 1, 101, 32, 108,
        50, 54, 108, 111,
        32, 52, 1);
    log.info("Plaintext '{}'", new String(plaintextPadded));
    log.info("Ciphertext '{}'", new String(ciphertext));

    PermutationCrypt decryptCipher = new PermutationCrypt(new OnePadding(), false);
    decryptCipher.setColumnOrders(columnOrder);
    byte[] decryptedPadded = new byte[ciphertext.length];
    decryptCipher.transpose(ciphertext, decryptedPadded);
    assertThat(decryptedPadded).containsExactly(plaintextPadded);

    log.info("Ciphertext '{}'", new String(ciphertext));
    log.info("Decrypted '{}'", new String(decryptedPadded));
  }

  @Test
  void transposeEncryptDecryptBytePadding() {
    byte[] plaintext = "Hello world 123456".getBytes();
    int[] columnOrder = new int[]{4, 2, 1, 3};

    PermutationCrypt encryptCipher = new PermutationCrypt(new BytePadding((byte) -99), true);
    encryptCipher.setColumnOrders(columnOrder);
    byte[] plaintextPadded = encryptCipher.pad(columnOrder, plaintext);
    byte[] ciphertext = new byte[plaintextPadded.length];

    encryptCipher.transpose(plaintext, ciphertext);
    assertThat(ciphertext).containsExactly(72, 111, 114, 49, 53, 108, 119, 100, 51, -99, 101, 32,
        108, 50, 54, 108, 111,
        32, 52, -99);
    log.info("Plaintext '{}'", new String(plaintextPadded));
    log.info("Ciphertext '{}'", new String(ciphertext));

    PermutationCrypt decryptCipher = new PermutationCrypt(new BytePadding((byte) -99), false);
    decryptCipher.setColumnOrders(columnOrder);
    byte[] decryptedPadded = new byte[ciphertext.length];
    decryptCipher.transpose(ciphertext, decryptedPadded);
    assertThat(decryptedPadded).containsExactly(plaintextPadded);

    log.info("Ciphertext '{}'", new String(ciphertext));
    log.info("Decrypted '{}'", new String(decryptedPadded));
  }

  @Test
  void encryptDecrypt() {
    /**
     byte[] plaintext = "Hello World !!@#$%^&&*(*(+_)(*&=-0".getBytes();
     byte[] ciphertext = new byte[plaintext.length];
     byte[] decryptedtext = new byte[ciphertext.length];

     WrappedKeyParameter<Key> wrappedKeyParam = new WrappedKeyParameter<>(new PermutationKey(0, 5, 3, 4, 2, 1));

     BlockCipher cipher = new PermutationCrypt(new BytePadding((byte) 101), true);
     cipher.init(true, wrappedKeyParam);

     log.info("Using Permutation cipher ...");
     log.info("Plaintext '{}'", new String(plaintext));
     log.info("Key '{}'", wrappedKeyParam.getWrappedKey());

     //Encryption test
     int processedBytes = cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
     log.info("Processed bytes '{}' ", processedBytes);
     log.info("Encrypted Ciphertext {}", new String(ciphertext));
     assertThat(processedBytes).isEqualTo(plaintext.length);
     assertThat(ciphertext).isNotEmpty().isNotIn(plaintext);

     //Decryption test
     //re-initialize cipher for decryption
     cipher.init(false, wrappedKeyParam);
     assertThat(cipher.processBytes(ciphertext, 0, ciphertext.length, decryptedtext, 0)).isEqualTo(ciphertext.length);
     assertThat(decryptedtext).containsExactly(plaintext);
     log.info("Decrypted ciphertext '{}'", new String(decryptedtext));
     */
  }


  @Test
  void canPad() {
    byte[] plaintext = "Hello world !@#@$%^$#^%#&^".getBytes();
    int[] columnOrder = new int[]{4, 2, 1, 3};
    PermutationCrypt cipher = new PermutationCrypt();
    assertThat(cipher.canPad(columnOrder, plaintext)).isTrue();
    assertThat(cipher.padNum(columnOrder, plaintext)).isEqualTo(2);
    assertThat(cipher.canPad(columnOrder, cipher.pad(columnOrder, plaintext))).isFalse();
  }
}