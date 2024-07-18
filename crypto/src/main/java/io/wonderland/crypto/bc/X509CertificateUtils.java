package io.wonderland.crypto.bc;

import io.wonderland.crypto.CSP;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.RoleSyntax;
import org.bouncycastle.asn1.x509.X509AttributeIdentifiers;
import org.bouncycastle.cert.AttributeCertificateHolder;
import org.bouncycastle.cert.AttributeCertificateIssuer;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v2AttributeCertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

@Slf4j
public class X509CertificateUtils {

  private static CertificateFactory certificateFactory;
  private static long serialNumberBase = System.currentTimeMillis();

  static {
    init();
  }


  private static void init() {
    try {
      certificateFactory = CertificateFactory.getInstance("X.509", CSP.BC);
    } catch (Exception e) {
      log.error("Error ", e);
      System.exit(1);
    }
  }


  /**
   * Calculate a serial number using a monotonically increasing value.
   *
   * @return a BigInteger representing the next serial number in the sequence.
   */
  public static synchronized BigInteger getCounterSerialNumber() {
    return BigInteger.valueOf(serialNumberBase++);
  }

  /**
   * Create a X500Name object for issuer/subject data.
   *
   * @param country          country
   * @param state            state
   * @param organization     organization
   * @param organizationUnit organization unit
   * @param commonName       common name
   * @return X500Name object
   */
  public static X500Name createX500Name(String country, String state, String organization,
      String organizationUnit, String commonName) {
    return new X500NameBuilder()
        .addRDN(BCStyle.C, country)
        .addRDN(BCStyle.ST, state)
        .addRDN(BCStyle.O, organization)
        .addRDN(BCStyle.OU, organizationUnit)
        .addRDN(BCStyle.CN, commonName).build();
  }

  /**
   * Create a X500Principal object for issuer/subject data.
   *
   * @param country          country
   * @param state            state
   * @param organization     organization
   * @param organizationUnit organization unit
   * @param commonName       common name
   * @return X500Principal object
   */
  public static X500Principal createX500Principal(String country, String state, String organization,
      String organizationUnit, String commonName) {
    String sb = "C" + "=" + country + ","
        + "ST" + "=" + state + ","
        + "O" + "=" + organization + ","
        + "OU" + "=" + organizationUnit + ","
        + "CN" + "=" + commonName;
    return new X500Principal(sb);

  }

  /**
   * Simple method to convert an X509CertificateHolder to an X509Certificate using the
   * java.security.cert.CertificateFactory class.
   */
  public static X509Certificate toX509Certificate(X509CertificateHolder certHolder)
      throws GeneralSecurityException, IOException {
    return (X509Certificate) certificateFactory.generateCertificate(
        new ByteArrayInputStream(certHolder.getEncoded()));
  }

  /**
   * Convert an X500Name to use the IETF style.
   */
  public static X500Name toIETFName(X500Name name) {
    return X500Name.getInstance(RFC4519Style.INSTANCE, name);
  }

  /**
   * Build a V1 x509 certificate with parameters.
   *
   * @param provider      cryptographic service providers
   * @param signAlgorithm signature algorithm
   * @param keyPair       keypair
   * @param issuer        certificate issuer
   * @param serialNumber  certificate number
   * @param notBefore     certificate validity not before
   * @param notAfter      certificate validity not after
   * @param subject       certificate subject
   * @return X509CertificateHolder object
   * @throws OperatorCreationException at content signer creation
   */
  public static X509CertificateHolder createX509v1CertHolder(String provider, String signAlgorithm,
      KeyPair keyPair, X500Name issuer, BigInteger serialNumber, Date notBefore, Date notAfter,
      X500Name subject) throws OperatorCreationException {
    X509v1CertificateBuilder certBuilder = new JcaX509v1CertificateBuilder(issuer, serialNumber,
        notBefore, notAfter, subject, keyPair.getPublic());
    ContentSigner signer = new JcaContentSignerBuilder(signAlgorithm)
        .setProvider(provider).build(keyPair.getPrivate());
    return certBuilder.build(signer);
  }

