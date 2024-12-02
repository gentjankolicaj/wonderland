package io.wonderland.rh.mac;

import static io.wonderland.rh.GlobalConstants.SPACING;

import io.atlassian.fugue.Either;
import io.wonderland.rh.base.fx.CharsetDropdown;
import io.wonderland.rh.base.fx.CharsetTextArea;
import io.wonderland.rh.base.fx.CodecDropdown;
import io.wonderland.rh.base.fx.CodecTextArea;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.fx.TextPane;
import io.wonderland.rh.base.fx.base.BaseTitledPane;
import io.wonderland.rh.base.observer.EitherKeyObserver;
import io.wonderland.rh.base.observer.TypeObserver;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Optional;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MacMessagePane extends BaseTitledPane {

  private final BorderPane container = new BorderPane();
  private final VBox controlBox = new VBox();
  private final HBox textBox = new HBox();
  private final CharsetTextArea messageTextArea = new CharsetTextArea();
  private final CodecTextArea macTextArea = new CodecTextArea();

  //Observers of message & digest arrays
  private final TypeObserver<byte[]> messageObserver = new TypeObserver<>();
  private final TypeObserver<byte[]> macObserver = new TypeObserver<>();

  //dropdowns
  private final CharsetDropdown<String, Charset, Void> charsetDropdown = CharsetDropdown.create(
      CharsetDropdown.func(messageTextArea, messageObserver));
  //text panes
  private final TextPane messagePane = new TextPane("Message", charsetDropdown, messageTextArea);
  private final CodecDropdown codecDropdown = CodecDropdown.createCodec(
      CodecDropdown.defaultFunc(macObserver, macTextArea));
  private final TextPane macPane = new TextPane("Message Authentication Code (MAC)", codecDropdown,
      macTextArea);
  private final Button signBtn = new Button("sign");
  private final String macName;
  private final Optional<Mac> optMac;
  private final EitherKeyObserver eitherKeyObserver;

  public MacMessagePane(String title, String cspName, String macName,
      EitherKeyObserver eitherKeyObserver)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    this.macName = macName;
    this.optMac = getMacInstance(cspName, macName);
    this.eitherKeyObserver = eitherKeyObserver;
    this.setText(title);
    this.build();

  }

  protected static Optional<Mac> getMacInstance(String cspName, String serviceName)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    Mac tmp = Mac.getInstance(serviceName, cspName);
    log.info("Selected MAC '{}' - provider '{}' ", tmp.getAlgorithm(), tmp.getProvider().getName());
    return Optional.of(tmp);
  }

  private void build() {
    this.buildControlBox();
    this.textBox.setSpacing(SPACING);
    this.textBox.getChildren().addAll(messagePane, macPane);

    this.container.setTop(controlBox);
    this.container.setCenter(textBox);
    this.setContent(this.container);
  }

  private void buildControlBox() {
    this.signBtn.setPrefWidth(100);
    this.signBtn.setOnMousePressed(new SignBtnReleased());

    HBox buttonBox = new HBox();
    buttonBox.setSpacing(SPACING);
    buttonBox.getChildren().addAll(signBtn);

    this.controlBox.getChildren().addAll(buttonBox);
    this.controlBox.setSpacing(SPACING);
  }


  private class SignBtnReleased implements EventHandler<Event> {

    @Override
    public void handle(Event event) {
      if (optMac.isEmpty()) {
        return;
      }
      try {
        macInit();
        Mac mac = optMac.get();
        String message =
            StringUtils.isEmpty(messageTextArea.getText()) ? StringUtils.EMPTY
                : messageTextArea.getText();
        byte[] tag = mac.doFinal(message.getBytes());
        macObserver.update(tag);

        //update mac text area
        codecDropdown.applyFunc();
        log.info("MAC: message={}, mac={}", message.getBytes(), tag);
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }

    private void macInit() throws InvalidKeyException {
      Optional<Either<SecretKey, KeyPair>> optional = eitherKeyObserver.getOptionalKey();
      if (optMac.isPresent() && optional.isPresent() && eitherKeyObserver.isUpdated()) {
        Either<SecretKey, KeyPair> either = optional.get();
        SecretKey secretKey = either.left().get();
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), macName);
        optMac.get().init(secretKeySpec);
      }
    }
  }

}


