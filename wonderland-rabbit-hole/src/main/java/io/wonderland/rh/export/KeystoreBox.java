package io.wonderland.rh.export;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.keystore.KeyStoreType;
import io.wonderland.rh.utils.GuiUtils;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.SearchableComboBox;

@Slf4j

public class KeystoreBox extends VBox {

  private final SearchableComboBox<String> keystoreComboBox = new SearchableComboBox<>();
  private final Label keystorePathLbl = GuiUtils.getTitle("Keystore path :");
  private final Label keystorePwdLbl = GuiUtils.getTitle("Keystore password :");
  private final PasswordField keystorePasswordTF = new PasswordField();
  private final Label keyAliasLbl = GuiUtils.getTitle("Key alias :");
  private final TextField keyAliasTF = new TextField();
  private final Label keyPwdLbl = GuiUtils.getTitle("Key password :");
  private final PasswordField keyPasswordTF = new PasswordField();
  @Getter
  private final KeystoreParams keystoreParams = new KeystoreParams(
      KeyStoreType.PKCS12.name().toLowerCase(), null, null,
      null, null);
  private Label pathIconLbl = new Label();


  public KeystoreBox() {
    build();
  }

  private void build() {
    this.keystoreComboBox.setItems(FXCollections.observableArrayList(getKeystoreTypes()));
    this.keystoreComboBox.setPromptText("Keystore type :");
    this.pathIconLbl.setGraphic(
        GuiUtils.getIconClasspath("/icons/opened-folder/icons8-opened-folder-16.png"));
    this.addEventHandlers();
    this.getChildren().add(this.keystoreComboBox);
    this.getChildren().add(
        GuiUtils.getHboxGrowLast(GlobalConstants.DIALOG_SPACING, keystorePathLbl, pathIconLbl));
    this.getChildren()
        .add(GuiUtils.getHboxGrowLast(GlobalConstants.DIALOG_SPACING, keystorePwdLbl,
            keystorePasswordTF));
    this.getChildren()
        .add(GuiUtils.getHboxGrowLast(GlobalConstants.DIALOG_SPACING, keyAliasLbl, keyAliasTF));
    this.getChildren()
        .add(GuiUtils.getHboxGrowLast(GlobalConstants.DIALOG_SPACING, keyPwdLbl, keyPasswordTF));
    this.setSpacing(GlobalConstants.SPACING);
  }

  private List<String> getKeystoreTypes() {
    return Arrays.stream(KeyStoreType.values()).map(KeyStoreType::getName)
        .collect(Collectors.toList());
  }

  private void addEventHandlers() {
    this.keystoreComboBox.setOnAction(new KeystoreComboBoxEventHandler());
    this.pathIconLbl.setOnMouseReleased(new PathIconEventHandler());
    this.keystorePasswordTF.textProperty()
        .addListener((observableValue, oldValue, newValue) -> keystoreParams.setKsPwd(newValue));
    this.keyAliasTF.textProperty()
        .addListener((observableValue, oldValue, newValue) -> keystoreParams.setKeyAlias(newValue));
    this.keyPasswordTF.textProperty()
        .addListener((observableValue, oldValue, newValue) -> keystoreParams.setKeyPwd(newValue));
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class KeystoreParams {

    private String ksType;
    private String ksPath;
    private String ksPwd;
    private String keyAlias;
    private String keyPwd;

    public static KeystoreParams empty() {
      return new KeystoreParams();
    }

    /**
     * Because we can't create entries with null password on keystore
     *
     * @return key password in keystore
     */
    public String getKeyPwd() {
      return StringUtils.isEmpty(keyPwd) ? "" : keyPwd;
    }
  }

  private class KeystoreComboBoxEventHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
      if (StringUtils.isNotEmpty(keystoreComboBox.getValue())) {
        keystoreParams.setKsType(keystoreComboBox.getValue());
      }
    }
  }

  private class PathIconEventHandler implements EventHandler<Event> {

    public static final String KEY_PATH = "KeyStore path";

    @Override
    public void handle(Event event) {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle(KEY_PATH);
      final File file = directoryChooser.showDialog(getScene().getWindow());
      try {
        if (StringUtils.isNotEmpty(file.getName())) {
          updatePathIconLbl(file.getPath());
          keystoreParams.setKsPath(file.getPath());
        }
      } catch (Exception e) {
        pathIconLbl.setText(StringUtils.EMPTY);
        log.error(e.getMessage());
      }
    }

    private void updatePathIconLbl(String path) {
      String partialPath = "~" + splitPath(path);
      pathIconLbl = new Label(partialPath);
      getChildren().remove(1);
      getChildren().add(1,
          GuiUtils.getHboxGrowLast(GlobalConstants.DIALOG_SPACING, keystorePathLbl, pathIconLbl));
    }

    private String splitPath(String path) {
      char separatorChar = File.separatorChar;
      //Arbitrary value based on gui
      int maxStringSize = 30;
      String lastShortPath = "";
      int len = path.length() - 1;
      for (int i = len; i >= 0; i--) {
        if (path.charAt(i) == separatorChar) {
          if (path.substring(i, len + 1).length() > maxStringSize) {
            break;
          } else {
            lastShortPath = path.substring(i, len + 1);
          }
        }
      }
      return lastShortPath;
    }
  }

}
