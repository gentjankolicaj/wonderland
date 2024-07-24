package io.wonderland.crypto.bc;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.crypto.AbstractTest;
import io.wonderland.crypto.DateUtils;
import io.wonderland.crypto.KeyPairUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.security.spec.MGF1ParameterSpec;
import java.util.Date;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaAlgorithmParametersConverter;
import org.junit.jupiter.api.Test;

class MailUtilsTest extends AbstractTest {

  static AlgorithmIdentifier getAlgId() throws InvalidAlgorithmParameterException {
    JcaAlgorithmParametersConverter paramsConverter = new JcaAlgorithmParametersConverter();
    AlgorithmIdentifier oaepParams = paramsConverter.getAlgorithmIdentifier(
        PKCSObjectIdentifiers.id_RSAES_OAEP,
        new OAEPParameterSpec("SHA-256",
            "MGF1", new MGF1ParameterSpec("SHA-256"),
            PSource.PSpecified.DEFAULT));
    return oaepParams;
  }

  static X509CertificateHolder getSelfSignedCert(KeyPair keyPair, String signAlgorithm)
      throws GeneralSecurityException, OperatorCreationException, CertIOException {

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
  void encryptDecryptEnveloped()
      throws GeneralSecurityException, OperatorCreationException, IOException, MessagingException, SMIMEException, CMSException {
    byte[] message = ("Content-Type: text/plain; name=null; charset=us-ascii\r\n" +
        "Content-Transfer-Encoding: 7bit\r\n" +
        "Content-Disposition: inline; filename=test.txt\r\n" +
        "\r\nHello, world!\r\n".getBytes()).getBytes();

    KeyPair rsaKP = KeyPairUtils.generateKeyPair(CSP_NAME, "RSA");
    X509CertificateHolder selfSignedCertHolder = getSelfSignedCert(rsaKP, "SHA256withRSA");
    X509Certificate selfSignedCert = X509CertificateUtils.toX509Certificate(selfSignedCertHolder);

    MimeBodyPart bodyPart = new MimeBodyPart(new ByteArrayInputStream(message));
    AlgorithmIdentifier algId = getAlgId();

    //encrypt decrypt
    MimeBodyPart encryptedMime = MailUtils.encryptEnveloped(CSP_NAME, algId, selfSignedCert,
        bodyPart);
    BodyPart decryptedBodyPart = MailUtils.decryptEnveloped(CSP_NAME, encryptedMime, selfSignedCert,
        rsaKP.getPrivate());
    assertThat((byte[]) decryptedBodyPart.getContent()).containsExactly(message);
  }

  @Test
  void signAndVerifyMultipart()
      throws GeneralSecurityException, OperatorCreationException, IOException, SMIMEException, MessagingException, CMSException {
    byte[] message = ("Content-Type: text/plain; name=null; charset=us-ascii\r\n" +
        "Content-Transfer-Encoding: 7bit\r\n" +
        "Content-Disposition: inline; filename=test.txt\r\n" +
        "\r\nHello, world!\r\n".getBytes()).getBytes();
    String signAlgorithm = "SHA256withECDSA";
    KeyPair kp = KeyPairUtils.generateKeyPair(CSP_NAME, "EC");
    X509CertificateHolder selfSignedCertHolder = getSelfSignedCert(kp, signAlgorithm);

    MimeBodyPart bodyPart = new MimeBodyPart(new ByteArrayInputStream(message));

    MimeMultipart signedMultipart = MailUtils.signMultipart(CSP_NAME, signAlgorithm,
        kp.getPrivate(), selfSignedCertHolder, bodyPart);
    assertThat(
        MailUtils.verifySignedMultipart(CSP_NAME, selfSignedCertHolder, signedMultipart)).isTrue();
  }

}