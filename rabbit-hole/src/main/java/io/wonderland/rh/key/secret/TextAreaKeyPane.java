package io.wonderland.rh.key.secret;

import io.atlassian.fugue.Either;
import io.wonderland.alice.crypto.key.GenericSecretKey;
import io.wonderland.alice.crypto.key.NullSecretKey;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.codec.CodecAlg;
import io.wonderland.rh.base.fx.CodecDropdown;
import io.wonderland.rh.base.fx.CodecTextArea;
import io.wonderland.rh.base.observer.EitherKeyObserver;
import io.wonderland.rh.utils.GuiUtils;
import java.security.Key;
import java.security.KeyPair;
import java.util.Optional;
import java.util.function.Function;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.VBox;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class TextAreaKeyPane<T extends Key> extends AbstractKeyPane<T> {

  @Getter
  protected final String keyAlgorithm;
  protected final CodecTextArea textArea;
  private final CodecDropdown codecDropdown;
  private final EitherKeyObserver eitherKeyObserver;


  public TextAreaKeyPane(String keyAlgorithm, EitherKeyObserver eitherKeyObserver) {
    this.keyAlgorithm = keyAlgorithm;
    this.eitherKeyObserver = eitherKeyObserver;
    this.textArea = new CodecTextArea();
    this.codecDropdown = CodecDropdown.createCodec(dropdownFunc(eitherKeyObserver, textArea));
    this.build();
  }


  /**
   * Function applied when numeral base is changed at dropdown.
   *
   * @return functional interface impl
   */
  protected Function<CodecAlg<byte[], String, String, byte[]>, Void> dropdownFunc(
      EitherKeyObserver eitherKeyObserver,
      CodecTextArea codecTextArea) {
    return codecAlg -> {
      Optional<Either<SecretKey, KeyPair>> optional = eitherKeyObserver.getOptionalKey();
      if (StringUtils.isNotEmpty(codecTextArea.getText()) && optional.isPresent()) {
        SecretKey secretKey = optional.get().left().get();

        //encode key bytes according to current codec
        String encoded = codecAlg.encode().apply(secretKey.getEncoded());

        //set codec alg with which string was encoded
        codecTextArea.setCodecAlg(codecAlg);

        //set new key
        codecTextArea.clear();
        codecTextArea.setText(encoded);
      }
      return null;
    };
  }

  private void build() {
    //add text listener
    this.textArea.textProperty().addListener(getTextChangeListener());
    VBox container = new VBox(GlobalConstants.SPACING, GuiUtils.getTitle(this.keyAlgorithm),
        codecDropdown, textArea);
    this.setCenter(container);
  }

  private ChangeListener<String> getTextChangeListener() {
    return (obs, oldValue, newValue) -> {
      if (StringUtils.isNotEmpty(newValue)) {
        // update key observer
        eitherKeyObserver.update(Either.left(getKey()));
      }
      log.info("Key input changed.");
    };
  }


  public T getCipherKey() {
    throw new UnsupportedOperationException("Unimplemented.");
  }


  public SecretKey getKey() {
    CodecAlg<byte[], String, String, byte[]> codecAlg = textArea.getCodecAlg();
    return new GenericSecretKey(codecAlg.decode().apply(textArea.getText()));
  }

  public boolean removeKey() {
    this.textArea.clear();
    this.eitherKeyObserver.update(Either.left(new NullSecretKey()));
    return true;
  }

}
