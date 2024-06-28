package io.wonderland.rh.cipher;

import static io.wonderland.rh.GlobalConstants.SPACING;

import io.atlassian.fugue.Either;
import io.wonderland.rh.base.codec.CodecAlg;
import io.wonderland.rh.base.fx.CharsetDropdown;
import io.wonderland.rh.base.fx.CodecDropdown;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.fx.TextPane;
import io.wonderland.rh.base.fx.base.BaseTitledPane;
import io.wonderland.rh.base.observer.KeygenObserver;
import io.wonderland.rh.cipher.mediator.CipherMediator;
import io.wonderland.rh.cipher.mediator.Ciphertext;
import io.wonderland.rh.cipher.mediator.CiphertextArea;
import io.wonderland.rh.cipher.mediator.Plaintext;
import io.wonderland.rh.cipher.mediator.PlaintextArea;
import io.wonderland.rh.exception.ServiceException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
public class CipherMessagePane extends BaseTitledPane {

  private final VBox controlBox = new VBox();
  private final HBox textBox = new HBox();
  private final VBox rootBox = new VBox();

  private final Plaintext plaintext = new Plaintext();
  private final Ciphertext ciphertext = new Ciphertext();
  private final PlaintextArea plaintextArea = new PlaintextArea();
  private final CiphertextArea ciphertextArea = new CiphertextArea();

  //dropdown creation
  private final CharsetDropdown<String, Charset, Void> plaintextDropdown = CharsetDropdown.create(
      CharsetDropdown.func(plaintextArea, plaintext));
  //text panes ui
  private final TextPane plaintextPane = new TextPane("Plaintext", plaintextDropdown,
      plaintextArea);
  private final CodecDropdown ciphertextDropdown = CodecDropdown.createCodec(
      func(ciphertext, ciphertextArea));
  private final TextPane ciphertextPane = new TextPane("Ciphertext", ciphertextDropdown,
      ciphertextArea);
  private final CipherMediator cipherMediator = new CipherMediator(plaintext, ciphertext,
      plaintextArea,
      ciphertextArea);
  private final Button encryptBtn = new Button("encrypt");
  private final Button decryptBtn = new Button("decrypt");
  private final Button clearBtn = new Button("clear");

  //encrypt & decrypt cipher
  private final Optional<Cipher> optEC;
  private final Optional<Cipher> optDC;
  private final KeygenObserver keygenObserver;
  private final String cipherName;

  public CipherMessagePane(String title, String cspName, String cipherName,
      KeygenObserver keygenObserver) {
    this.optEC = getCipherInstance(cspName, cipherName);
    this.optDC = getCipherInstance(cspName, cipherName);
    this.keygenObserver = keygenObserver;
    this.cipherName = cipherName;
    this.setText(title);
    this.build();

  }

  private void build() {
    this.buildControlBox();

    this.textBox.setSpacing(SPACING);
    this.textBox.getChildren().addAll(plaintextPane, ciphertextPane);
    HBox.setHgrow(plaintextPane, Priority.ALWAYS);
    HBox.setHgrow(ciphertextPane, Priority.ALWAYS);

    this.rootBox.setSpacing(5);
    this.rootBox.getChildren().addAll(controlBox, textBox);
    this.setContent(rootBox);
  }

  private void buildControlBox() {
    this.encryptBtn.setPrefWidth(100);
    this.decryptBtn.setPrefWidth(100);
    this.clearBtn.setPrefWidth(100);
    this.encryptBtn.setOnMousePressed(new EncryptBtnReleased());
    this.decryptBtn.setOnMousePressed(new DecryptBtnReleased());
    this.clearBtn.setOnMousePressed(new ClearBtnReleased());

    HBox buttonBox = new HBox();
    buttonBox.setSpacing(SPACING);
    buttonBox.getChildren().addAll(encryptBtn, decryptBtn, clearBtn);

    this.controlBox.getChildren().addAll(keygenObserver.getContainerBox(), buttonBox);
    this.controlBox.setSpacing(SPACING);
  }

