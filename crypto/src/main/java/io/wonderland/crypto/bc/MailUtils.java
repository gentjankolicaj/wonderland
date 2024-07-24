package io.wonderland.crypto.bc;


import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute;
import org.bouncycastle.asn1.smime.SMIMECapability;
import org.bouncycastle.asn1.smime.SMIMECapabilityVector;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;
import org.bouncycastle.mail.smime.SMIMEUtil;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.CollectionStore;
import org.bouncycastle.util.Store;


public final class MailUtils {


  private MailUtils() {
  }


  /**
   * Create an application/pkcs7-mime from the body part in message.
   *
   * @param provider       cryptographic service provider
   * @param encryptionCert public key certificate associated with the intended recipient.
   * @param message        byte[] representing the body part to be encrypted.
   * @return a MimeBodyPart containing an application/pkcs7-mime MIME object.
   */
  public static MimeBodyPart encryptEnveloped(String provider,
      AlgorithmIdentifier algId, X509Certificate encryptionCert, MimeBodyPart message)
      throws GeneralSecurityException, CMSException, SMIMEException {
    SMIMEEnvelopedGenerator envGen = new SMIMEEnvelopedGenerator();

    envGen.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(encryptionCert, algId)
        .setProvider(provider));

    return envGen.generate(message, new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES256_CBC)
        .setProvider(provider)
        .build());
  }

  /**
   * Extract a MIME body part from an enveloped message.
   *
   * @param provider         cryptographic service provider
   * @param encryptedMessage the enveloped message.
   * @param recipientCert    the certificate associated with the recipient key.
   * @param recipientKey     the private key to recover the body part with.
   */
  public static MimeBodyPart decryptEnveloped(String provider, MimeBodyPart encryptedMessage,
      X509Certificate recipientCert, PrivateKey recipientKey)
      throws CMSException, MessagingException, SMIMEException {
    SMIMEEnveloped envData = new SMIMEEnveloped(encryptedMessage);

    RecipientInformationStore recipients = envData.getRecipientInfos();

    RecipientInformation recipient = recipients.get(new JceKeyTransRecipientId(recipientCert));

    return SMIMEUtil.toMimeBodyPart(recipient.getContent(
        new JceKeyTransEnvelopedRecipient(recipientKey).setProvider(provider)));
  }

  /**
   * Create a multipart/signed for the body part in message.
   *
   * @param signingKey  private key to generate the signature with.
   * @param signingCert public key certificate associated with the signing key.
   * @param message     the body part to be signed.
   * @return a MimeMultipart of the type multipart/signed MIME object.
   */
  public static MimeMultipart signMultipart(String provider, String signAlgorithm,
      PrivateKey signingKey, X509CertificateHolder signingCert, MimeBodyPart message)
      throws OperatorCreationException, SMIMEException {
    List<X509CertificateHolder> certList = List.of(signingCert);

    Store<X509CertificateHolder> certs = new CollectionStore<>(certList);

    SMIMESignedGenerator signedGen = new SMIMESignedGenerator();
    signedGen.addSignerInfoGenerator(
        new JcaSimpleSignerInfoGeneratorBuilder()
            .setProvider(provider)
            .setSignedAttributeGenerator(generateSMIMECapabilities())
            .build(signAlgorithm, signingKey, signingCert));
    signedGen.addCertificates(certs);

    return signedGen.generate(message);
  }

  /**
   * Verify a MimeMultipart containing a multipart/signed object.
   *
   * @param signedMessage the multipart/signed.
   * @param signerCert    the certificate of one of the signers of signedMessage.
   * @return true if the multipart/signed verified for signerCert, false otherwise.
   */
  public static boolean verifySignedMultipart(String provider, X509CertificateHolder signerCert
      , MimeMultipart signedMessage)
      throws GeneralSecurityException, OperatorCreationException, CMSException, MessagingException {
    SMIMESigned signedData = new SMIMESigned(signedMessage);

    SignerInformationStore signerStore = signedData.getSignerInfos();
    SignerInformation signer = signerStore.get(
        new SignerId(signerCert.getIssuer(), signerCert.getSerialNumber()));

    return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider(provider)
        .build(signerCert));
  }

  /**
   * Basic method to provide some S/MIME capability attributes for anyone responding to our
   * message.
   *
   * @return an AttributeTable with the additional attributes.
   */
  public static AttributeTable generateSMIMECapabilities() {
    ASN1EncodableVector signedAttrs = new ASN1EncodableVector();

    SMIMECapabilityVector caps = new SMIMECapabilityVector();
    caps.addCapability(SMIMECapability.aES128_CBC);
    caps.addCapability(SMIMECapability.aES192_CBC);
    caps.addCapability(SMIMECapability.aES256_CBC);
    caps.addCapability(SMIMECapability.preferSignedData);

    signedAttrs.add(new SMIMECapabilitiesAttribute(caps));
    return new AttributeTable(signedAttrs);
  }


}
