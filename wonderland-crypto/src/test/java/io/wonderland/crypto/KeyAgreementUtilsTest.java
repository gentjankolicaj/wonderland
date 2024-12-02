package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.spec.ECGenParameterSpec;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.spec.DHUParameterSpec;
import org.bouncycastle.jcajce.spec.MQVParameterSpec;
import org.junit.jupiter.api.Test;

@Slf4j
public class KeyAgreementUtilsTest extends AbstractTest {

  @Test
  void keyAgreementGenerateSecret() throws GeneralSecurityException {
    KeyPair aDHKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);
    KeyPair bDHKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);

    assertThat(
        KeyAgreementUtils.generateSecret(CSP_NAME, "DH", aDHKeyPair.getPrivate(),
            bDHKeyPair.getPublic()))
        .isNotNull().hasSize(32);

    byte[] secret = KeyAgreementUtils.generateSecret(CSP_NAME, "DH", aDHKeyPair.getPrivate(),
        bDHKeyPair.getPublic());
    log.debug("generateSecret() , key agreement secret {}", new String(secret));
  }

  @Test
  void keyAgreementGenerateSecretKey() throws GeneralSecurityException {
    KeyPair aDHKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);
    KeyPair bDHKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);
    byte[] agreedSecret = KeyAgreementUtils.generateSecret(CSP_NAME, "DH",
        aDHKeyPair.getPrivate(), bDHKeyPair.getPublic());
    SecretKey secretKey = KeyAgreementUtils.generateSecretKey(CSP_NAME, "DH",
        "AES", aDHKeyPair.getPrivate(), bDHKeyPair.getPublic());
    assertThat(secretKey.getEncoded()).containsExactly(agreedSecret);
  }

  @Test
  void keyAgreementGenerateSecretKeyWithKeyMaterial() throws GeneralSecurityException {
    byte[] keyMaterial = "Hello world for AES Key".getBytes();
    KeyPair aECKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC",
        new ECGenParameterSpec("P-256"));
    KeyPair bECKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC",
        new ECGenParameterSpec("P-256"));

    //generate first secret key with key material
    SecretKey aKey = KeyAgreementUtils.generateSecretKey(CSP_NAME, "ECCDHwithSHA256KDF",
        "AES", aECKeyPair.getPrivate(), bECKeyPair.getPublic(), keyMaterial);

    //generate second secret key with key material
    SecretKey bKey = KeyAgreementUtils.generateSecretKey(CSP_NAME, "ECCDHwithSHA256KDF",
        "AES", bECKeyPair.getPrivate(), aECKeyPair.getPublic(), keyMaterial);
    assertThat(aKey.getEncoded()).containsExactly(bKey.getEncoded());
    assertThat(aKey.getAlgorithm()).isEqualTo(bKey.getAlgorithm());
  }

  /**
   * Basic Unified Diffie-Hellman example showing use of two key pairs per party in the protocol,
   * with one set being regarded as ephemeral.
   */
  @Test
  void DHUGenerateSecretKey() throws GeneralSecurityException {
    byte[] keyMaterial = "Hello world for AES Key".getBytes();
    KeyPair aDHKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);
    KeyPair bDHKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);

    //Ephemeral key pairs
    KeyPair aDHKeyPairEph = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);
    KeyPair bDHKeyPairEph = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);

    //generate a secret key with key material
    DHUParameterSpec aDHUParameterSpec = new DHUParameterSpec(aDHKeyPairEph.getPublic(),
        aDHKeyPairEph.getPrivate(), bDHKeyPairEph.getPublic(), keyMaterial);

    SecretKey aKey = KeyAgreementUtils.generateSecretKey(CSP_NAME, "DHUwithSHA256KDF",
        "AES", aDHKeyPair.getPrivate(), bDHKeyPair.getPublic(), aDHUParameterSpec);

    //generate b secret key with key material
    DHUParameterSpec bDHUParameterSpec = new DHUParameterSpec(bDHKeyPairEph.getPublic(),
        bDHKeyPairEph.getPrivate(), aDHKeyPairEph.getPublic(), keyMaterial);

    SecretKey bKey = KeyAgreementUtils.generateSecretKey(CSP_NAME, "DHUwithSHA256KDF",
        "AES", bDHKeyPair.getPrivate(), aDHKeyPair.getPublic(), bDHUParameterSpec);

    assertThat(aKey.getEncoded()).containsExactly(bKey.getEncoded());
    assertThat(aKey.getAlgorithm()).isEqualTo(bKey.getAlgorithm());
  }

  /**
   * Basic Diffie-Hellman MQV example showing use of two key pairs per party in the protocol, with
   * one set being regarded as ephemeral.
   */
  @Test
  void MQVGenerateSecretKey() throws GeneralSecurityException {
    byte[] keyMaterial = "Hello world for AES Key".getBytes();
    KeyPair aDHKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);
    KeyPair bDHKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);

    //Ephemeral key pairs
    KeyPair aDHKeyPairEph = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);
    KeyPair bDHKeyPairEph = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);

    //generate first secret key with key material
    MQVParameterSpec aDHUParameterSpec = new MQVParameterSpec(aDHKeyPairEph.getPublic(),
        aDHKeyPairEph.getPrivate(),
        bDHKeyPairEph.getPublic(), keyMaterial);

    SecretKey aKey = KeyAgreementUtils.generateSecretKey(CSP_NAME, "MQVwithSHA256KDF",
        "AES", aDHKeyPair.getPrivate(), bDHKeyPair.getPublic(), aDHUParameterSpec);

    //generate second secret key with key material
    MQVParameterSpec bDHUParameterSpec = new MQVParameterSpec(bDHKeyPairEph.getPublic(),
        bDHKeyPairEph.getPrivate(),
        aDHKeyPairEph.getPublic(), keyMaterial);
    SecretKey bKey = KeyAgreementUtils.generateSecretKey(CSP_NAME, "MQVwithSHA256KDF",
        "AES", bDHKeyPair.getPrivate(), aDHKeyPair.getPublic(), bDHUParameterSpec);

    assertThat(aKey.getEncoded()).containsExactly(bKey.getEncoded());
    assertThat(aKey.getAlgorithm()).isEqualTo(bKey.getAlgorithm());
  }


}
