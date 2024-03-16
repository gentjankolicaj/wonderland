package io.wonderland.rh.utils;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GuiUtils {

  private GuiUtils(){}

  public static ImageView getImageView(String filePath) {
    try {
      return new ImageView(new Image(MyFileUtils.getFile(filePath)));
    } catch (Exception e) {
      return new ImageView();
    }
  }

  public static BackgroundImage getLocalBackgroundImage(String filePath) throws IOException {
    BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false,
        false, true, false);

    Image image = new Image(MyFileUtils.getFile(filePath));
    return new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
        size);

  }

  public static Background getKeyBackground() {
    try {
      return new Background(getLocalBackgroundImage("key.png"));
    } catch (IOException e) {
      log.error("Error loading key.png image", e);
      return new Background(new BackgroundFill(Paint.valueOf("WHITE"), CornerRadii.EMPTY, Insets.EMPTY));
    }
  }

}
