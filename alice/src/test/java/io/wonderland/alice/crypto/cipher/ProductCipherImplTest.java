package io.wonderland.alice.crypto.cipher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.alice.crypto.ProductCipher;
import io.wonderland.alice.crypto.key.GenericSecretKey;
import io.wonderland.alice.crypto.key.secretkey.AffineKey;
import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import io.wonderland.alice.jca.AliceProvider;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

@Slf4j
class ProductCipherImplTest {

  static {
    Security.addProvider(new AliceProvider());
  }

  @Test
  void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
    Cipher caesarCipher = Cipher.getInstance("Caesar");
    Key caesarKey = new CaesarKey(5);

    Cipher otpCipher = Cipher.getInstance("OTP");
    byte[] key = "1qazxsw23edc4rfv5tgb6yhnmju78ik,.l/';".getBytes();
    Key optKey = new GenericSecretKey(key);

    Cipher affineCipher = Cipher.getInstance("Affine");
    Key affineKey = new AffineKey(2, 3, 5);

    ProductCipher productCipher = new ProductCipherImpl();
    assertThatCode(() -> productCipher.init(true, Map.entry(caesarCipher, caesarKey),
        Map.entry(otpCipher, optKey),
        Map.entry(affineCipher, affineKey))).doesNotThrowAnyException();
    assertThatThrownBy(() -> productCipher.init(true, null)).isInstanceOf(
        IllegalArgumentException.class);
    assertThat(productCipher.getCiphers()).hasSize(3)
        .contains(caesarCipher, otpCipher, affineCipher);
  }

  @Test
  void update() {
    byte[] input = "Hello world ->".getBytes();

    ProductCipher productCipher = new ProductCipherImpl();

    assertThatCode(() -> {
      productCipher.update(input);
      productCipher.update(input);
    }).doesNotThrowAnyException();

    assertThat(productCipher.update(input)).hasSize(input.length * 3);

    //Build an array
    byte[] buffer;
    buffer = ArrayUtils.addAll(input, input);
    buffer = ArrayUtils.addAll(buffer, input);
    buffer = ArrayUtils.addAll(buffer, input);
    assertThat(productCipher.update(input)).contains(buffer);

  }

  @Test
  void doFinal()
      throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
    Cipher caesarCipher = Cipher.getInstance("Caesar");
    Key caesarKey = new CaesarKey(5);

    Cipher otpCipher = Cipher.getInstance("OTP");
    byte[] key = "1qazxsw23edc4rfv5tgb6yhnmju78ik,.l/';".getBytes();
    Key optKey = new GenericSecretKey(key);

    Cipher affineCipher = Cipher.getInstance("Affine");
    Key affineKey = new AffineKey(13, 3, 21);

    ProductCipher productCipher = new ProductCipherImpl();
    productCipher.init(true, Map.entry(caesarCipher, caesarKey), Map.entry(otpCipher, optKey),
        Map.entry(affineCipher, affineKey));

    byte[] input = "Hello world@!$!#$#".getBytes();
    productCipher.update(input);

    byte[] ciphertext = productCipher.doFinal();
    assertThat(ciphertext).isNotIn(input);
    log.info("Ciphertext '{}'", new String(ciphertext));

    //reinitialize cipher
    productCipher.init(false, Map.entry(affineCipher, affineKey), Map.entry(otpCipher, optKey),
        Map.entry(caesarCipher, caesarKey));
    byte[] plaintext = productCipher.doFinal(ciphertext);
    assertThat(plaintext).containsExactly(input);
    log.info("Plaintext '{}'", new String(input));
  }

}