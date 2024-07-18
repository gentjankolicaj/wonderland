package io.wonderland.crypto.bc;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.crypto.AbstractTest;
import io.wonderland.crypto.DateUtils;
import io.wonderland.crypto.KeyPairUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.jupiter.api.Test;

class X509CertificateUtilsTest extends AbstractTest {


  @Test
  void getCounterSerialNumber() {
    BigInteger counterSerialNumberA = X509CertificateUtils.getCounterSerialNumber();
    BigInteger counterSerialNumberB = X509CertificateUtils.getCounterSerialNumber();
    assertThat(counterSerialNumberB).isEqualTo(counterSerialNumberA.add(BigInteger.valueOf(1)));
  }

  @Test
  void createX500Name() {
    X500Name x500Name = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");
    assertThat(x500Name).isNotNull();

  }

  @Test
  void toIETFName() {
    X500Name x500Name = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    assertThat(X509CertificateUtils.toIETFName(x500Name)).isNotNull();
  }


  @Test
  void createX500Principal() {
    X500Principal x500Principal = X509CertificateUtils.createX500Principal("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");
    assertThat(x500Principal).isNotNull();
  }

  @Test
  void toX509Certificate() throws GeneralSecurityException, OperatorCreationException, IOException {
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertificateUtils.getCounterSerialNumber();

    X500Name issuer = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Name subject = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    Date notBefore = DateUtils.calcDate(0);
    Date notAfter = DateUtils.calcDate(34);

    X509CertificateHolder x509v1CertHolder = X509CertificateUtils.createX509v1CertHolder(CSP_NAME
        , "SHA256withECDSA", keyPair, issuer, serialNumber, notBefore, notAfter, subject);

    assertThat(x509v1CertHolder).isNotNull();
    assertThat(x509v1CertHolder.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(x509v1CertHolder.getIssuer()).isEqualTo(issuer);
    assertThat(x509v1CertHolder.getSubject()).isEqualTo(subject);
    assertThat(x509v1CertHolder.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509v1CertHolder.getNotAfter()).isEqualTo(notAfter);

    X509Certificate x509Cert = X509CertificateUtils.toX509Certificate(x509v1CertHolder);
    assertThat(x509Cert).isNotNull();
    assertThat(x509Cert.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509Cert.getNotAfter()).isEqualTo(notAfter);
  }


  @Test
  void createX509v1CertHolder() throws GeneralSecurityException, OperatorCreationException {
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertificateUtils.getCounterSerialNumber();

    X500Name issuer = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Name subject = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    Date notBefore = DateUtils.calcDate(0);
    Date notAfter = DateUtils.calcDate(34);

    X509CertificateHolder x509v1CertHolder = X509CertificateUtils.createX509v1CertHolder(CSP_NAME
        , "SHA256withECDSA", keyPair, issuer, serialNumber, notBefore, notAfter, subject);

    assertThat(x509v1CertHolder).isNotNull();
    assertThat(x509v1CertHolder.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(x509v1CertHolder.getIssuer()).isEqualTo(issuer);
    assertThat(x509v1CertHolder.getSubject()).isEqualTo(subject);
    assertThat(x509v1CertHolder.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509v1CertHolder.getNotAfter()).isEqualTo(notAfter);

    //second test with X500Principal
    X500Principal issuerP = X509CertificateUtils.createX500Principal("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Principal subjectP = X509CertificateUtils.createX500Principal("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    X509CertificateHolder x509v1CertHolder1 = X509CertificateUtils.createX509v1CertHolder(CSP_NAME
        , "SHA256withECDSA", keyPair, issuerP, serialNumber, notBefore, notAfter, subjectP);
    assertThat(x509v1CertHolder1).isNotNull();
    assertThat(x509v1CertHolder1.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(x509v1CertHolder1.getIssuer()).isEqualTo(issuer);
    assertThat(x509v1CertHolder1.getSubject()).isEqualTo(subject);
    assertThat(x509v1CertHolder1.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509v1CertHolder1.getNotAfter()).isEqualTo(notAfter);

  }

  @Test
  void createX509v3CertHolder() throws GeneralSecurityException, OperatorCreationException {
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertificateUtils.getCounterSerialNumber();

    X500Name issuer = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Name subject = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    Date notBefore = DateUtils.calcDate(0);
    Date notAfter = DateUtils.calcDate(34);

    X509CertificateHolder x509v3CertHolder = X509CertificateUtils.createX509v3CertHolder(CSP_NAME
        , "SHA256withECDSA", keyPair, issuer, serialNumber, notBefore, notAfter, subject);

    assertThat(x509v3CertHolder).isNotNull();
    assertThat(x509v3CertHolder.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(x509v3CertHolder.getIssuer()).isEqualTo(issuer);
    assertThat(x509v3CertHolder.getSubject()).isEqualTo(subject);
    assertThat(x509v3CertHolder.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509v3CertHolder.getNotAfter()).isEqualTo(notAfter);

    //Second test with cert-fields
    X509CertificateHolder x509v3CertHolder1 = X509CertificateUtils.createX509v3CertHolder(CSP_NAME
        , "SHA256withECDSA", keyPair,
        new X509CertFields(serialNumber, issuer, notBefore, notAfter, subject));

    assertThat(x509v3CertHolder1).isNotNull();
    assertThat(x509v3CertHolder1.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(x509v3CertHolder1.getIssuer()).isEqualTo(issuer);
    assertThat(x509v3CertHolder1.getSubject()).isEqualTo(subject);
    assertThat(x509v3CertHolder1.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509v3CertHolder1.getNotAfter()).isEqualTo(notAfter);

  }

  @Test
  void createRootCert()
      throws GeneralSecurityException, OperatorCreationException, CertIOException {
    String signAlgorithm = "SHA256withECDSA";
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertificateUtils.getCounterSerialNumber();

    X500Name subject = X509CertificateUtils.createX500Name("AL", "Albania",
        "TestSign", "Root CA", "TestSign Root");

    Date notBefore = DateUtils.calcDate(0);
    Date notAfter = DateUtils.calcDate(34);

    SignFields signFields = new SignFields(CSP_NAME, signAlgorithm);
    X509RootCertFields x509RootCertFields = new X509RootCertFields(keyPair.getPrivate(),
        keyPair.getPublic(),
        serialNumber, notBefore, notAfter, subject);

    X509CertificateHolder rootCert = X509CertificateUtils.createRootCert(signFields,
        x509RootCertFields);
    assertThat(rootCert).isNotNull();
    assertThat(rootCert.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(rootCert.getIssuer()).isEqualTo(subject);
    assertThat(rootCert.getSubject()).isEqualTo(subject);
    assertThat(rootCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(rootCert.getNotAfter()).isEqualTo(notAfter);
  }

  @Test
  void createRootKeyCert()
      throws GeneralSecurityException, OperatorCreationException, CertIOException {
    String signAlgorithm = "SHA256withECDSA";
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertificateUtils.getCounterSerialNumber();

    X500Name subject = X509CertificateUtils.createX500Name("AL", "Albania",
        "TestSign", "Root CA", "TestSign Root");

    Date notBefore = DateUtils.calcDate(0);
    Date notAfter = DateUtils.calcDate(34);

    X509KeyCert keyCert = X509CertificateUtils.createRootKeyCert(keyPair, CSP_NAME, signAlgorithm,
        serialNumber, notBefore, notAfter, subject);

    assertThat(keyCert).isNotNull();
    assertThat(keyCert.getCert().getSerialNumber()).isEqualTo(serialNumber);
    assertThat(keyCert.getCert().getIssuer()).isEqualTo(subject);
    assertThat(keyCert.getCert().getSubject()).isEqualTo(subject);
    assertThat(keyCert.getCert().getNotBefore()).isEqualTo(notBefore);
    assertThat(keyCert.getCert().getNotAfter()).isEqualTo(notAfter);
  }

  @Test
  void createIntermediateCert()
      throws GeneralSecurityException, OperatorCreationException, CertIOException {
    String signAlgorithm = "SHA256withECDSA";
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertificateUtils.getCounterSerialNumber();

    X500Name issuer = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Name subject = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    Date notBefore = DateUtils.calcDate(0);
    Date notAfter = DateUtils.calcDate(34);

    //Trust certificate test
    X509CertificateHolder trustCert = X509CertificateUtils.createX509v3CertHolder(CSP_NAME
        , signAlgorithm, keyPair, issuer, serialNumber, notBefore, notAfter, subject);

    assertThat(trustCert).isNotNull();
    assertThat(trustCert.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(trustCert.getIssuer()).isEqualTo(issuer);
    assertThat(trustCert.getSubject()).isEqualTo(subject);
    assertThat(trustCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(trustCert.getNotAfter()).isEqualTo(notAfter);

    //Certificate authority test (or CA)
    X500Name caName = X509CertificateUtils.createX500Name("AL", "Albania", "ca-org", "ca-org-unit",
        "ca-org-name");
    KeyPair caKP = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");
    BigInteger caSerialNumber = X509CertificateUtils.getCounterSerialNumber();
    SignFields signFields = new SignFields(CSP_NAME, signAlgorithm);
    X509InterCertFields x509InterCertFields = new X509InterCertFields(trustCert,
        keyPair.getPrivate(), caSerialNumber,
        notBefore, notAfter, caName, caKP.getPublic(), 0);

    X509CertificateHolder caCert = X509CertificateUtils.createIntermediateCert(signFields,
        x509InterCertFields);
    assertThat(caCert).isNotNull();
    assertThat(caCert.getSerialNumber()).isEqualTo(caSerialNumber);
    assertThat(caCert.getIssuer()).isEqualTo(trustCert.getSubject());
    assertThat(caCert.getSubject()).isEqualTo(caName);
    assertThat(caCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(caCert.getNotAfter()).isEqualTo(notAfter);
  }


  @Test
  void createEndEntityCert()
      throws GeneralSecurityException, OperatorCreationException, CertIOException {
    String signAlgorithm = "SHA256withECDSA";
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertificateUtils.getCounterSerialNumber();

    X500Name issuer = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Name subject = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    Date notBefore = DateUtils.calcDate(0);
    Date notAfter = DateUtils.calcDate(34);

    //Trust certificate test
    X509CertificateHolder trustCert = X509CertificateUtils.createX509v3CertHolder(CSP_NAME
        , signAlgorithm, keyPair, issuer, serialNumber, notBefore, notAfter, subject);

    assertThat(trustCert).isNotNull();
    assertThat(trustCert.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(trustCert.getIssuer()).isEqualTo(issuer);
    assertThat(trustCert.getSubject()).isEqualTo(subject);
    assertThat(trustCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(trustCert.getNotAfter()).isEqualTo(notAfter);

    //Certificate authority test (or CA)
    X500Name caName = X509CertificateUtils.createX500Name("AL", "Albania", "ca-org", "ca-org-unit",
        "ca-org-name");
    KeyPair caKP = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");
    BigInteger caSerialNumber = X509CertificateUtils.getCounterSerialNumber();
    SignFields signFields = new SignFields(CSP_NAME, signAlgorithm);
    X509InterCertFields x509InterCertFields = new X509InterCertFields(trustCert,
        keyPair.getPrivate(), caSerialNumber,
        notBefore, notAfter, caName, caKP.getPublic(), 0);

    X509CertificateHolder caCert = X509CertificateUtils.createIntermediateCert(signFields,
        x509InterCertFields);
    assertThat(caCert).isNotNull();
    assertThat(caCert.getSerialNumber()).isEqualTo(caSerialNumber);
    assertThat(caCert.getIssuer()).isEqualTo(trustCert.getSubject());
    assertThat(caCert.getSubject()).isEqualTo(caName);
    assertThat(caCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(caCert.getNotAfter()).isEqualTo(notAfter);

    //End-entity certificate test
    X500Name eeName = X509CertificateUtils.createX500Name("AL", "Albania", "ee-org", "ee-org-unit",
        "ee-org-name");
    KeyPair eeKP = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");
    BigInteger eeSerialNumber = X509CertificateUtils.getCounterSerialNumber();
    X509EndCertFields x509EndCertFields = new X509EndCertFields(caCert, caKP.getPrivate(),
        eeSerialNumber,
        notBefore, notAfter, eeName, eeKP.getPublic());

    X509CertificateHolder eeCert = X509CertificateUtils.createEndEntityCert(signFields,
        x509EndCertFields);
    assertThat(eeCert).isNotNull();
    assertThat(eeCert.getSerialNumber()).isEqualTo(eeSerialNumber);
    assertThat(eeCert.getIssuer()).isEqualTo(caCert.getSubject());
    assertThat(eeCert.getSubject()).isEqualTo(eeName);
    assertThat(eeCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(eeCert.getNotAfter()).isEqualTo(notAfter);

  }

  @Test
  void createSpecialEndEntityCert()
      throws GeneralSecurityException, OperatorCreationException, CertIOException {
    String signAlgorithm = "SHA256withECDSA";
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertificateUtils.getCounterSerialNumber();

    X500Name issuer = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Name subject = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    Date notBefore = DateUtils.calcDate(0);
    Date notAfter = DateUtils.calcDate(34);

    //Trust certificate test
    X509CertificateHolder trustCert = X509CertificateUtils.createX509v3CertHolder(CSP_NAME
        , signAlgorithm, keyPair, issuer, serialNumber, notBefore, notAfter, subject);

    assertThat(trustCert).isNotNull();
    assertThat(trustCert.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(trustCert.getIssuer()).isEqualTo(issuer);
    assertThat(trustCert.getSubject()).isEqualTo(subject);
    assertThat(trustCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(trustCert.getNotAfter()).isEqualTo(notAfter);

    //Certificate authority test (or CA)
    X500Name caName = X509CertificateUtils.createX500Name("AL", "Albania", "ca-org", "ca-org-unit",
        "ca-org-name");
    KeyPair caKP = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");
    BigInteger caSerialNumber = X509CertificateUtils.getCounterSerialNumber();
    SignFields signFields = new SignFields(CSP_NAME, signAlgorithm);
    X509InterCertFields x509InterCertFields = new X509InterCertFields(trustCert,
        keyPair.getPrivate(), caSerialNumber,
        notBefore, notAfter, caName, caKP.getPublic(), 0);

    X509CertificateHolder caCert = X509CertificateUtils.createIntermediateCert(signFields,
        x509InterCertFields);
    assertThat(caCert).isNotNull();
    assertThat(caCert.getSerialNumber()).isEqualTo(caSerialNumber);
    assertThat(caCert.getIssuer()).isEqualTo(trustCert.getSubject());
    assertThat(caCert.getSubject()).isEqualTo(caName);
    assertThat(caCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(caCert.getNotAfter()).isEqualTo(notAfter);

    //End-entity certificate test
    X500Name eeName = X509CertificateUtils.createX500Name("AL", "Albania", "ee-org", "ee-org-unit",
        "ee-org-name");
    KeyPair eeKP = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");
    BigInteger eeSerialNumber = X509CertificateUtils.getCounterSerialNumber();
    X509EndCertFields x509EndCertFields = new X509EndCertFields(caCert, caKP.getPrivate(),
        eeSerialNumber,
        notBefore, notAfter, eeName, eeKP.getPublic());

    X509CertificateHolder eeCert = X509CertificateUtils.createSpecialEndEntityCert(signFields,
        x509EndCertFields,
        KeyPurposeId.id_kp_clientAuth);
    assertThat(eeCert).isNotNull();
    assertThat(eeCert.getSerialNumber()).isEqualTo(eeSerialNumber);
    assertThat(eeCert.getIssuer()).isEqualTo(caCert.getSubject());
    assertThat(eeCert.getSubject()).isEqualTo(eeName);
    assertThat(eeCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(eeCert.getNotAfter()).isEqualTo(notAfter);

  }


  @Test
  void createCertConverter() {
    assertThat(X509CertificateUtils.createCertConverter(CSP_NAME)).isNotNull();
  }

  @Test
  void convertToX509Certificate() throws GeneralSecurityException, OperatorCreationException {
    String signAlgorithm = "SHA256withECDSA";
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertificateUtils.getCounterSerialNumber();

    X500Name issuer = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Name subject = X509CertificateUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    Date notBefore = DateUtils.calcDate(0);
    Date notAfter = DateUtils.calcDate(34);

    //Trust certificate test
    X509CertificateHolder trustCert = X509CertificateUtils.createX509v3CertHolder(CSP_NAME
        , signAlgorithm, keyPair, issuer, serialNumber, notBefore, notAfter, subject);

    assertThat(trustCert).isNotNull();
    assertThat(trustCert.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(trustCert.getIssuer()).isEqualTo(issuer);
    assertThat(trustCert.getSubject()).isEqualTo(subject);
    assertThat(trustCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(trustCert.getNotAfter()).isEqualTo(notAfter);

    //convert from X509CertificateHolder to X509Certificate
    X509Certificate x509Certificate = X509CertificateUtils.convertToX509Certificate(CSP_NAME,
        trustCert);
    assertThat(x509Certificate).isNotNull();
    assertThat(x509Certificate.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(x509Certificate.getSigAlgName()).isEqualToIgnoringCase(signAlgorithm);
    assertThat(x509Certificate.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509Certificate.getNotAfter()).isEqualTo(notAfter);

  }
}