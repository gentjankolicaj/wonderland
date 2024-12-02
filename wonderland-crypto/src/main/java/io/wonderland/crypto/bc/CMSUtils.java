package io.wonderland.crypto.bc;

import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSAttributeTableGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.SignerInfoGeneratorBuilder;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.CollectionStore;
import org.bouncycastle.util.Store;

public final class CMSUtils {


  private CMSUtils() {
  }


  /**
   * Create a SignedData structure.
   *
   * @param provider      cryptographic service provider
   * @param signAlgorithm signing algorithm
   * @param signingKey    the key to generate the signature with.
   * @param signingCert   the cert to verify signingKey's signature with.
   * @param message       the raw message data
   * @param encapsulate   true if the data being signed should wrap.
   * @return a CMSSignedData holding the SignedData created.
   */
  public static CMSSignedData createSignedData(String provider, String signAlgorithm,
      PrivateKey signingKey, X509CertificateHolder signingCert, byte[] message, boolean encapsulate)
      throws CMSException, OperatorCreationException {
    ContentSigner contentSigner = new JcaContentSignerBuilder(signAlgorithm).setProvider(provider)
        .build(signingKey);

    DigestCalculatorProvider digestCalcProvider = new JcaDigestCalculatorProviderBuilder()
        .setProvider(provider).build();

    SignerInfoGenerator signerInfoGenerator = new SignerInfoGeneratorBuilder(digestCalcProvider)
        .build(contentSigner, signingCert);

    Store<X509CertificateHolder> certs = new CollectionStore<>(
        Collections.singletonList(signingCert));

    CMSSignedDataGenerator dataGen = new CMSSignedDataGenerator();
    dataGen.addSignerInfoGenerator(signerInfoGenerator);
    dataGen.addCertificates(certs);

    return dataGen.generate(new CMSProcessableByteArray(message), encapsulate);
  }


