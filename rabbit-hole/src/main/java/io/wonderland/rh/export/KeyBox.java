package io.wonderland.rh.export;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.utils.GuiUtils;
import java.io.File;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class KeyBox extends VBox {

  private static final String DEFAULT_KEY_FILENAME = "secret_key";
  private final Label keyPathLbl = GuiUtils.getTitle("Key path :");
  private final Label keyFilenameLbl = GuiUtils.getTitle("Key filename :");
  private final TextField keyFilenameTF = new TextField(DEFAULT_KEY_FILENAME);
  @Getter
  private final KeyParams keyParams = new KeyParams(null, DEFAULT_KEY_FILENAME);
  private Label pathIconLbl = new Label();

  public KeyBox() {
    build();
  }

  private void build() {
    this.pathIconLbl.setOnMouseReleased(new KeyPathEvent());
    this.pathIconLbl.setGraphic(
        GuiUtils.getIconClasspath("/icons/opened-folder/icons8-opened-folder-16.png"));
    this.keyFilenameTF.textProperty()
        .addListener((observableValue, oldValue, newValue) -> keyParams.setKeyFilename(newValue));
    this.getChildren()
        .addAll(GuiUtils.getHboxGrowLast(GlobalConstants.DIALOG_SPACING, keyPathLbl, pathIconLbl));
    this.getChildren().add(
        GuiUtils.getHboxGrowLast(GlobalConstants.DIALOG_SPACING, keyFilenameLbl, keyFilenameTF));
    this.setSpacing(GlobalConstants.SPACING);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class KeyParams {

    private String keyPath;
    private String keyFilename;

    public static KeyParams empty() {
      return new KeyParams();
    }
  }

  private class KeyPathEvent implements EventHandler<Event> {

    public static final String KEY_PATH = "Key path";

    @Override
    public void handle(Event event) {
      DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle(KEY_PATH);
      final File file = directoryChooser.showDialog(getScene().getWindow());
      try {
        if (StringUtils.isNotEmpty(file.getName())) {
          updatePathIconLbl(file.getPath());
          keyParams.setKeyPath(file.getPath());
        }
      } catch (Exception e) {
        pathIconLbl.setText(StringUtils.EMPTY);
        log.error(e.getMessage());
      }
    }

    private void updatePathIconLbl(String path) {
      String partialPath = "~" + splitPath(path);
      pathIconLbl = new Label(partialPath);
      getChildren().remove(0);
      getChildren().add(0,
          GuiUtils.getHboxGrowLast(GlobalConstants.DIALOG_SPACING, keyPathLbl, pathIconLbl));
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
