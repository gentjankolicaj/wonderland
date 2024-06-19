package io.wonderland.rh.base.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import io.atlassian.fugue.Either;
import io.atlassian.fugue.Pair;
import io.wonderland.rh.base.codec.CodecAlg;
import io.wonderland.rh.base.fx.ExceptionDialog;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.apache.commons.lang3.StringUtils;

public class AztecCode extends Barcode {

  private static final AztecWriter BARCODE_WRITER = new AztecWriter();
  private static final double CODE_TO_VIEW_RATIO = 0.6;
  private static final double DEFAULT_HEIGHT = 700;

  public AztecCode(CodecAlg<byte[], String, String, byte[]> codecAlg) {
    super(codecAlg);
  }

  private static BufferedImage generateQRCodeImage(String barcode, int width, int height) {
    BitMatrix bitMatrix = BARCODE_WRITER.encode(barcode, BarcodeFormat.AZTEC, width, height);
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }

  public static ImageView getImageView(Pane pane, String encoded) {
    if (StringUtils.isNotEmpty(encoded)) {
      double height =
          Objects.nonNull(pane) ? Math.max(pane.getHeight(), DEFAULT_HEIGHT) : DEFAULT_HEIGHT;
      int a = (int) Math.sqrt(encoded.length());
      int edge = (int) ((a / (a / height)) * CODE_TO_VIEW_RATIO);
      Image image = SwingFXUtils.toFXImage(generateQRCodeImage(encoded, edge, edge), null);
      ImageView imageView = new ImageView();
      imageView.setImage(image);
      imageView.setPreserveRatio(true);
      imageView.setSmooth(true);
      imageView.setCache(true);
      return imageView;
    }
    throw new IllegalArgumentException("ImageView not created.Argument must not be null or empty.");
  }

  @Override
  public Either<Node, Pair<Node, Node>> createKeyVisual(Pane parentPane,
      Either<String, Pair<String, String>> either) {
    if (either.isLeft()) {
      return Either.left(getImageView(parentPane, either.left().get()));
    } else {
      return Either.right(createRightNodes(parentPane, either.right().get()));
    }
  }

  private Pair<Node, Node> createRightNodes(Pane parentPane, Pair<String, String> pair) {
    ImageView pkImageView = null;
    ImageView skImageView = null;
    try {
      pkImageView = getImageView(parentPane, pair.left());
    } catch (Exception e) {
      ExceptionDialog ed = new ExceptionDialog(e);
      ed.showAndWait();
    }
    try {
      skImageView = getImageView(parentPane, pair.right());
    } catch (Exception e) {
      ExceptionDialog ed = new ExceptionDialog(e);
      ed.showAndWait();
    }
    return new Pair<>(pkImageView, skImageView);
  }
}
