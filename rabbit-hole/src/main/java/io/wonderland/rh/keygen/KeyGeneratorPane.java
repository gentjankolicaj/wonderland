package io.wonderland.rh.keygen;

import io.wonderland.common.Arrays;
import io.wonderland.rh.common.HTogglePane;
import io.wonderland.rh.utils.ZxingUtils;
import java.util.Map;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Getter
@Setter
@Slf4j
public class KeyGeneratorPane extends BorderPane {
  private final double QR_CODE_TO_VIEW_RATIO = 0.6;
  private HTogglePane<RadioButton> keyFormatPane = new HTogglePane<>("Secret-key format : ", s -> new RadioButton(s),
      Map.of("QR", () -> setQRCode(getSecretKey().getEncoded()), "Dec", () -> setString(getSecretKey().getEncoded())));
  private SecretKey secretKey;

  public KeyGeneratorPane() {
    this.setTop(keyFormatPane);
  }


  public void update(SecretKey secretKey) {
    this.secretKey = secretKey;
    this.setQRCode(secretKey.getEncoded());
    this.keyFormatPane.selectToggle("QR");
  }

  private void setQRCode(byte[] encoded) {
    if(ArrayUtils.isNotEmpty(encoded)) {
      try {
        int a = (int) Math.sqrt(encoded.length);
        int A = (int) ((a / (a / this.getHeight())) * QR_CODE_TO_VIEW_RATIO);
        Image image = SwingFXUtils.toFXImage(
            ZxingUtils.generateQRCodeImage(Arrays.getStringValueOf(secretKey.getEncoded()), A, A), null);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        this.setCenter(imageView);
      } catch (Exception e) {
        log.error("", e);
      }
    }
  }

  private void setString(byte[] encoded) {
    if(ArrayUtils.isNotEmpty(encoded)) {
      TextArea textArea = new TextArea();
      textArea.setText(Arrays.getStringValueOf(encoded, ','));
      ScrollPane scrollPane = new ScrollPane();
      scrollPane.setContent(textArea);
      scrollPane.setFitToWidth(true);
      scrollPane.setFitToHeight(true);
      scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
      scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
      this.setCenter(scrollPane);
    }
  }

}
