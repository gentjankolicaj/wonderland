package io.wonderland.rh.base.barcode;

import com.google.zxing.WriterException;
import io.atlassian.fugue.Either;
import io.atlassian.fugue.Pair;
import io.wonderland.rh.base.codec.CodecAlg;
import java.util.function.Function;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;

/**
 * Barcode is method of representing data in a visual, machine-readable form.
 */
@RequiredArgsConstructor
public abstract class Barcode implements CodecAlg<byte[], String, String, byte[]> {

  private final CodecAlg<byte[], String, String, byte[]> codecAlg;

  @Override
  public Function<byte[], String> encode() {
    return codecAlg.encode();
  }

  @Override
  public Function<String, byte[]> decode() {
    return codecAlg.decode();
  }

  @Override
  public String getName() {
    return this.getClass().getSimpleName() + "_" + codecAlg.getName();
  }

  public abstract Either<Node, Pair<Node, Node>> createKeyVisual(Pane parentPane,
      Either<String, Pair<String, String>> either) throws WriterException;

}