  /**
   * Build a V1 x509 certificate with parameters.
   *
   * @param provider      cryptographic service providers
   * @param signAlgorithm signature algorithm
   * @param keyPair       keypair
   * @param issuer        certificate issuer
   * @param serialNumber  certificate number
   * @param notBefore     certificate validity not before
   * @param notAfter      certificate validity not after
   * @param subject       certificate subject
   * @return X509CertificateHolder object
   * @throws OperatorCreationException at content signer creation
   */
  public static X509CertificateHolder createX509v1CertHolder(String provider, String signAlgorithm,
      KeyPair keyPair, X500Principal issuer, BigInteger serialNumber, Date notBefore, Date notAfter,
      X500Principal subject) throws OperatorCreationException {
    X509v1CertificateBuilder certBuilder = new JcaX509v1CertificateBuilder(issuer, serialNumber,
        notBefore, notAfter, subject, keyPair.getPublic());
    ContentSigner signer = new JcaContentSignerBuilder(signAlgorithm)
        .setProvider(provider).build(keyPair.getPrivate());
    return certBuilder.build(signer);
  }

  /**
   * Build a V3 x509 certificate with parameters.
   *
   * @param provider      cryptographic service providers
   * @param signAlgorithm signature algorithm
   * @param keyPair       keypair
   * @param issuer        certificate issuer
   * @param serialNumber  certificate number
   * @param notBefore     certificate validity not before
   * @param notAfter      certificate validity not after
   * @param subject       certificate subject
   * @return X509CertificateHolder object
   * @throws OperatorCreationException at content signer creation
   */
  public static X509CertificateHolder createX509v3CertHolder(String provider, String signAlgorithm,
      KeyPair keyPair, X500Name issuer, BigInteger serialNumber, Date notBefore, Date notAfter,
      X500Name subject) throws OperatorCreationException {
    X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(issuer, serialNumber,
        notBefore, notAfter, subject, keyPair.getPublic());
    ContentSigner signer = new JcaContentSignerBuilder(signAlgorithm)
        .setProvider(provider).build(keyPair.getPrivate());
    return certBuilder.build(signer);
  }

