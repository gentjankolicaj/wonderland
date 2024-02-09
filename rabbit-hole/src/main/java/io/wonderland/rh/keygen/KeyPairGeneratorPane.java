package io.wonderland.rh.keygen;

import io.wonderland.common.Arrays;
import io.wonderland.rh.common.HTogglePane;
import io.wonderland.rh.utils.ZxingUtils;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Map;
import java.util.Objects;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Getter
@Setter
@Slf4j
public class KeyPairGeneratorPane extends BorderPane {
  private Alert alert = new Alert(AlertType.NONE);
  private final double QR_CODE_TO_VIEW_RATIO = 0.8;
  private HTogglePane<RadioButton> keyFormatPane = new HTogglePane<>("KeyPair format : ", s -> new RadioButton(s),
      Map.of("QR", () -> setQRCode(getKeyPair()), "Dec", () -> setString(getKeyPair())));
  private KeyPair keyPair;

  public KeyPairGeneratorPane() {
    this.setTop(keyFormatPane);
  }


  public void update(KeyPair keyPair) {
    this.keyPair = keyPair;
    this.setQRCode(keyPair);
    this.keyFormatPane.selectToggle("QR");
  }

  private void setQRCode(KeyPair keyPair) {
    //set public key
    byte[] pubKey = keyPair.getPublic().getEncoded();
    byte[] privKey = keyPair.getPrivate().getEncoded();
    HBox hBox=new HBox();
    hBox.setSpacing(20);
    try {
      if (ArrayUtils.isNotEmpty(pubKey)) {
        int a = (int) Math.sqrt(pubKey.length);
        int A = (int) ((a / (a / this.getHeight())) * QR_CODE_TO_VIEW_RATIO);
        Image image = SwingFXUtils.toFXImage(
            ZxingUtils.generateQRCodeImage(Arrays.getStringValueOf(pubKey), A, A), null);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        hBox.getChildren().add(imageView);
      }
      if (ArrayUtils.isNotEmpty(privKey)) {
        int a = (int) Math.sqrt(privKey.length);
        int A = (int) ((a / (a / this.getHeight())) * QR_CODE_TO_VIEW_RATIO);
        Image image = SwingFXUtils.toFXImage(
            ZxingUtils.generateQRCodeImage(Arrays.getStringValueOf(privKey), A, A), null);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        hBox.getChildren().add(imageView);
      }
      this.setCenter(hBox);
    } catch (Exception e) {
      log.error("", e);
      alert.setAlertType(AlertType.ERROR);
      alert.show();
      alert.setContentText(e.getMessage());
    }
  }

  private void setString(KeyPair keyPair) {
    byte[] pubKey = keyPair.getPublic().getEncoded();
    byte[] privKey = keyPair.getPrivate().getEncoded();
    StringBuilder sb = new StringBuilder();
    sb.append("PUBLIC-KEY: ").append(Arrays.getStringValueOf(pubKey, ',')).append("\n");
    sb.append("PRIVATE-KEY: ").append(Arrays.getStringValueOf(privKey, ','));
    TextArea textArea = new TextArea();
    textArea.setText(sb.toString());
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(textArea);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    this.setCenter(scrollPane);
  }

}
