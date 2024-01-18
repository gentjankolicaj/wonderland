package io.wonderland.bc.cert;

import io.wonderland.bc.Constants;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Slf4j
public class BCX509CertificateManager {

  private static final Map<String, X509CertificateHolder> certMap = new ConcurrentHashMap<>();
  private static CertificateFactory x509CertificateFactory;

  static {
    Security.addProvider(new BouncyCastleProvider());
    init();
  }


  private static void init() {
    try {
      x509CertificateFactory = CertificateFactory.getInstance("X.509", Constants.BC_CSP);
    } catch (Exception e) {
      log.error("Error on BC init().", e);
      System.exit(1);
    }
  }


}
/*

  public static X509Certificate getX509Certificate(X509CertificateHolder x509CertificateHolder)
      throws GeneralSecurityException, IOException {
     return (X509Certificate) x509CertificateFactory.generateCertificate(new ByteArrayInputStream(x509CertificateHolder.getEncoded()));
  }

  public static X500Name toIETFName(X500Name x500Name){
    return X500Name.getInstance(RFC4519Style.INSTANCE,x500Name);
  }

  public static BCX509CertificateKeyPair createX509KeyCertPair(KeyPair keyPair,String signatureAlgorithm) throws OperatorCreationException {
    X509CertificateHolder x509CertificateHolder=createX509CertificateV1(keyPair,signatureAlgorithm);
    return new BCX509CertificateKeyPair(keyPair,x509CertificateHolder);
  }

  public static X500Name createX500Name(String country,String state,String locality,String organization,String commonName){
    return new X500NameBuilder(BCStyle.INSTANCE)
        .addRDN(BCStyle.C, country)
        .addRDN(BCStyle.ST, state)
        .addRDN(BCStyle.L, locality)
        .addRDN(BCStyle.O, organization)
        .addRDN(BCStyle.CN, commonName)
        .build();
  }



  *//**
   * Build a sample self-signed V1 certificate to use as a trust anchor, or
   * root certificate.
   *
   * @param keyPair the key pair to use for signing and providing the public key.
   * @param signatureAlgorithm the signature algorithm to sign the certificate with.
   * @return an X509CertificateHolder containing the V1 certificate.
   *//*
  public static X509CertificateHolder createX509CertificateV1(KeyPair keyPair, String signatureAlgorithm,
      BCX509CertificateFields certFields)
      throws OperatorCreationException {
    X509v1CertificateBuilder certificateBuilder = new JcaX509v1CertificateBuilder(
        certFields.getIssuer(),certFields.getSerial(),certFields.getNotBefore(),
        certFields.getNotAfter(),certFields.getSubject(), keyPair.getPublic());

    ContentSigner signer = new JcaContentSignerBuilder(signatureAlgorithm).setProvider(Constants.BC_SECURITY_PROVIDER)
        .build(keyPair.getPrivate());

    return certificateBuilder.build(signer);
  }

  public static BCX509CertificateKeyPair createInterCert(BCX509CertificateKeyPair BCX509CertificateKeyPair)
      throws GeneralSecurityException, OperatorCreationException, CertIOException {
    PrivateKey trustAnchorKey = BCX509CertificateKeyPair.getKeyPair().getPrivate();
    X509CertificateHolder trustAnchorCert = BCX509CertificateKeyPair.getCert();

    KeyPair ecKeyPair=KeyUtils.generateKeyPair("EC");

    X509CertificateHolder caCert = createIntermediateCertificate(trustAnchorCert, trustAnchorKey,
        "SHA256withECDSA", ecKeyPair.getPublic(), 0);
    return new BCX509CertificateKeyPair(ecKeyPair, caCert);
  }


  *//**
   * Build a sample V3 intermediate certificate that can be used as a CA
   * certificate.
   *
   * @param signerCert certificate carrying the public key that will later
   *                   be used to verify this certificate's signature.
   * @param signerKey private key used to generate the signature in the
   *                  certificate.
   * @param signatureAlgorithm the signature algorithm to sign the certificate with.
   * @param certKey public key to be installed in the certificate.
   * @param followingCACerts
   * @param certFields 
   * @return an X509CertificateHolder containing the V3 certificate.
   *//*
  public static X509CertificateHolder createIntermediateCertificate(X509CertificateHolder signerCert, PrivateKey signerKey,
      String signatureAlgorithm, PublicKey certKey, int followingCACerts, BCX509CertificateFields certFields)
      throws CertIOException, GeneralSecurityException, OperatorCreationException {

    X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
        signerCert.getSubject(), certFields.getSerial(),certFields.getNotBefore(),
        certFields.getNotAfter(), certFields.getSubject(), certKey);

    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

    certificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(signerCert))
        .addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(certKey))
        .addExtension(Extension.basicConstraints, true, new BasicConstraints(followingCACerts))
        .addExtension(Extension.keyUsage, true,
            new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

    ContentSigner signer = new JcaContentSignerBuilder(signatureAlgorithm).setProvider(Constants.BC_SECURITY_PROVIDER).build(signerKey);

    return certificateBuilder.build(signer);
  }

  public static BCX509CertificateKeyPair createEndEntityPair(BCX509CertificateKeyPair caPair,String signatureAlgorithm)
      throws GeneralSecurityException, OperatorCreationException, CertIOException {
    PrivateKey caPrivateKey = caPair.getKeyPair().getPrivate();
    X509CertificateHolder caCert = caPair.getCert();

    KeyPair ecKeyPair=KeyUtils.generateKeyPair("EC");

    X509CertificateHolder eeCert = createEndEntityCert(caCert, caPrivateKey, signatureAlgorithm, ecKeyPair.getPublic());
    return new BCX509CertificateKeyPair(ecKeyPair, eeCert);
  }

  *//**
   * Create a general end-entity certificate for use in verifying digital
   * signatures.
   *
   * @param signerCert certificate carrying the public key that will later
   *                   be used to verify this certificate's signature.
   * @param signerKey private key used to generate the signature in the
   *                  certificate.
   * @param signatureAlgorithm the signature algorithm to sign the certificate with.
   * @param certKey public key to be installed in the certificate.
   * @param certFields
   * @return an X509CertificateHolder containing the V3 certificate.
   *//*
  public static X509CertificateHolder createEndEntityCert(X509CertificateHolder signerCert, PrivateKey signerKey,
      String signatureAlgorithm, PublicKey certKey, BCX509CertificateFields certFields) throws CertIOException, GeneralSecurityException, OperatorCreationException {

    X509v3CertificateBuilder   certificateBuilder = new JcaX509v3CertificateBuilder(
        signerCert.getSubject(), certFields.getSerial(),certFields.getNotBefore(),certFields.getNotAfter(),certFields.getSubject(), certKey);

    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

    certificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(signerCert))
        .addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(certKey))
        .addExtension(Extension.basicConstraints, true, new BasicConstraints(false))
        .addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature));

    ContentSigner signer = new JcaContentSignerBuilder(signatureAlgorithm).setProvider(Constants.BC_SECURITY_PROVIDER).build(signerKey);
    return certificateBuilder.build(signer);
  }

  public static BCX509CertificateKeyPair createEndEntitySpecCert(BCX509CertificateKeyPair caPair)
      throws GeneralSecurityException, OperatorCreationException, CertIOException
  {
    PrivateKey caPrivateKey = caPair.getKeyPair().getPrivate();
    X509CertificateHolder caCert = caPair.getCert();

    KeyPair ecKeyPair=KeyUtils.generateKeyPair("EC");

    X509CertificateHolder specEECert = createSpecialPurposeEndEntity(caCert, caPrivateKey,
            "SHA256withECDSA", ecKeyPair.getPublic(), KeyPurposeId.id_kp_timeStamping);
    return new BCX509CertificateKeyPair(ecKeyPair, specEECert);
  }


  *//**
   * Create a special purpose end entity cert which is associated with a
   * particular key purpose.
   *
   * @param signerCert certificate carrying the public key that will later
   *                   be used to verify this certificate's signature.
   * @param signerKey private key used to generate the signature in the
   *                  certificate.
   * @param sigAlg the signature algorithm to sign the certificate with.
   * @param certKey public key to be installed in the certificate.
   * @param keyPurpose the specific KeyPurposeId to associate with this
   *                   certificate's public key.
   * @return an X509CertificateHolder containing the V3 certificate.
   *//*
  public static X509CertificateHolder createSpecialPurposeEndEntity(
      X509CertificateHolder signerCert, PrivateKey signerKey,
      String sigAlg, PublicKey certKey, KeyPurposeId keyPurpose)
      throws OperatorCreationException, CertIOException,
      GeneralSecurityException
  {
    X500NameBuilder x500NameBld = new X500NameBuilder(BCStyle.INSTANCE)
        .addRDN(BCStyle.C, "AU")
        .addRDN(BCStyle.ST, "Victoria")
        .addRDN(BCStyle.L, "Melbourne")
        .addRDN(BCStyle.O, "The Legion of the Bouncy Castle")
        .addRDN(BCStyle.CN, "Demo End-Entity Certificate");

    X500Name subject = x500NameBld.build();

    X509v3CertificateBuilder   certificateBuilder = new JcaX509v3CertificateBuilder(signerCert.getSubject(),
        calculateSerialNumber(), calculateDate(0), calculateDate(24 * 31), subject, certKey);

    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

    certificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(signerCert))
        .addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(certKey))
        .addExtension(Extension.basicConstraints, true, new BasicConstraints(false))
        .addExtension(Extension.extendedKeyUsage, true, new ExtendedKeyUsage(keyPurpose));

    ContentSigner signer = new JcaContentSignerBuilder(sigAlg)
        .setProvider("BC").build(signerKey);

    return certificateBuilder.build(signer);
  }
  *//**
   * Extract the DER encoded value octets of an extension from a JCA
   * X509Certificate.
   *
   * @param cert the certificate of interest.
   * @param extensionOID the OID associated with the extension of interest.
   * @return the DER encoding inside the extension, null if extension missing.
   *//*
  public static byte[] extractExtensionValue(X509Certificate cert, ASN1ObjectIdentifier extensionOID) {
    byte[] octString = cert.getExtensionValue(extensionOID.getId());

    if (octString == null) {
      return null;
    }
    return ASN1OctetString.getInstance(octString).getOctets();
  }

  public static X509AttributeCertificateHolder createAttrCertSample(PrivateKey issuerSigningKey,
      X509CertificateHolder issuerCert, X509CertificateHolder holderCert) throws OperatorCreationException {
    X509AttributeCertificateHolder attrCert = createAttributeCertificate(issuerCert, issuerSigningKey,
        "SHA256withECDSA", holderCert, "id://DAU123456789");
    return attrCert;
  }

  public static X509AttributeCertificateHolder createAttributeCertificate(X509CertificateHolder issuerCert
      , PrivateKey issuerKey, String sigAlg, X509CertificateHolder holderCert, String holderRoleUri)
      throws OperatorCreationException {
    X509v2AttributeCertificateBuilder acBldr =
        new X509v2AttributeCertificateBuilder(new AttributeCertificateHolder(holderCert),
            new AttributeCertificateIssuer(issuerCert.getSubject()), calculateSerialNumber(),
            calculateDate(0), calculateDate(24 * 7));

    GeneralName roleName = new GeneralName(GeneralName.uniformResourceIdentifier, holderRoleUri);
    acBldr.addAttribute(X509AttributeIdentifiers.id_at_role, new RoleSyntax(roleName));
    ContentSigner signer = new JcaContentSignerBuilder(sigAlg).setProvider("BC").build(issuerKey);
    return acBldr.build(signer);
  }

}*/
