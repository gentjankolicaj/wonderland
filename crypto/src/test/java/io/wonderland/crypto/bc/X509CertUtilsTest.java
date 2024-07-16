package io.wonderland.crypto.bc;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.crypto.AbstractTest;
import io.wonderland.crypto.KeyPairUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.jupiter.api.Test;

class X509CertUtilsTest extends AbstractTest {

  @Test
  void calcDate() {
    Date tenHours = X509CertUtils.calcDate(10);
    assertThat(tenHours).isAfter(new Date());
  }

  @Test
  void counterSerialNumber() {
    BigInteger counterSerialNumberA = X509CertUtils.counterSerialNumber();
    BigInteger counterSerialNumberB = X509CertUtils.counterSerialNumber();
    assertThat(counterSerialNumberB).isEqualTo(counterSerialNumberA.add(BigInteger.valueOf(1)));
  }

  @Test
  void createX500Name() {
    X500Name x500Name = X509CertUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");
    assertThat(x500Name).isNotNull();

  }

  @Test
  void toIETFName() {
    X500Name x500Name = X509CertUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    assertThat(X509CertUtils.toIETFName(x500Name)).isNotNull();
  }


  @Test
  void createX500Principal() {
    X500Principal x500Principal = X509CertUtils.createX500Principal("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");
    assertThat(x500Principal).isNotNull();
  }

  @Test
  void toX509Certificate() throws GeneralSecurityException, OperatorCreationException, IOException {
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertUtils.counterSerialNumber();

    X500Name issuer = X509CertUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Name subject = X509CertUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    Date notBefore = X509CertUtils.calcDate(0);
    Date notAfter = X509CertUtils.calcDate(34);

    X509CertificateHolder x509v1CertHolder = X509CertUtils.createX509v1CertHolder(CSP_NAME
        , "SHA256withECDSA", keyPair, issuer, serialNumber, notBefore, notAfter, subject);

    assertThat(x509v1CertHolder).isNotNull();
    assertThat(x509v1CertHolder.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(x509v1CertHolder.getIssuer()).isEqualTo(issuer);
    assertThat(x509v1CertHolder.getSubject()).isEqualTo(subject);
    assertThat(x509v1CertHolder.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509v1CertHolder.getNotAfter()).isEqualTo(notAfter);

    X509Certificate x509Cert = X509CertUtils.toX509Certificate(x509v1CertHolder);
    assertThat(x509Cert).isNotNull();
    assertThat(x509Cert.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509Cert.getNotAfter()).isEqualTo(notAfter);
  }


  @Test
  void createX509v1CertHolder() throws GeneralSecurityException, OperatorCreationException {
    KeyPair keyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");

    BigInteger serialNumber = X509CertUtils.counterSerialNumber();

    X500Name issuer = X509CertUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Name subject = X509CertUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    Date notBefore = X509CertUtils.calcDate(0);
    Date notAfter = X509CertUtils.calcDate(34);

    X509CertificateHolder x509v1CertHolder = X509CertUtils.createX509v1CertHolder(CSP_NAME
        , "SHA256withECDSA", keyPair, issuer, serialNumber, notBefore, notAfter, subject);

    assertThat(x509v1CertHolder).isNotNull();
    assertThat(x509v1CertHolder.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(x509v1CertHolder.getIssuer()).isEqualTo(issuer);
    assertThat(x509v1CertHolder.getSubject()).isEqualTo(subject);
    assertThat(x509v1CertHolder.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509v1CertHolder.getNotAfter()).isEqualTo(notAfter);

    //second test with X500Principal
    X500Principal issuerP = X509CertUtils.createX500Principal("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Principal subjectP = X509CertUtils.createX500Principal("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    X509CertificateHolder x509v1CertHolder1 = X509CertUtils.createX509v1CertHolder(CSP_NAME
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

    BigInteger serialNumber = X509CertUtils.counterSerialNumber();

    X500Name issuer = X509CertUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto-bc", "crypto-bc");
    X500Name subject = X509CertUtils.createX500Name("AL", "Albania",
        "wonderland", "wonderland-crypto", "crypto");

    Date notBefore = X509CertUtils.calcDate(0);
    Date notAfter = X509CertUtils.calcDate(34);

    X509CertificateHolder x509v3CertHolder = X509CertUtils.createX509v3CertHolder(CSP_NAME
        , "SHA256withECDSA", keyPair, issuer, serialNumber, notBefore, notAfter, subject);

    assertThat(x509v3CertHolder).isNotNull();
    assertThat(x509v3CertHolder.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(x509v3CertHolder.getIssuer()).isEqualTo(issuer);
    assertThat(x509v3CertHolder.getSubject()).isEqualTo(subject);
    assertThat(x509v3CertHolder.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509v3CertHolder.getNotAfter()).isEqualTo(notAfter);

    //Second test with cert-fields
    X509CertificateHolder x509v3CertHolder1 = X509CertUtils.createX509v3CertHolder(CSP_NAME
        , "SHA256withECDSA", keyPair,
        new X509CertFields(serialNumber, issuer, notBefore, notAfter, subject));

    assertThat(x509v3CertHolder1).isNotNull();
    assertThat(x509v3CertHolder1.getSerialNumber()).isEqualTo(serialNumber);
    assertThat(x509v3CertHolder1.getIssuer()).isEqualTo(issuer);
    assertThat(x509v3CertHolder1.getSubject()).isEqualTo(subject);
    assertThat(x509v3CertHolder1.getNotBefore()).isEqualTo(notBefore);
    assertThat(x509v3CertHolder1.getNotAfter()).isEqualTo(notAfter);

  }


}