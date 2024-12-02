package io.wonderland.crypto.bc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import io.wonderland.crypto.AbstractTest;
import io.wonderland.crypto.DateUtils;
import io.wonderland.crypto.KeyPairUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.jupiter.api.Test;

class CMSUtilsTest extends AbstractTest {


  static X509CertificateHolder getSelfSignedCert()
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

    return X509CertificateUtils.createRootCert(signFields,
        x509RootCertFields);

  }

  @Test
  void createSignedData()
      throws GeneralSecurityException, OperatorCreationException, CertIOException, CMSException {
    String signAlgorithm = "SHA256withECDSA";
    byte[] message = "Hello world".getBytes();
    KeyPair kp = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");
    X509CertificateHolder selfSignedCert = getSelfSignedCert();

    CMSSignedData cmsSignedData = CMSUtils.createSignedData(CSP_NAME, signAlgorithm,
        kp.getPrivate(), selfSignedCert, message, true);
    assertThat(cmsSignedData).isNotNull();


  }

  @Test
  void verifySignedDataEncapsulated()
      throws GeneralSecurityException, OperatorCreationException, IOException, CMSException {
    String signAlgorithm = "SHA256withECDSA";
    byte[] message = "Hello world".getBytes();
    KeyPair kp = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");
    X509CertificateHolder selfSignedCert = getSelfSignedCert();

    CMSSignedData cmsSignedData = CMSUtils.createSignedData(CSP_NAME, signAlgorithm,
        kp.getPrivate(), selfSignedCert, message, true);

    assertThatCode(() -> CMSUtils.verifySignedDataEncapsulated(CSP_NAME,
        cmsSignedData.getEncoded())).doesNotThrowAnyException();

  }


}