  /**
   * Build a V3 x509 certificate with parameters.
   *
   * @param provider       cryptographic service providers
   * @param signAlgorithm  signature algorithm
   * @param keyPair        keypair
   * @param x509CertFields x509 certificate fields
   * @return X509CertificateHolder object
   * @throws OperatorCreationException at content signer creation
   */
  public static X509CertificateHolder createX509v3CertHolder(String provider, String signAlgorithm,
      KeyPair keyPair, X509CertFields x509CertFields) throws OperatorCreationException {
    X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
        x509CertFields.getIssuer(), x509CertFields.getSerial(), x509CertFields.getNotBefore(),
        x509CertFields.getNotAfter(), x509CertFields.getSubject(), keyPair.getPublic());
    ContentSigner signer = new JcaContentSignerBuilder(signAlgorithm)
        .setProvider(provider).build(keyPair.getPrivate());
    return certBuilder.build(signer);
  }


  /**
   * Build a V3 self-signed root certificate that can be used as a CA certificate.Issuer and subject
   * fields are the same.
   *
   * @param signFields         signing algorithm fields
   * @param x509RootCertFields root certificate signing fields.
   * @return an X509CertificateHolder containing the V3 certificate.
   */
  public static X509CertificateHolder createRootCert(
      SignFields signFields, X509RootCertFields x509RootCertFields)
      throws CertIOException, GeneralSecurityException, OperatorCreationException {

    X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
        x509RootCertFields.getSubject(), x509RootCertFields.getSerial(),
        x509RootCertFields.getNotBefore(), x509RootCertFields.getNotAfter(),
        x509RootCertFields.getSubject(), x509RootCertFields.getPublicKey());

    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

    certBuilder.addExtension(Extension.subjectKeyIdentifier, false,
            extUtils.createSubjectKeyIdentifier(x509RootCertFields.getPublicKey()))
        .addExtension(Extension.keyUsage, true,
            new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

    ContentSigner contentSigner = new JcaContentSignerBuilder(signFields.getAlgorithm())
        .setProvider(signFields.getProvider()).build(x509RootCertFields.getPrivateKey());

    return certBuilder.build(contentSigner);
  }

  public static X509KeyCert createRootKeyCert(KeyPair rootKP, String provider,
      String signAlgorithm, BigInteger serial, Date notBefore, Date notAfter, X500Name subject)
      throws GeneralSecurityException, OperatorCreationException, CertIOException {
    return new X509KeyCert(rootKP, createRootCert(new SignFields(provider, signAlgorithm),
        new X509RootCertFields(rootKP.getPrivate(), rootKP.getPublic(), serial, notBefore, notAfter,
            subject)));
  }


  /**
   * Build a sample V3 intermediate certificate that can be used as a CA certificate.
   *
   * @param signFields          signing algorithm fields
   * @param x509InterCertFields certificate signing fields.
   * @return an X509CertificateHolder containing the V3 certificate.
   */
  public static X509CertificateHolder createIntermediateCert(
      SignFields signFields, X509InterCertFields x509InterCertFields)
      throws CertIOException, GeneralSecurityException, OperatorCreationException {

    X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
        x509InterCertFields.getTrustCert().getSubject(), x509InterCertFields.getSerial(),
        x509InterCertFields.getNotBefore(), x509InterCertFields.getNotAfter(),
        x509InterCertFields.getSubject(), x509InterCertFields.getCertKey());

    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

    certBuilder.addExtension(Extension.authorityKeyIdentifier, false,
            extUtils.createAuthorityKeyIdentifier(x509InterCertFields.getTrustCert()))
        .addExtension(Extension.subjectKeyIdentifier, false,
            extUtils.createSubjectKeyIdentifier(x509InterCertFields.getCertKey()))
        .addExtension(Extension.basicConstraints, true,
            new BasicConstraints(x509InterCertFields.getFollowingCACerts()))
        .addExtension(Extension.keyUsage, true,
            new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

    ContentSigner contentSigner = new JcaContentSignerBuilder(signFields.getAlgorithm())
        .setProvider(signFields.getProvider()).build(x509InterCertFields.getTrustKey());

    return certBuilder.build(contentSigner);
  }


  /**
   * Build a V3 end-entity certificate that can be used as a x509 certificate.
   *
   * @param signFields        signing algorithm fields
   * @param x509EndCertFields end-entity certificate fields.
   * @return an X509CertificateHolder containing the V3 certificate.
   */
  public static X509CertificateHolder createEndEntityCert(
      SignFields signFields, X509EndCertFields x509EndCertFields)
      throws CertIOException, GeneralSecurityException, OperatorCreationException {

    X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
        x509EndCertFields.getSignerCert().getSubject(), x509EndCertFields.getSerial(),
        x509EndCertFields.getNotBefore(), x509EndCertFields.getNotAfter()
        , x509EndCertFields.getSubject(), x509EndCertFields.getCertKey());

    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

    certBuilder.addExtension(Extension.authorityKeyIdentifier,
            false, extUtils.createAuthorityKeyIdentifier(x509EndCertFields.getSignerCert()))
        .addExtension(Extension.subjectKeyIdentifier,
            false, extUtils.createSubjectKeyIdentifier(x509EndCertFields.getCertKey()))
        .addExtension(Extension.basicConstraints,
            true, new BasicConstraints(false))
        .addExtension(Extension.keyUsage,
            true, new KeyUsage(KeyUsage.digitalSignature));

    ContentSigner contentSigner = new JcaContentSignerBuilder(signFields.getAlgorithm())
        .setProvider(signFields.getProvider()).build(x509EndCertFields.getSignerKey());

    return certBuilder.build(contentSigner);
  }


  /**
   * Create a special purpose end entity cert which is associated with a particular key purpose.
   *
   * @param signFields        signing algorithm fields
   * @param x509EndCertFields end-entity certificate fields.
   * @param keyPurpose        the specific KeyPurposeId to associate with this certificate's public
   *                          key.
   * @return an X509CertificateHolder containing the V3 certificate.
   */
  public static X509CertificateHolder createSpecialEndEntityCert(
      SignFields signFields, X509EndCertFields x509EndCertFields, KeyPurposeId keyPurpose)
      throws OperatorCreationException, CertIOException, GeneralSecurityException {

    X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
        x509EndCertFields.getSignerCert().getSubject(), x509EndCertFields.getSerial(),
        x509EndCertFields.getNotBefore(), x509EndCertFields.getNotAfter(),
        x509EndCertFields.getSubject(), x509EndCertFields.getCertKey());

    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

    certBuilder.addExtension(Extension.authorityKeyIdentifier, false,
            extUtils.createAuthorityKeyIdentifier(x509EndCertFields.getSignerCert()))
        .addExtension(Extension.subjectKeyIdentifier, false,
            extUtils.createSubjectKeyIdentifier(x509EndCertFields.getCertKey()))
        .addExtension(Extension.basicConstraints, true,
            new BasicConstraints(false))
        .addExtension(Extension.extendedKeyUsage, true,
            new ExtendedKeyUsage(keyPurpose));

    ContentSigner contentSigner = new JcaContentSignerBuilder(signFields.getAlgorithm())
        .setProvider(signFields.getProvider()).build(x509EndCertFields.getSignerKey());

    return certBuilder.build(contentSigner);
  }


  /**
   * Extract the DER encoded value octets of an extension from a JCA X509Certificate.
   *
   * @param x509Certificate the certificate of interest.
   * @param extensionOID    the OID associated with the extension of interest.
   * @return the DER encoding inside the extension, null if extension missing.
   */
  public static byte[] extractExtensionValue(X509Certificate x509Certificate,
      ASN1ObjectIdentifier extensionOID) {
    byte[] octString = x509Certificate.getExtensionValue(extensionOID.getId());
    if (octString == null) {
      return null;
    }
    return ASN1OctetString.getInstance(octString).getOctets();
  }

  public static X509AttributeCertificateHolder createAttributeCert(SignFields signFields,
      X509AttrCertFields x509AttrCertFields) throws OperatorCreationException {

    X509v2AttributeCertificateBuilder attributeBuilder =
        new X509v2AttributeCertificateBuilder(
            new AttributeCertificateHolder(x509AttrCertFields.getHolderCert()),
            new AttributeCertificateIssuer(x509AttrCertFields.getIssuerCert().getSubject()),
            x509AttrCertFields.getSerial(),
            x509AttrCertFields.getNotBefore(), x509AttrCertFields.getNotAfter());

    GeneralName roleName = new GeneralName(
        GeneralName.uniformResourceIdentifier, x509AttrCertFields.getHolderRoleUri());

    attributeBuilder.addAttribute(
        X509AttributeIdentifiers.id_at_role, new RoleSyntax(roleName));

    ContentSigner signer = new JcaContentSignerBuilder(signFields.getAlgorithm())
        .setProvider(signFields.getProvider()).build(x509AttrCertFields.getIssuerKey());

    return attributeBuilder.build(signer);
  }


  public static JcaX509CertificateConverter createCertConverter(String provider) {
    return new JcaX509CertificateConverter().setProvider(provider);
  }

  public static X509Certificate convertToX509Certificate(String provider,
      X509CertificateHolder x509CertificateHolder)
      throws CertificateException {
    return new JcaX509CertificateConverter().setProvider(provider)
        .getCertificate(x509CertificateHolder);
  }


}
