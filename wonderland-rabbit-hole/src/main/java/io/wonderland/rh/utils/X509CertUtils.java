package io.wonderland.rh.utils;

import io.wonderland.rh.cert.CertValidity;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.bc.BcX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class X509CertUtils {

  public static final SecureRandom SECURE_RANDOM = new SecureRandom();
  public static final String DEFAULT_SIGN_ALG_RSA = "SHA256WithRSA";
  public static final String DEFAULT_CSP = "BC";

  private X509CertUtils() {
  }

  public static X509Certificate getSelfSignedCert(KeyPair keyPair)
      throws IOException, CertificateException, OperatorCreationException {
    return getSelfSignedCert(keyPair, getProjectSubject(), CertValidity.ofYears(100),
        DEFAULT_SIGN_ALG_RSA);
  }

  public static X509Certificate getSelfSignedCert(final KeyPair keyPair, final X500Name subject,
      final CertValidity validity, final String signatureAlgorithm)
      throws IOException, CertificateException, OperatorCreationException {

    final BigInteger serialNumber = new BigInteger(Long.SIZE, SECURE_RANDOM);

    final PublicKey publicKey = keyPair.getPublic();
    final byte[] publicKeyEncoded = publicKey.getEncoded();
    final var keyPublicInfo = SubjectPublicKeyInfo.getInstance(publicKeyEncoded);

    /*
     * Generate the Subject (Public-) Key Identifier...
     */
    try (final ByteArrayInputStream ist = new ByteArrayInputStream(publicKeyEncoded);
        final ASN1InputStream ais = new ASN1InputStream(ist)) {
      final ASN1Sequence asn1Sequence = (ASN1Sequence) ais.readObject();

      final SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(
          asn1Sequence);
      final SubjectKeyIdentifier subjectPublicKeyId = new BcX509ExtensionUtils().createSubjectKeyIdentifier(
          subjectPublicKeyInfo);

      /*
       * Now build the Certificate, add some Extensions & sign it with our own Private Key...
       */
      final X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(subject,
          serialNumber,
          validity.getNotBefore(), validity.getNotAfter(), subject, keyPublicInfo);
      final ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm).build(
          keyPair.getPrivate());

      /*
       * BasicConstraints instantiated with "CA=true"
       * The BasicConstraints Extension is usually marked "critical=true"
       *
       * The Subject Key Identifier extension identifies the public key certified by this certificate.
       * This extension provides a way of distinguishing public keys if more than one is available for
       * a given subject name.
       */
      final X509CertificateHolder certHolder = certBuilder
          .addExtension(Extension.basicConstraints, true, new BasicConstraints(true))
          .addExtension(Extension.subjectKeyIdentifier, false, subjectPublicKeyId)
          .build(contentSigner);

      return new JcaX509CertificateConverter().setProvider(DEFAULT_CSP).getCertificate(certHolder);
    }
  }

  public static X500Name getProjectSubject() {

    return new X500Name(new RDN[]{new RDN(
        new AttributeTypeAndValue[]{
            new AttributeTypeAndValue(BCStyle.CN, new DERUTF8String("rabbit-hole")),
            new AttributeTypeAndValue(BCStyle.OU, new DERUTF8String("wonderland")),
            new AttributeTypeAndValue(BCStyle.O, new DERUTF8String("wonderland")),
            new AttributeTypeAndValue(BCStyle.L, new DERUTF8String("west")),
            new AttributeTypeAndValue(BCStyle.ST, new DERUTF8String("None")),
            new AttributeTypeAndValue(BCStyle.C, new DERUTF8String("None")),
            new AttributeTypeAndValue(BCStyle.POSTAL_CODE, new DERUTF8String("1001")),
            new AttributeTypeAndValue(BCStyle.STREET, new DERUTF8String("Wonderland Street")),
            new AttributeTypeAndValue(BCStyle.EmailAddress,
                new DERUTF8String("wonderland@email.com")),
            new AttributeTypeAndValue(BCStyle.TELEPHONE_NUMBER, new DERUTF8String("+11122233344"))
        })});
  }

  public static X500Name createSubject(String common, String orgUnit, String org, String locality,
      String state,
      String country, String postalCode, String street, String email, String tel) {
    return new X500Name(new RDN[]{new RDN(
        new AttributeTypeAndValue[]{
            new AttributeTypeAndValue(BCStyle.CN, new DERUTF8String(common)),
            new AttributeTypeAndValue(BCStyle.OU, new DERUTF8String(orgUnit)),
            new AttributeTypeAndValue(BCStyle.O, new DERUTF8String(org)),
            new AttributeTypeAndValue(BCStyle.L, new DERUTF8String(locality)),
            new AttributeTypeAndValue(BCStyle.ST, new DERUTF8String(state)),
            new AttributeTypeAndValue(BCStyle.C, new DERUTF8String(country)),
            new AttributeTypeAndValue(BCStyle.POSTAL_CODE, new DERUTF8String(postalCode)),
            new AttributeTypeAndValue(BCStyle.STREET, new DERUTF8String(street)),
            new AttributeTypeAndValue(BCStyle.EmailAddress, new DERUTF8String(email)),
            new AttributeTypeAndValue(BCStyle.TELEPHONE_NUMBER, new DERUTF8String(tel))})});
  }


}
