package io.wonderland.rh.utils;


import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
public final class GuiUtils {

  private GuiUtils() {
  }

  public static Label getTitle(String text) {
    Label label = new javafx.scene.control.Label(text);
    label.setFont(Font.font("ARIAL", FontWeight.BOLD, 13));
    return label;
  }

  public static Label getTitle(String text, Font font) {
    Label label = new javafx.scene.control.Label(text);
    label.setFont(font);
    return label;
  }


  public static ImageView getIconClasspath(String pathname) {
    Image image = new Image(
        Objects.requireNonNull(GuiUtils.class.getResource(pathname)).toExternalForm());
    ImageView imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    return imageView;
  }

  public static HBox getHboxGrowLast(int spacing, Node... nodes) {
    if (ArrayUtils.isEmpty(nodes)) {
      throw new IllegalArgumentException("Must have at least 1 node.");
    }
    HBox hBox = new HBox();
    hBox.setSpacing(spacing);
    hBox.getChildren().addAll(nodes);
    HBox.setHgrow(nodes[nodes.length - 1], Priority.ALWAYS);
    return hBox;
  }


}