  /**
   * Function applied when numeral base is changed at dropdown.
   *
   * @param ciphertext     reference
   * @param ciphertextArea gui reference
   * @return functional interface impl
   */
  private Function<CodecAlg<byte[], String, String, byte[]>, Void> func(Ciphertext ciphertext,
      CiphertextArea ciphertextArea) {
    return codecAlg -> {
      if (ArrayUtils.isNotEmpty(ciphertext.getValue())) {
        byte[] ct = ciphertext.getValue();

        //encode ciphertext bytes according to current codec
        String encoded = codecAlg.encode().apply(ct);

        //set codec alg with which string was encoded
        ciphertextArea.setCodecAlg(codecAlg);

        //set new text
        ciphertextArea.clear();
        ciphertextArea.setText(encoded);
      }
      return null;
    };
  }

  protected Optional<Cipher> getCipherInstance(String cspName, String serviceName)
      throws ServiceException {
    Cipher tmp = null;
    try {
      tmp = Cipher.getInstance(serviceName, cspName);
      log.info("Selected cipher '{}' - provider '{}' ", tmp.getAlgorithm(),
          tmp.getProvider().getName());
    } catch (Exception e) {
      log.error("Failed to instantiate cipher service '{}'-'{}'", cspName, serviceName);
    }
    return Optional.ofNullable(tmp);
  }

  private void cipherInit() throws InvalidKeyException {
    Optional<Either<SecretKey, KeyPair>> optional = keygenObserver.getOptionalKey();
    if (optional.isPresent() && keygenObserver.isUpdated()) {
      Either<SecretKey, KeyPair> either = optional.get();
      if (either.isLeft()) {
        secretKeyInit(either.left().get());
      } else if (either.isRight()) {
        KeyPair keyPair = either.right().get();
        if (keygenObserver.isPublicKeySelected()) {
          keyPairInit(keyPair.getPublic(), keyPair.getPrivate());
        } else {
          keyPairInit(keyPair.getPrivate(), keyPair.getPublic());
        }
      }
    }
  }

  private void secretKeyInit(SecretKey secretKey) throws InvalidKeyException {
    SecretKeySpec secretKeySpec = getSecretKeySpec(cipherName, secretKey);
    //init encrypt cipher
    if (optEC.isPresent()) {
      Cipher ec = optEC.get();
      try {
        ec.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      } catch (Exception e) {
        log.error("Failed to init encrypt-cipher.", e);
        throw e;
      }
    }

    //init decrypt cipher
    if (optDC.isPresent()) {
      Cipher dc = optDC.get();
      try {
        dc.init(Cipher.DECRYPT_MODE, secretKeySpec);
      } catch (Exception e) {
        log.error("Failed to init decrypt-cipher.", e);
        throw e;
      }
    }
  }

  /**
   * Added because of java.security.InvalidKeyException: Wrong format: RAW bytes needed.
   *
   * @param cipherName cipher algorithm name
   * @param secretKey  secret key instance
   * @return secret key spec instance
   */
  private SecretKeySpec getSecretKeySpec(String cipherName, SecretKey secretKey) {
    if (cipherName.equals(secretKey.getAlgorithm())) {
      return new SecretKeySpec(secretKey.getEncoded(), secretKey.getAlgorithm());
    } else {
      return new SecretKeySpec(secretKey.getEncoded(), cipherName);
    }
  }

  private void keyPairInit(Key publicKey, Key privateKey) throws InvalidKeyException {
    //init encrypt cipher
    if (optEC.isPresent()) {
      Cipher ec = optEC.get();
      try {
        ec.init(Cipher.ENCRYPT_MODE, publicKey);
      } catch (Exception e) {
        log.error("Failed to init encrypt-cipher.", e);
        throw e;
      }
    }

    //init decrypt cipher
    if (optDC.isPresent()) {
      Cipher dc = optDC.get();
      try {
        dc.init(Cipher.DECRYPT_MODE, privateKey);
      } catch (Exception e) {
        log.error("Failed to init decrypt-cipher.", e);
        throw e;
      }
    }
  }


  class EncryptBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      try {
        //init cipher
        cipherInit();
        if (optEC.isPresent()) {
          Cipher cipher = optEC.get();
          cipherMediator.encrypt(cipher);
        }
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }
  }

  class DecryptBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      try {
        //init cipher
        cipherInit();
        if (optDC.isPresent()) {
          Cipher cipher = optDC.get();

          //decrypt
          cipherMediator.decrypt(cipher);
        }
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }
  }

  class ClearBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      CompletableFuture.runAsync(cipherMediator::clear);

    }
  }

}


