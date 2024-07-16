package io.wonderland.crypto.bc;

import io.wonderland.crypto.CSP;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.auth.x500.X500Principal;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

@Slf4j
public class X509CertUtils {

  private static final Map<String, X509CertificateHolder> certMap = new ConcurrentHashMap<>();
  private static CertificateFactory certificateFactory;
  private static long serialNumberBase = System.currentTimeMillis();

  static {
    Security.addProvider(new BouncyCastleProvider());
    init();
  }


  private static void init() {
    try {
      certificateFactory = CertificateFactory.getInstance("X.509", CSP.BC);
    } catch (Exception e) {
      log.error("Error on BC init().", e);
      System.exit(1);
    }
  }


  /**
   * Calculate a date in seconds (suitable for the PKIX profile - RFC 5280)
   *
   * @param hoursInFuture hours ahead of now, may be negative.
   * @return a Date set to now + (hoursInFuture * 60 * 60) seconds
   */
  public static Date calcDate(int hoursInFuture) {
    long secs = System.currentTimeMillis() / 1000;
    return new Date((secs + ((long) hoursInFuture * 60 * 60)) * 1000);
  }

  /**
   * Calculate a serial number using a monotonically increasing value.
   *
   * @return a BigInteger representing the next serial number in the sequence.
   */
  public static synchronized BigInteger counterSerialNumber() {
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
        x509CertFields.getIssuer(),
        x509CertFields.getSerial(), x509CertFields.getNotBefore(), x509CertFields.getNotAfter(),
        x509CertFields.getSubject(), keyPair.getPublic());
    ContentSigner signer = new JcaContentSignerBuilder(signAlgorithm)
        .setProvider(provider).build(keyPair.getPrivate());
    return certBuilder.build(signer);
  }

}
