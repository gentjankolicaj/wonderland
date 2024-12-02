package io.wonderland.crypto;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

class SealedObjectUtilsTest extends AbstractTest {

  @Test
  void serialize() throws GeneralSecurityException, IOException {
    String algorithm = "AES";
    SecretKey aesKey = SecretKeyUtils.generateSecretKey(CSP_NAME, algorithm);
    Cipher cipher = Cipher.getInstance(algorithm, CSP_NAME);

    //init cipher
    cipher.init(Cipher.ENCRYPT_MODE, aesKey);

    //serialize & encrypt
    byte[] encryptedSerialization = SealedObjectUtils.serialize(
        new SealedObject(new UserTest("john", "doe"), cipher));
    assertThat(encryptedSerialization).isNotNull();

  }

  @Test
  void getSealedObject() throws GeneralSecurityException, IOException, ClassNotFoundException {
    String algorithm = "AES";
    SecretKey aesKey = SecretKeyUtils.generateSecretKey(CSP_NAME, algorithm);
    Cipher cipher = Cipher.getInstance(algorithm, CSP_NAME);

    //init cipher
    cipher.init(Cipher.ENCRYPT_MODE, aesKey);

    //serialize & encrypt
    UserTest userTest = new UserTest("john", "doe");

    byte[] encryptedSerialization = SealedObjectUtils.serialize(new SealedObject(userTest, cipher));
    assertThat(encryptedSerialization).isNotNull();

    //deserialize
    SealedObject sealedObject = SealedObjectUtils.deserialize(encryptedSerialization);

    Object objectSealed = sealedObject.getObject(aesKey);
    UserTest userTestSealed = (UserTest) objectSealed;
    assertThat(userTestSealed.name).isEqualTo(userTest.name);
    assertThat(userTestSealed.surname).isEqualTo(userTest.surname);

  }

  @AllArgsConstructor
  @Getter
  @Setter
  static class UserTest implements Serializable {

    private final String name;
    private final String surname;
  }
}