  /**
   * Verify the passed in encoding of a CMS SignedData, assumes encapsulated data.
   *
   * @param provider          cryptographic service provider
   * @param encodedSignedData the BER encoding of the SignedData
   */
  public static void verifySignedDataEncapsulated(String provider, byte[] encodedSignedData)
      throws CMSException, OperatorCreationException, CertificateException {
    CMSSignedData signedData = new CMSSignedData(encodedSignedData);
    SignerInformationStore signers = signedData.getSignerInfos();
    Store<X509CertificateHolder> certs = signedData.getCertificates();

    for (SignerInformation signerInfo : signers) {
      Collection<X509CertificateHolder> certCollection = certs.getMatches(signerInfo.getSID());
      X509CertificateHolder cert = certCollection.iterator().next();
      signerInfo.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider(provider).build(cert));
    }
  }


  /**
   * Create a SignedData structure changing the signed attributes it is created with from the
   * default ones.
   *
   * @param provider      cryptographic service providers
   * @param signAlgorithm signing algorithm
   * @param signingKey    the key to generate the signature with.
   * @param signingCert   the cert to verify signingKey's signature with.
   * @param message       the raw message data
   * @param encapsulate   true if the data being signed should be wrapped.
   * @return a CMSSignedData holding the SignedData created.
   */
  public static CMSSignedData createSignedDataWithAttributesEdit(String provider,
      String signAlgorithm, PrivateKey signingKey, X509CertificateHolder signingCert,
      byte[] message, boolean encapsulate, CMSAttributeTableGenerator cmsAttributeTable)
      throws CMSException, OperatorCreationException {
    ContentSigner contentSigner = new JcaContentSignerBuilder(signAlgorithm).setProvider(provider)
        .build(signingKey);

    DigestCalculatorProvider digestCalcProvider =
        new JcaDigestCalculatorProviderBuilder().setProvider(provider).build();

    SignerInfoGenerator signerInfoGenerator = new SignerInfoGeneratorBuilder(digestCalcProvider)
        .setSignedAttributeGenerator(cmsAttributeTable)
        .build(contentSigner, signingCert);

    Store<X509CertificateHolder> certs = new CollectionStore<>(
        Collections.singletonList(signingCert));

    CMSSignedDataGenerator dataGen = new CMSSignedDataGenerator();
    dataGen.addSignerInfoGenerator(signerInfoGenerator);
    dataGen.addCertificates(certs);

    CMSTypedData typedMsg = new CMSProcessableByteArray(message);
    return dataGen.generate(typedMsg, encapsulate);
  }


  /**
   * Update a SignerInfo to include a counter signature. The resulting CMSSignedData will also
   * contain the certificate for the counter signer.
   *
   * @param provider        cryptographic service providers
   * @param signAlgorithm   signing algorithm
   * @param original        the SignedData with the SignerInfo to be countersigned.
   * @param counterSignKey  the key being used for countersigning.
   * @param counterSignCert the certificate associated with counterSignKey.
   * @return an updated SignedData with the counter signature.
   */
  public static CMSSignedData addCounterSignature(String provider, String signAlgorithm,
      CMSSignedData original, PrivateKey counterSignKey, X509CertificateHolder counterSignCert)
      throws CMSException, OperatorCreationException {
    SignerInfoGenerator signerInfoGenerator =
        new JcaSimpleSignerInfoGeneratorBuilder()
            .setProvider(provider).build(signAlgorithm, counterSignKey, counterSignCert);

    CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

    gen.addSignerInfoGenerator(signerInfoGenerator);

    SignerInformationStore signers = original.getSignerInfos();
    SignerInformation signerInfo = signers.iterator().next();

    signerInfo = SignerInformation.addCounterSigners(
        signerInfo, gen.generateCounterSigners(signerInfo));

    Collection<X509CertificateHolder> originalCerts = original.getCertificates()
        .getMatches(null);

    Set<X509CertificateHolder> totalCerts = new HashSet<>(originalCerts);
    totalCerts.add(counterSignCert);

    CMSSignedData counterSigned = CMSSignedData.replaceSigners(
        original, new SignerInformationStore(signerInfo));

    counterSigned = CMSSignedData.replaceCertificatesAndCRLs(
        counterSigned, new CollectionStore<>(totalCerts), null, null);

    return counterSigned;
  }


  /**
   * Verify the passed in encoding of a CMS SignedData, assumes a detached signature with msg
   * representing the raw data that was signed.
   *
   * @param provider          cryptographic service provider
   * @param encodedSignedData the BER encoding of the SignedData
   * @param message           the data that was used to create the SignerInfo in the SignedData
   */
  public static void verifySignedDataDetached(String provider, byte[] encodedSignedData,
      byte[] message)
      throws CMSException, CertificateException, OperatorCreationException {
    CMSSignedData signedData = new CMSSignedData(new CMSProcessableByteArray(message),
        encodedSignedData);
    SignerInformationStore signers = signedData.getSignerInfos();
    Store<X509CertificateHolder> certs = signedData.getCertificates();

    for (SignerInformation signerInfo : signers) {
      Collection<X509CertificateHolder> certCollection = certs.getMatches(signerInfo.getSID());
      X509CertificateHolder cert = certCollection.iterator().next();

      signerInfo.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider(provider).build(cert));
    }
  }

  /**
   * Verify all SignerInfos from a SignedData structure, including any counter signatures.
   *
   * @param provider   cryptographic service provider
   * @param signedData BER encoding of the SignedData structure.
   */
  public static void verifyAllSigners(String provider, CMSSignedData signedData)
      throws CMSException {
    final Store<X509CertificateHolder> certs = signedData.getCertificates();

    signedData.verifySignatures(signerId -> {
      try {
        X509CertificateHolder cert = (X509CertificateHolder) certs
            .getMatches(signerId).iterator().next();

        return new JcaSimpleSignerInfoVerifierBuilder()
            .setProvider(provider).build(cert);
      } catch (CertificateException e) {
        throw new OperatorCreationException(
            "verifier provider failed: " + e.getMessage(), e);
      }
    });
  }


}
