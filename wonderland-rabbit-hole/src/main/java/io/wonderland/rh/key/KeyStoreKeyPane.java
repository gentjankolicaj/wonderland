package io.wonderland.rh.key;

import io.atlassian.fugue.Either;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.fx.CheckboxTitledPane;
import io.wonderland.rh.base.fx.ConfirmationDialog;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.observer.EitherKeyObserver;
import io.wonderland.rh.utils.CodecUtils;
import io.wonderland.rh.utils.GuiUtils;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class KeyStoreKeyPane extends TitledPane {

  private final VBox container = new VBox();


  public KeyStoreKeyPane(String title, KeyStore keyStore, EitherKeyObserver eitherKeyObserver) {
    this.setText(title);
    this.build(keyStore, eitherKeyObserver);
  }

  private void build(KeyStore keyStore, EitherKeyObserver eitherKeyObserver) {
    if (Objects.nonNull(keyStore)) {
      try {
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
          String alias = aliases.nextElement();
          if (keyStore.isKeyEntry(alias)) {
            container.getChildren().add(new KeyEntryBox(keyStore, alias, eitherKeyObserver));
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage());
      }
      this.container.setSpacing(GlobalConstants.SPACING);
      this.setContent(container);
    }
  }

  static class KeyEntryBox extends CheckboxTitledPane {

    static final String COPY_ENCODING_ICON = "/icons/copy-encoding/icons8-copy-24.png";
    private final VBox container = new VBox();
    private final Label showKeyLbl = new Label();
    private final KeyStore keyStore;
    private final String keyAlias;
    private final EitherKeyObserver eitherKeyObserver;

    @Getter
    private Either<SecretKey, KeyPair> either;

    KeyEntryBox(KeyStore keyStore, String keyAlias, EitherKeyObserver eitherKeyObserver) {
      super(String.format("key-alias : [ %s ]", keyAlias));
      this.keyStore = keyStore;
      this.keyAlias = keyAlias;
      this.eitherKeyObserver = eitherKeyObserver;
      this.build();
    }

    private void build() {
      this.showKeyLbl.setOnMouseReleased(new ShowKeyBtnReleased());
      this.showKeyLbl.setGraphic(
          GuiUtils.getIconClasspath("/icons/show-pwd/icons8-show-password-24.png"));
      this.showKeyLbl.setTooltip(new Tooltip("Show key"));
      this.container.setSpacing(GlobalConstants.SPACING);
      this.container.getChildren().addAll(showKeyLbl);
      this.setContent(container);
    }

    @Override
    protected ChangeListener<Boolean> checkboxListener() {
      return (observable, oldValue, newValue) -> {
        if (Boolean.TRUE.equals(newValue)) {
          Either<SecretKey, KeyPair> tmp = getEither();
          if (Objects.isNull(tmp)) {
            ConfirmationDialog cd = new ConfirmationDialog("Encrypted key",
                "Decrypt key so it can be used by Cipher.");
            cd.showAndWait();
            selectCheckbox(false);
          } else {
            eitherKeyObserver.update(either);
          }
        }
      };
    }


    private class ShowKeyBtnReleased implements EventHandler<Event> {

      @Override
      public void handle(Event event) {
        try {
          showKey();
        } catch (Exception e) {
          ExceptionDialog ed = new ExceptionDialog(e);
          ed.showAndWait();
        }
      }

      private void showKey()
          throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText(String.format("Enter key password for [%s]:", keyAlias));
        Optional<String> optionalPwd = td.showAndWait();
        if (optionalPwd.isPresent()) {
          KeyStore.Entry entry = getAliasEntry(keyAlias, optionalPwd.get());
          if (entry instanceof KeyStore.SecretKeyEntry) {
            either = Either.left(((SecretKeyEntry) entry).getSecretKey());
          } else {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;
            either = Either.right(
                new KeyPair(privateKeyEntry.getCertificate().getPublicKey(),
                    privateKeyEntry.getPrivateKey()));
          }
          Platform.runLater(() -> buildContainer(either));
        }
      }

      private KeyStore.Entry getAliasEntry(String alias, String keyPwd)
          throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
        return StringUtils.isEmpty(keyPwd) ? keyStore.getEntry(alias, null)
            : keyStore.getEntry(alias, new PasswordProtection(keyPwd.toCharArray()));
      }

      private void buildContainer(Either<SecretKey, KeyPair> either) {
        if (Objects.nonNull(either)) {
          clearContainer();
          if (either.isLeft()) {
            buildSecretKey(either.left().get());
          } else {
            buildKeyPair(either.right().get());
          }
        }
      }

      private void clearContainer() {
        container.getChildren().removeAll(container.getChildren());
      }

      private void buildSecretKey(SecretKey secretKey) {
        Label skLbl = GuiUtils.getTitle(GlobalConstants.SECRET_KEY);
        TextField keyTF = new TextField();
        Label copyKeyLbl = new Label();

        //add listeners
        copyKeyLbl.setOnMouseReleased(copySecretKeyListener());
        copyKeyLbl.setGraphic(GuiUtils.getIconClasspath(COPY_ENCODING_ICON));
        copyKeyLbl.setTooltip(new Tooltip("Copy key"));
        //add key content
        keyTF.setText(CodecUtils.encodeBase10(secretKey.getEncoded(), ' '));
        keyTF.setEditable(false);
        HBox box = new HBox(GlobalConstants.SPACING, skLbl, copyKeyLbl, keyTF);
        HBox.setHgrow(keyTF, Priority.ALWAYS);
        HBox.setHgrow(box, Priority.ALWAYS);
        container.getChildren().addAll(box);
      }

      private void buildKeyPair(KeyPair keyPair) {
        final Label pkLbl = GuiUtils.getTitle(GlobalConstants.PUBLIC_KEY);
        final Label skLbl = GuiUtils.getTitle(GlobalConstants.PRIVATE_KEY);
        final Label copyPKEncodingLbl = new Label();
        final Label copySKEncodingLbl = new Label();
        final TextField pkTF = new TextField();
        final TextField skTF = new TextField();

        //add pk listeners
        copyPKEncodingLbl.setOnMouseReleased(copyPKKeyListener());
        copyPKEncodingLbl.setGraphic(GuiUtils.getIconClasspath(COPY_ENCODING_ICON));
        copyPKEncodingLbl.setTooltip(new Tooltip("Copy " + GlobalConstants.PUBLIC_KEY));

        copySKEncodingLbl.setOnMouseReleased(copySKKeyListener());
        copySKEncodingLbl.setGraphic(GuiUtils.getIconClasspath(COPY_ENCODING_ICON));
        copySKEncodingLbl.setTooltip(new Tooltip("Copy " + GlobalConstants.PRIVATE_KEY));

        //add key content
        pkTF.setText(CodecUtils.encodeBase10(keyPair.getPublic().getEncoded(), ' '));
        pkTF.setEditable(false);
        skTF.setText(CodecUtils.encodeBase10(keyPair.getPrivate().getEncoded(), ' '));
        skTF.setEditable(false);
        HBox pkHBox = new HBox(GlobalConstants.SPACING, pkLbl, copyPKEncodingLbl, pkTF);
        HBox skHBox = new HBox(GlobalConstants.SPACING, skLbl, copySKEncodingLbl, skTF);
        HBox.setHgrow(pkTF, Priority.ALWAYS);
        HBox.setHgrow(skTF, Priority.ALWAYS);
        HBox.setHgrow(pkHBox, Priority.ALWAYS);
        HBox.setHgrow(skHBox, Priority.ALWAYS);
        container.getChildren().addAll(pkHBox);
        container.getChildren().addAll(skHBox);
      }

      private EventHandler<Event> copySecretKeyListener() {
        return event -> {
          try {
            if (Objects.nonNull(either) && either.isLeft()) {
              SecretKey secretKey = either.left().get();
              Clipboard clipboard = Clipboard.getSystemClipboard();
              ClipboardContent content = new ClipboardContent();
              content.put(DataFormat.PLAIN_TEXT,
                  CodecUtils.encodeBase10(secretKey.getEncoded(), ' '));
              clipboard.setContent(content);
            }
          } catch (Exception e) {
            ExceptionDialog ed = new ExceptionDialog(e);
            ed.showAndWait();
          }
        };
      }

      private EventHandler<Event> copyPKKeyListener() {
        return event -> {
          try {
            if (Objects.nonNull(either) && either.isRight()) {
              PublicKey publicKey = either.right().get().getPublic();
              Clipboard clipboard = Clipboard.getSystemClipboard();
              ClipboardContent content = new ClipboardContent();
              content.put(DataFormat.PLAIN_TEXT,
                  CodecUtils.encodeBase10(publicKey.getEncoded(), ' '));
              clipboard.setContent(content);
            }
          } catch (Exception e) {
            ExceptionDialog ed = new ExceptionDialog(e);
            ed.showAndWait();
          }
        };
      }

      private EventHandler<Event> copySKKeyListener() {
        return event -> {
          try {
            if (Objects.nonNull(either) && either.isRight()) {
              PrivateKey privateKey = either.right().get().getPrivate();
              Clipboard clipboard = Clipboard.getSystemClipboard();
              ClipboardContent content = new ClipboardContent();
              content.put(DataFormat.PLAIN_TEXT,
                  CodecUtils.encodeBase10(privateKey.getEncoded(), ' '));
              clipboard.setContent(content);
            }
          } catch (Exception e) {
            ExceptionDialog ed = new ExceptionDialog(e);
            ed.showAndWait();
          }
        };
      }
    }
  }

}
