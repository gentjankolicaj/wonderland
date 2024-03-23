package io.wonderland.rh.mac;

import io.wonderland.rh.base.KeyObserver;
import io.wonderland.rh.base.TypeObserver;
import io.wonderland.rh.base.dropdown.Dropdown;
import io.wonderland.rh.base.pane.TextPane;
import io.wonderland.rh.base.dropdown.DropdownHelper;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MacMessagePane extends TitledPane {

  private static final int SPACING = 10;
  private final Alert alert = new Alert(AlertType.NONE);
  private final VBox controlBox = new VBox();
  private final HBox textBox = new HBox();
  private final VBox rootBox = new VBox();
  private final TextArea messageTextArea = new TextArea();
  private final TextArea macTextArea = new TextArea();

  //Observers of message & digest arrays
  private final TypeObserver<byte[]> messageObserver=new TypeObserver<>();
  private final TypeObserver<byte[]> macObserver=new TypeObserver<>() ;
  private final Dropdown<String,byte[],TextArea> messageDropdown= DropdownHelper.getCharsetDropdown(messageTextArea,messageObserver);
  private final Dropdown<String,byte[],TextArea> macDropdown= DropdownHelper.getEncodingDropdown(macTextArea,macObserver);
  private final TextPane messagePane = new TextPane("Message",messageDropdown, messageTextArea);
  private final TextPane macPane = new TextPane("Message Authentication Code (MAC)", macDropdown,macTextArea);
  private final Button signBtn=new Button("sign");
  private final String macName;
  private final Optional<Mac> optionalMac;
  private final KeyObserver<?> keyObserver;

  public MacMessagePane(String title, String macName, KeyObserver<?> keyObserver) throws NoSuchAlgorithmException {
    this.macName=macName;
    this.optionalMac = getMacInstance(macName);
    this.keyObserver = keyObserver;
    this.setText(title);
    this.build();

  }

  private void build() {
    this.buildControlBox();

    this.textBox.setSpacing(SPACING);
    this.textBox.getChildren().addAll(messagePane, macPane);
    HBox.setHgrow(messagePane, Priority.ALWAYS);
    HBox.setHgrow(macPane, Priority.ALWAYS);

    this.rootBox.setSpacing(5);
    this.rootBox.getChildren().addAll(controlBox,textBox);
    this.setContent(this.rootBox);
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


  protected static Optional<Mac> getMacInstance(String serviceName) throws NoSuchAlgorithmException {
    Mac tmp=Mac.getInstance(serviceName);
    log.info("Selected MAC '{}' - provider '{}' ", tmp.getAlgorithm(), tmp.getProvider().getName());
    return Optional.of(tmp);
  }



  class SignBtnReleased implements EventHandler<Event> {
    @Override
    public void handle(Event event) {
      if (optionalMac.isEmpty()) {
        return;
      }
      try {
        macInit();
        Mac mac= optionalMac.get();
        String message =  StringUtils.isEmpty(messageTextArea.getText())? StringUtils.EMPTY:messageTextArea.getText();
        byte[] tag = mac.doFinal(message.getBytes());
        macObserver.update(tag);

        //update mac text area
        macDropdown.getSelectedDropdownElement().runConsumer(macObserver, macTextArea);

        log.debug("MAC: message={}, mac={}", message.getBytes(),tag);
      } catch (Exception e) {
        log.error(e.getMessage());
        alert.setAlertType(AlertType.ERROR);
        alert.show();
        alert.setHeaderText(e.getLocalizedMessage());
      }
    }

    private void macInit() throws InvalidKeyException {
      if(optionalMac.isPresent() && keyObserver.isUpdated() && (keyObserver.getOptionalKey().isPresent())) {
          Optional<?>  optional=keyObserver.getOptionalKey();
        Key key = (Key) optional.get();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), macName);
        optionalMac.get().init(secretKeySpec);
      }
    }
  }

}


