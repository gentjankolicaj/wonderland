package io.wonderland.rh.keygen;

import static io.wonderland.rh.GlobalConstants.BORDER_PANE_INSETS;

import io.atlassian.fugue.Either;
import io.atlassian.fugue.Pair;
import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.barcode.Barcode;
import io.wonderland.rh.base.codec.CodecAlg;
import io.wonderland.rh.base.fx.CodecDropdown;
import io.wonderland.rh.base.fx.CodecDropdownItem;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.observer.KeygenObservable;
import io.wonderland.rh.base.observer.TypeObserver;
import io.wonderland.rh.utils.CodecUtils;
import io.wonderland.rh.utils.GuiUtils;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Objects;
import java.util.function.Function;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class KeyPairGeneratorPane extends BorderPane {

  private static final double QR_CODE_TO_VIEW_RATIO = 0.8;
  private final TypeObserver<KeyPair> keyPairTypeObserver = new TypeObserver<>();
  private final Label pkLbl = GuiUtils.getTitle(GlobalConstants.PUBLIC_KEY);
  private final Label skLbl = GuiUtils.getTitle(GlobalConstants.PRIVATE_KEY);
  private final Label copyPKEncodingLbl = new Label();
  private final Label copySKEncodingLbl = new Label();
  private final TextField pkTF = new TextField();
  private final TextField skTF = new TextField();
  private final KeyPairGenerator keyPairGenerator;
  private final KeygenObservable keygenObservable;
  private KeyPair keyPair;
  private final CodecDropdown codecDropdown = CodecDropdown.createCodecWithBarcode(keyPairFunc());

  public KeyPairGeneratorPane(KeyPairGenerator keyPairGenerator,
      KeygenObservable keygenObservable) {
    this.keyPairGenerator = keyPairGenerator;
    this.keygenObservable = keygenObservable;
    build();
  }

  private void build() {
    this.setTop(codecDropdown);
    this.buildLabels();
    this.setEventHandlers();
    BorderPane.setMargin(codecDropdown, BORDER_PANE_INSETS);
  }

  private void buildLabels() {
    this.copyPKEncodingLbl.setGraphic(
        GuiUtils.getIconClasspath("/icons/copy-encoding/icons8-copy-24.png"));
    this.copyPKEncodingLbl.setTooltip(new Tooltip("Copy public key encoding"));
    this.copySKEncodingLbl.setGraphic(
        GuiUtils.getIconClasspath("/icons/copy-encoding/icons8-copy-24.png"));
    this.copySKEncodingLbl.setTooltip(new Tooltip("Copy private key encoding"));
  }

  private void setEventHandlers() {
    this.copyPKEncodingLbl.setOnMouseReleased(event -> {
      try {
        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.PLAIN_TEXT,
            CodecUtils.encodeBase10(keyPair.getPublic().getEncoded(), ' '));
        Clipboard.getSystemClipboard().setContent(content);
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    });

    this.copySKEncodingLbl.setOnMouseReleased(event -> {
      try {
        ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.PLAIN_TEXT,
            CodecUtils.encodeBase10(keyPair.getPrivate().getEncoded(), ' '));
        Clipboard.getSystemClipboard().setContent(content);
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    });

  }

  public void updateKeyPairBarcode(Pair<Node, Node> pair) {
    HBox keyPairContainer = new HBox();
    keyPairContainer.setSpacing(GlobalConstants.SPACING);
    try {

      //public key labels
      HBox pkContainer = new HBox();
      pkContainer.getChildren().addAll(pkLbl, copyPKEncodingLbl);
      pkContainer.setSpacing(GlobalConstants.SPACING);

      //get public key barcode
      VBox.setVgrow(pair.left(), Priority.ALWAYS);
      VBox parentContainer = new VBox(pkContainer, pair.left());
      parentContainer.setSpacing(GlobalConstants.SPACING);

      keyPairContainer.getChildren().add(parentContainer);
    } catch (Exception e) {
      ExceptionDialog ed = new ExceptionDialog(e);
      ed.showAndWait();
    }
    try {
      //private key labels
      HBox skContainer = new HBox();
      skContainer.getChildren().addAll(skLbl, copySKEncodingLbl);
      skContainer.setSpacing(GlobalConstants.SPACING);

      //get private key barcode
      VBox.setVgrow(pair.right(), Priority.ALWAYS);
      VBox parentContainer = new VBox(skContainer, pair.right());
      parentContainer.setSpacing(GlobalConstants.SPACING);

      keyPairContainer.getChildren().add(parentContainer);
    } catch (Exception e) {
      ExceptionDialog ed = new ExceptionDialog(e);
      ed.showAndWait();
    }
    this.setCenter(keyPairContainer);
    BorderPane.setMargin(keyPairContainer, BORDER_PANE_INSETS);
  }

  private void updateKeyPair(String pkEncoded, String skEncoded) {
    pkTF.setText(pkEncoded);
    pkTF.setEditable(false);
    skTF.setText(skEncoded);
    skTF.setEditable(false);

    HBox pkContainer = new HBox();
    HBox.setHgrow(pkTF, Priority.ALWAYS);
    pkContainer.getChildren().addAll(pkLbl, copyPKEncodingLbl, pkTF);
    pkContainer.setSpacing(GlobalConstants.SPACING);

    HBox skContainer = new HBox();
    HBox.setHgrow(skTF, Priority.ALWAYS);
    skContainer.getChildren().addAll(skLbl, copySKEncodingLbl, skTF);
    skContainer.setSpacing(GlobalConstants.SPACING);

    VBox keyPairContainer = new VBox();
    keyPairContainer.setSpacing(GlobalConstants.SPACING);
    keyPairContainer.getChildren().addAll(pkContainer, skContainer);
    setCenter(keyPairContainer);
    BorderPane.setMargin(keyPairContainer, BORDER_PANE_INSETS);
  }


  public void generate() {
    this.keyPair = keyPairGenerator.generateKeyPair();
    //notify observers & update pane
    this.keygenObservable.notifyObservers(Either.right(keyPair));
    this.keyPairTypeObserver.update(keyPair);
    this.applySelectedCodec();
  }

  private void applySelectedCodec() {
    CodecDropdownItem codecDropdownItem = this.codecDropdown.getSelectedDropdownItem();
    CodecAlg<byte[], String, String, byte[]> codecAlg = codecDropdownItem.getCodecAlg();
    codecDropdownItem.getFunc().apply(codecAlg);
  }

  /**
   * Function applied when numeral base is changed at dropdown.
   *
   * @return functional interface impl
   */
  private Function<CodecAlg<byte[], String, String, byte[]>, Void> keyPairFunc() {
    return codecAlg -> {
      KeyPair contextKeyPair = getKeyPair();
      if (Objects.nonNull(contextKeyPair)) {

        //encode secret bytes according to current codec
        String pkEncoded = codecAlg.encode().apply(contextKeyPair.getPublic().getEncoded());
        String skEncoded = codecAlg.encode().apply(contextKeyPair.getPrivate().getEncoded());

        if (codecAlg instanceof Barcode) {
          Barcode barcode = (Barcode) codecAlg;
          try {
            Either<Node, Pair<Node, Node>> either = barcode.createKeyVisual(this,
                Either.right(new Pair<>(pkEncoded, skEncoded)));
            this.updateKeyPairBarcode(either.right().get());
          } catch (Exception e) {
            ExceptionDialog ed = new ExceptionDialog(e);
            ed.showAndWait();
          }
        } else {
          //update pane with encoded
          this.updateKeyPair(pkEncoded, skEncoded);
        }
      }
      return null;
    };
  }


}
