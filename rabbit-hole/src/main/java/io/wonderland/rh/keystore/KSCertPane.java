package io.wonderland.rh.keystore;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.utils.BaseUtils;
import io.wonderland.rh.utils.GuiUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

@Slf4j
public class KSCertPane extends TitledPane {

  private final VBox container = new VBox();
  private KeyStore keyStore;

  public KSCertPane(String title) {
    this.setText(title);
  }

  public KSCertPane(String title, KeyStore keyStore) {
    this.keyStore = keyStore;
    this.setText(title);
    this.setContent(container);
    this.build();
  }

  private void build() {
    try {
      Enumeration<String> aliases = keyStore.aliases();
      while (aliases.hasMoreElements()) {
        String alias = aliases.nextElement();
        if (keyStore.isCertificateEntry(alias)) {
          CertEntryBox certEntryBox = new CertEntryBox(alias, keyStore.getCertificate(alias));
          container.getChildren().add(certEntryBox);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    this.container.setSpacing(GlobalConstants.SPACING);
    this.setContent(container);
  }

  private static class CertEntryBox extends TitledPane {

    private final VBox container = new VBox();
    private final HBox controlContainer = new HBox();
    private final Label copyCertLbl = new Label();
    private final Label textLbl = new Label();
    private final Label rawLbl = new Label();
    private final Certificate certificate;
    private final String certAlias;

    CertEntryBox(String certAlias, Certificate certificate) {
      this.certAlias = certAlias;
      this.certificate = certificate;
      this.setText(String.format("cert-alias : [ %s ]", certAlias));
      this.build();
    }

    private static VBox getRawCertificateContainer(Certificate certificate) {
      if (Objects.isNull(certificate)) {
        return new VBox();
      } else {
        VBox certContainer = new VBox();
        certContainer.getChildren()
            .addAll(getCertRaw(certificate), getCertTypeInfo(certificate),
                getCertPKInfo(certificate));
        certContainer.setSpacing(GlobalConstants.SPACING);
        return certContainer;
      }
    }

    private static HBox getCertRaw(Certificate certificate) {
      TextField certTF = new TextField();
      try {
        certTF.setEditable(false);
        certTF.setText(BaseUtils.getBase10(certificate.getEncoded(), ' '));
      } catch (Exception e) {
        log.error(e.getMessage());
      }
      HBox.setHgrow(certTF, Priority.ALWAYS);
      return new HBox(GuiUtils.getTitle("Cert-raw"), certTF);
    }

    private static HBox getCertTypeInfo(Certificate certificate) {
      return new HBox(GuiUtils.getTitle("Cert-type:"),
          GuiUtils.getTitle(certificate.getType(), Font.font("arial")));
    }

    private static HBox getCertPKInfo(Certificate certificate) {
      TextField pkTF = new TextField();
      pkTF.setEditable(false);
      pkTF.setText(BaseUtils.getBase10(certificate.getPublicKey().getEncoded(), ' '));
      HBox.setHgrow(pkTF, Priority.ALWAYS);
      return new HBox(GuiUtils.getTitle("Cert-PK:"), pkTF);
    }

    private void build() {
      this.copyCertLbl.setOnMouseReleased(new CopyCertificateEvent());
      this.copyCertLbl.setGraphic(
          GuiUtils.getIconClasspath("/icons/copy-encoding/icons8-copy-24.png"));
      this.copyCertLbl.setTooltip(new Tooltip("Copy certificate"));
      this.rawLbl.setGraphic(GuiUtils.getIconClasspath("/icons/icons8-raw-24.png"));
      this.rawLbl.setOnMouseReleased(new RawCertificateEvent());
      this.textLbl.setGraphic(GuiUtils.getIconClasspath("/icons/icons8-txt-24.png"));
      this.textLbl.setOnMouseReleased(new TextCertificateEvent());
      this.controlContainer.getChildren().addAll(rawLbl, textLbl, copyCertLbl);
      this.container.getChildren()
          .addAll(controlContainer, getRawCertificateContainer(certificate));
      this.container.setSpacing(GlobalConstants.SPACING);
      this.setContent(container);
    }

    private class CopyCertificateEvent implements EventHandler<Event> {

      @Override
      public void handle(Event event) {
        //todo: to debug & fix
        try {
          if (Objects.isNull(certificate)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.showAndWait();
          } else {
            Path path = writeTmpFile();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putFiles(List.of(path.toFile()));
            clipboard.setContent(content);
          }
        } catch (Exception e) {
          ExceptionDialog ed = new ExceptionDialog(e);
          ed.showAndWait();
        }
      }

      private Path writeTmpFile() throws CertificateEncodingException, IOException {
        return Files.write(Path.of(System.getProperty("user.dir"), certAlias),
            certificate.getEncoded());
      }
    }

    private class RawCertificateEvent implements EventHandler<Event> {

      @Override
      public void handle(Event event) {
        container.getChildren().removeAll(container.getChildren());
        container.getChildren().addAll(controlContainer, getRawCertificateContainer(certificate));
      }
    }


    private class TextCertificateEvent implements EventHandler<Event> {

      @Override
      public void handle(Event event) {
        if (certificate instanceof X509Certificate) {
          updateCertPane((X509Certificate) certificate);
        }
      }

      private void updateCertPane(X509Certificate x509Certificate) {
        List<HBox> textBoxes = new ArrayList<>();
        try {
          textBoxes.add(createTextEntry("Version: ", x509Certificate.getVersion()));
          textBoxes.add(createTextEntry("Serial number: ", x509Certificate.getSerialNumber()));
          textBoxes.add(createTextEntry("Issuer: ", x509Certificate.getIssuerDN()));
          textBoxes.add(
              createTextEntry("Issuer principal: ", x509Certificate.getIssuerX500Principal()));
          textBoxes.add(createTextEntry("Subject: ", x509Certificate.getSubjectDN()));
          textBoxes.add(
              createTextEntry("Subject principal: ", x509Certificate.getSubjectX500Principal()));
          textBoxes.add(createTextEntry("Not after: ", x509Certificate.getNotAfter()));
          textBoxes.add(createTextEntry("Not before: ", x509Certificate.getNotBefore()));
          textBoxes.add(
              createTextFieldEntry("TBS Certificate: ", x509Certificate.getTBSCertificate()));
          textBoxes.add(createTextFieldEntry("Signature: ", x509Certificate.getSignature()));
          textBoxes.add(createTextEntry("SignAlgName: ", x509Certificate.getSigAlgName()));
          textBoxes.add(createTextEntry("SignAlgOID: ", x509Certificate.getSigAlgOID()));
          textBoxes.add(createTextEntry("SignAlgParam: ", x509Certificate.getSigAlgParams()));
        } catch (Exception e) {
          ExceptionDialog ed = new ExceptionDialog(e);
          ed.showAndWait();
        }
        VBox vBox;
        vBox = new VBox(GlobalConstants.SPACING, textBoxes.toArray(new HBox[]{}));
        container.getChildren().removeAll(container.getChildren());
        container.getChildren().addAll(controlContainer, vBox);
      }

      private HBox createTextFieldEntry(String title, byte[] data) {
        TextField textField = new TextField();
        textField.setEditable(false);
        textField.setText(Hex.encodeHexString(data));
        HBox.setHgrow(textField, Priority.ALWAYS);
        return new HBox(GuiUtils.getTitle(title), textField);
      }

      private HBox createTextEntry(String title, Object data) {
        Label label = new Label();
        if (Objects.nonNull(data)) {
          label.setText(data.toString());
        }
        HBox.setHgrow(label, Priority.ALWAYS);
        return new HBox(GuiUtils.getTitle(title), label);
      }
    }
  }

}

