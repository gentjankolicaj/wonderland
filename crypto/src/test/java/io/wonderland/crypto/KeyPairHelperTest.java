package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.spec.DHParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class KeyPairHelperTest extends AbstractTest {


  @Test
  void constructors() throws GeneralSecurityException {
    KeyPairHelper keyPairHelper = new KeyPairHelper("RSA");
    KeyPair keyPair = keyPairHelper.generateKeyPair(1024);
    assertThat(keyPair).isNotNull();

    //test case
    KeyPairHelper keyPairHelper1 = new KeyPairHelper(CSP_NAME, "RSA");
    KeyPair keyPair1 = keyPairHelper1.generateKeyPair(1024);
    assertThat(keyPair1).isNotNull();
  }


  @Test
  void RSAGenerateKeyPair() throws GeneralSecurityException {
    KeyPairHelper keyPairHelper = new KeyPairHelper(CSP_NAME, "RSA");
    KeyPair keyPair0 = keyPairHelper.generateKeyPair(2048);
    assertThat(keyPair0).isNotNull();
    assertThat(keyPair0.getPrivate()).isNotNull();
    assertThat(keyPair0.getPublic()).isNotNull();

    KeyPair keyPair1 = keyPairHelper.generateKeyPair(1028, new SecureRandom());
    assertThat(keyPair1).isNotNull();
    assertThat(keyPair1.getPrivate()).isNotNull();
    assertThat(keyPair1.getPublic()).isNotNull();

  }

  @Test
  void DSAGenerateKeyPair() throws GeneralSecurityException {
    KeyPairHelper keyPairHelper = new KeyPairHelper(CSP_NAME, "DSA");
    KeyPair dsaKeyPair = keyPairHelper.generateKeyPair(2048);
    assertThat(dsaKeyPair).isNotNull();
    assertThat(dsaKeyPair.getPrivate()).isNotNull();
    assertThat(dsaKeyPair.getPublic()).isNotNull();
  }

  @Test
  void ECSM2GenerateKeyPair() throws GeneralSecurityException {
    KeyPairHelper keyPairHelper = new KeyPairHelper(CSP_NAME, "EC");
    KeyPair ecKeyPair = keyPairHelper.generateKeyPair(new ECGenParameterSpec("sm2p256v1"));
    assertThat(ecKeyPair).isNotNull();
    assertThat(ecKeyPair.getPrivate()).isNotNull();
    assertThat(ecKeyPair.getPublic()).isNotNull();
  }

  @Test
  void ED448GenerateKeyPair() throws GeneralSecurityException {
    KeyPairHelper keyPairHelper = new KeyPairHelper(CSP_NAME, "ED448");
    KeyPair dsaKeyPair = keyPairHelper.generateKeyPair();
    assertThat(dsaKeyPair).isNotNull();
    assertThat(dsaKeyPair.getPrivate()).isNotNull();
    assertThat(dsaKeyPair.getPublic()).isNotNull();
  }

  @Test
  void DSTU4145GenerateKeyPair() throws GeneralSecurityException {
    KeyPairHelper keyPairHelper = new KeyPairHelper(CSP_NAME, "DSTU4145");
    KeyPair dstu4145KeyPair = keyPairHelper.generateKeyPair(
        new ECGenParameterSpec("1.2.804.2.1.1.1.1.3.1.1.2.3"));
    assertThat(dstu4145KeyPair).isNotNull();
    assertThat(dstu4145KeyPair.getPrivate()).isNotNull();
    assertThat(dstu4145KeyPair.getPublic()).isNotNull();
  }

  @Test
  @Disabled("disable because of performance")
  void DHGenerateKeyPair() throws GeneralSecurityException {
    KeyPairHelper keyPairHelper = new KeyPairHelper(CSP_NAME, "DH");
    KeyPair dhKeyPair0 = keyPairHelper.generateKeyPair(256);
    assertThat(dhKeyPair0).isNotNull();
    assertThat(dhKeyPair0.getPrivate()).isNotNull();
    assertThat(dhKeyPair0.getPublic()).isNotNull();

    //second key pair with algorithm parameter specs
    AlgorithmParameters dhAlgorithmParams = AlgorithmParameterUtils.generateAlgParams(
        CSP_NAME, "DH");
    KeyPair dhKeyPair1 = keyPairHelper.generateKeyPair(
        dhAlgorithmParams.getParameterSpec(DHParameterSpec.class));
    assertThat(dhKeyPair1).isNotNull();
    assertThat(dhKeyPair1.getPrivate()).isNotNull();
    assertThat(dhKeyPair1.getPublic()).isNotNull();

    // with spec & secure random
    DHParameterSpec spec = dhAlgorithmParams.getParameterSpec(DHParameterSpec.class);
    KeyPair dhKeyPair2 = keyPairHelper.generateKeyPair(spec, new SecureRandom());
    assertThat(dhKeyPair2).isNotNull();
    assertThat(dhKeyPair2.getPrivate()).isNotNull();
    assertThat(dhKeyPair2.getPublic()).isNotNull();
  }


  @Test
  void createPrivateKey() throws GeneralSecurityException {
    PKCS8EncodedKeySpec privateKeyEncodedSpec = new PKCS8EncodedKeySpec(
        Base64.decode("MIIEpAIBAAKCAQEAw9w9gsya8eQyIDoG8JZhFBPPdD92pkhKsKZX4lXOPJJ3eCpA\n"
            + "0jCRzJ4ooS0F/oizp/7ND82Aq/rZf9AK/Pem2cCL0hr5PmPO0Z3tsb6cVl7tg5u+\n"
            + "J5Tj1rXVnPNsU1OlPqa6UVwKyhny6AgvvHMbL6M76UL/YM14EpnExNEeT4TR/Ija\n"
            + "F7owy0I7ORwUreNb2DdZFAPdnzurcQj3zaVWfhmcKgPfobdwNjFC7O/sUJcim4Uj\n"
            + "R3y1BnOSnsgfPd1f87EoetZVZ/igE2erT/EWQeoI9/+2ufn3CWhgnxS2burFMwEi\n"
            + "O0On+lVOGLj+0gcgyRh2z7qjxcrcH/B2wjljTwIDAQABAoIBAFmIqZnMfJxNS9jN\n"
            + "jfSXWeN6tuAWTt/uti4QrKYrwW6RKgoFjsJHL69RMZOUaGQWC8KlSQqLT+HOd3Tl\n"
            + "HtDLSTvLuF8gs4WgzJ+oSUtyrjcRiBQcsw2XE5xIXVE1OfTRjP2Z7BxbLhd7Sz5k\n"
            + "16WXHPtm7HFSjjmrU9N09a1fRzLj4MkvlqHdE5zyVWLgYp/KHj45Y17+zwyzsbwB\n"
            + "8eioPGrEnMR+DKmcibGydngdWjHlfAQRqJkkC6uBhHXoEpmjJyG0mNmr8GUW49Rt\n"
            + "pMSJdnRLle9JPkeXMwuko/JoV1M6YrBaVfzTz2+oO3mnPhKHIcPkbjTKA9sOaD+V\n"
            + "Q07AnBkCgYEA6hKHUqW81ItjmhntPOkEwFw0UCIkaREwQt0deksoupENC+KQo40c\n"
            + "oe+EpvRxLF8haDIBiDs+aINg/cHPuWdOxcG4N0AIiQ3HwPRITcz0RvBmMF+W9UNB\n"
            + "TshmeLpFVO5ZcoR5M0xaKrQfg5Bz/PhdfsimwG1JGnspSxGJildBQDUCgYEA1jVR\n"
            + "iZ7Rvhb04uQWbxu0x30BmCOLIfrkotTqfcCblYjF6tr21/hT2prBfem2ubqWKEDp\n"
            + "NWx4xiVBW2/jgDxzKpB7ih1gbhnAI07cBTbq+footij44Sz7eUq5znjTBfjDp1+k\n"
            + "qtP/O/GTGf5LsRfiumOU7PN8ERjG+42FlX1DzfMCgYEA4G+pr1ZZa9bHRwArGGc5\n"
            + "dhQy2M8T6GZhxwrq89LTF6hzQP0ZwKhSVvcpU0g4p9oDVzvzeiOMIHwwaMAII/bp\n"
            + "cfbgYqGUTY2YBex005x8cPSalzFgtoSpPxgqIQJB7kCoJYTeDZDdN+sD+Itum5Wt\n"
            + "WB6evQ1MtgZ3vpHvNmWZnC0CgYBkJ1XSVLGYgT9Kfn6GwJuL0kTWj3fUEWypPYfN\n"
            + "+CpGhkaTgoF7hR4fzc++QXIv8K+YbpEba3YknvKp/+yM3rayJg+9CfM2R0/wskRp\n"
            + "I75F1tMGKK4FCnUhxvCNOyzfU+qW7T8eqDRkIJU4yA835AUcRMcy6r0NeVo/73GP\n"
            + "7ZuwRQKBgQDUeQaatVcT/HRcfFcQv0Khk5Wq5R4CHlkIALeIlyMG11k4G9+AU+J8\n"
            + "8NQfFMs0ch+HgIAym6KDXTTWwSlwpp92FdhW0DVOCMAR0AbT0/o1/o/v9YT9re86\n"
            + "ixbJo5dE0MkKkswVFLw4QCfKIGzFUQh4OsVbi6BoSqxd9raPAsEEbg=="));
    KeyPairHelper keyPairHelper = new KeyPairHelper(CSP_NAME, "RSA");
    assertThat(keyPairHelper.generatePrivateKey(privateKeyEncodedSpec)).isNotNull();
  }

  @Test
  void createPublicKey() throws GeneralSecurityException {
    X509EncodedKeySpec publicKeyEncodedSpec = new X509EncodedKeySpec(
        Base64.decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw9w9gsya8eQyIDoG8JZh\n"
            + "FBPPdD92pkhKsKZX4lXOPJJ3eCpA0jCRzJ4ooS0F/oizp/7ND82Aq/rZf9AK/Pem\n"
            + "2cCL0hr5PmPO0Z3tsb6cVl7tg5u+J5Tj1rXVnPNsU1OlPqa6UVwKyhny6AgvvHMb\n"
            + "L6M76UL/YM14EpnExNEeT4TR/IjaF7owy0I7ORwUreNb2DdZFAPdnzurcQj3zaVW\n"
            + "fhmcKgPfobdwNjFC7O/sUJcim4UjR3y1BnOSnsgfPd1f87EoetZVZ/igE2erT/EW\n"
            + "QeoI9/+2ufn3CWhgnxS2burFMwEiO0On+lVOGLj+0gcgyRh2z7qjxcrcH/B2wjlj\n"
            + "TwIDAQAB")
    );
    KeyPairHelper keyPairHelper = new KeyPairHelper(CSP_NAME, "RSA");
    assertThat(keyPairHelper.generatePublicKey(publicKeyEncodedSpec)).isNotNull();
  }


}