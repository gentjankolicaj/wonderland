package io.wonderland.crypto.bc;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRLException;
import java.security.cert.X509CRL;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

@Slf4j
public class X509CRLUtils {

  private X509CRLUtils() {
  }


  public static X509CRLHolder createEmptyCRL(SignFields signFields,
      X509EmptyCRLFields x509EmptyCRLFields)
      throws NoSuchAlgorithmException, CertIOException, OperatorCreationException {
    X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(x509EmptyCRLFields.getCaCert().getSubject(),
        x509EmptyCRLFields.getUpdate());

    crlBuilder.setNextUpdate(x509EmptyCRLFields.getNextUpdate());

    // add extensions to CRL
    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

    crlBuilder.addExtension(Extension.authorityKeyIdentifier, false,
        extUtils.createAuthorityKeyIdentifier(x509EmptyCRLFields.getCaCert()));

    ContentSigner signer = new JcaContentSignerBuilder(signFields.getAlgorithm())
        .setProvider(signFields.getProvider()).build(x509EmptyCRLFields.getCaKey());

    return crlBuilder.build(signer);
  }

  public static JcaX509CRLConverter createCRLConverter(String provider) {
    return new JcaX509CRLConverter().setProvider(provider);
  }

  public static X509CRL convertToX509CRL(String provider, X509CRLHolder x509CRLHolder)
      throws CRLException {
    return new JcaX509CRLConverter().setProvider(provider).getCRL(x509CRLHolder);
  }


  /**
   * Create a CRL containing a single revocation.
   *
   * @param signFields          signing algorithm fields
   * @param x509CreateCRLFields certificate revocation list fields.
   * @return an X509CRLHolder representing the revocation list for the CA.
   */
  public X509CRLHolder createCRL(SignFields signFields, X509CreateCRLFields x509CreateCRLFields)
      throws IOException, GeneralSecurityException, OperatorCreationException {

    X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(x509CreateCRLFields.getCaCert().getSubject(),
        x509CreateCRLFields.getUpdate());

    crlBuilder.setNextUpdate(x509CreateCRLFields.getNextUpdate());

    // add revocation
    CRLReason crlReason = CRLReason.lookup(CRLReason.privilegeWithdrawn);
    ExtensionsGenerator extGen = new ExtensionsGenerator();
    extGen.addExtension(Extension.reasonCode, false, crlReason);

    crlBuilder.addCRLEntry(x509CreateCRLFields.getCertToRevoke().getSerialNumber(),
        x509CreateCRLFields.getRevocationDate(), extGen.generate());

    // add extensions to CRL
    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

    crlBuilder.addExtension(Extension.authorityKeyIdentifier, false,
        extUtils.createAuthorityKeyIdentifier(x509CreateCRLFields.getCaCert()));

    ContentSigner signer = new JcaContentSignerBuilder(signFields.getAlgorithm())
        .setProvider(signFields.getProvider()).build(x509CreateCRLFields.getCaKey());

    return crlBuilder.build(signer);
  }

  /**
   * Create an updated CRL from a previous one and add a new revocation.
   *
   * @param signFields          signing algorithm fields
   * @param x509UpdateCRLFields certificate revocation list fields.
   * @return an X509CRLHolder representing the updated revocation list for the CA.
   */
  public X509CRLHolder updateCRL(SignFields signFields, X509UpdateCRLFields x509UpdateCRLFields)
      throws IOException, GeneralSecurityException, OperatorCreationException {
    X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(x509UpdateCRLFields.getCaCert().getIssuer(),
        x509UpdateCRLFields.getUpdate());

    crlBuilder.setNextUpdate(x509UpdateCRLFields.getNextUpdate());

    // add new revocation
    CRLReason crlReason = CRLReason.lookup(CRLReason.privilegeWithdrawn);
    ExtensionsGenerator extGen = new ExtensionsGenerator();
    extGen.addExtension(Extension.reasonCode, false, crlReason);

    crlBuilder.addCRLEntry(x509UpdateCRLFields.getCertToRevoke().getSerialNumber(),
        x509UpdateCRLFields.getRevocationDate(), extGen.generate());

    // add previous revocations
    crlBuilder.addCRL(x509UpdateCRLFields.getPreviousCaCRL());

    // add extensions to CRL
    JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

    crlBuilder.addExtension(Extension.authorityKeyIdentifier, false,
        extUtils.createAuthorityKeyIdentifier(x509UpdateCRLFields.getCaCert()));

    ContentSigner signer = new JcaContentSignerBuilder(signFields.getAlgorithm())
        .setProvider(signFields.getProvider()).build(x509UpdateCRLFields.getCaKey());

    return crlBuilder.build(signer);
  }


}
