package io.wonderland.crypto.bc;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.crypto.AbstractTest;
import io.wonderland.crypto.DateUtils;
import io.wonderland.crypto.KeyPairUtils;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509CRL;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.jupiter.api.Test;

class X509CRLUtilsTest extends AbstractTest {

  @Test
  void createEmptyCRL()
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
        keyPair.getPublic(), serialNumber, notBefore, notAfter, subject);
    PrivateKey caKey = keyPair.getPrivate();
    Date thisUpdate = new Date();
    Date nextUpdate = DateUtils.calcDate(34);

    X509CertificateHolder rootCert = X509CertificateUtils.createRootCert(signFields,
        x509RootCertFields);
    assertThat(rootCert).isNotNull();
    assertThat(rootCert.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(rootCert.getIssuer()).isEqualTo(subject);
    assertThat(rootCert.getSubject()).isEqualTo(subject);
    assertThat(rootCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(rootCert.getNotAfter()).isEqualTo(notAfter);

    X509EmptyCRLFields x509EmptyCRLFields = new X509EmptyCRLFields(rootCert, caKey, thisUpdate,
        nextUpdate);
    X509CRLHolder x509CRLHolder = X509CRLUtils.createEmptyCRL(signFields, x509EmptyCRLFields);

    assertThat(x509CRLHolder).isNotNull();
    assertThat(x509CRLHolder.getIssuer()).isEqualTo(subject);
    assertThat(x509CRLHolder.getThisUpdate()).isBeforeOrEqualTo(thisUpdate);
    assertThat(x509CRLHolder.getNextUpdate()).isBeforeOrEqualTo(nextUpdate);
  }

  @Test
  void createCRLConverter() {
    JcaX509CRLConverter converter = X509CRLUtils.createCRLConverter(CSP_NAME);
    assertThat(converter).isNotNull();
  }

  @Test
  void convertToX509CRL()
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
        keyPair.getPublic(), serialNumber, notBefore, notAfter, subject);
    PrivateKey caKey = keyPair.getPrivate();
    Date thisUpdate = new Date();
    Date nextUpdate = DateUtils.calcDate(34);

    X509CertificateHolder rootCert = X509CertificateUtils.createRootCert(signFields,
        x509RootCertFields);
    assertThat(rootCert).isNotNull();
    assertThat(rootCert.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(rootCert.getIssuer()).isEqualTo(subject);
    assertThat(rootCert.getSubject()).isEqualTo(subject);
    assertThat(rootCert.getNotBefore()).isEqualTo(notBefore);
    assertThat(rootCert.getNotAfter()).isEqualTo(notAfter);

    X509EmptyCRLFields x509EmptyCRLFields = new X509EmptyCRLFields(rootCert, caKey, thisUpdate,
        nextUpdate);
    X509CRLHolder x509CRLHolder = X509CRLUtils.createEmptyCRL(signFields, x509EmptyCRLFields);

    assertThat(x509CRLHolder).isNotNull();
    assertThat(x509CRLHolder.getIssuer()).isEqualTo(subject);
    assertThat(x509CRLHolder.getThisUpdate()).isBeforeOrEqualTo(thisUpdate);
    assertThat(x509CRLHolder.getNextUpdate()).isBeforeOrEqualTo(nextUpdate);

    X509CRL x509CRL = X509CRLUtils.convertToX509CRL(CSP_NAME, x509CRLHolder);
    assertThat(x509CRL).isNotNull();
    assertThat(x509CRL.getSigAlgName()).isEqualToIgnoringCase(signAlgorithm);
    assertThat(x509CRL.getThisUpdate()).isBeforeOrEqualTo(thisUpdate);
    assertThat(x509CRL.getNextUpdate()).isBeforeOrEqualTo(nextUpdate);
  }
}