package io.wonderland.rh.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.wonderland.commons.Arrays;
import java.awt.image.BufferedImage;
import java.security.Key;
import java.util.Objects;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
public final class ZxingUtils {

  private static final QRCodeWriter QR_CODE_WRITER = new QRCodeWriter();
  private static final double QR_CODE_TO_VIEW_RATIO = 0.6;
  private static final double DEFAULT_HEIGHT = 700;

  private ZxingUtils() {
  }

  public static BufferedImage generateQRCodeImage(String barcode, int width, int height) throws Exception {
    BitMatrix bitMatrix = QR_CODE_WRITER.encode(barcode, BarcodeFormat.QR_CODE, width, height);
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }

  public static ImageView getImageView(Pane pane, Key key) throws Exception {
    if (Objects.nonNull(key) && ArrayUtils.isNotEmpty(key.getEncoded())) {
      byte[] encoded = key.getEncoded();
      double height = Objects.nonNull(pane) ? pane.getHeight() > DEFAULT_HEIGHT ? pane.getHeight() : DEFAULT_HEIGHT
          : DEFAULT_HEIGHT;
      int a = (int) Math.sqrt(encoded.length);
      int edge = (int) ((a / (a / height)) * QR_CODE_TO_VIEW_RATIO);
      Image image = SwingFXUtils.toFXImage(
          ZxingUtils.generateQRCodeImage(Arrays.getStringValueOf(encoded), edge, edge), null);
      ImageView imageView = new ImageView();
      imageView.setImage(image);
      imageView.setPreserveRatio(true);
      imageView.setSmooth(true);
      imageView.setCache(true);
      return imageView;
    }
    throw new IllegalArgumentException("ImageView not created.Argument must not be null or empty.");
  }

}
