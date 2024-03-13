package io.wonderland.rh.misc;

import io.wonderland.rh.utils.LabelUtils;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MiscPane extends BorderPane {

  private final HBox infoBox = new HBox();

  private Optional<Misc> optionalMisc;

  MiscPane(Optional<Misc> optionalMisc){
    this.optionalMisc=optionalMisc;
    this.build();
  }

  private void build(){
    this.setTop(getToolPanel());
    this.updateToolPanel();
  }

  private void updateToolPanel() {
    if(optionalMisc.isPresent()){
      if (!infoBox.getChildren().isEmpty()) {
        this.infoBox.getChildren().remove(0);
      }
      Misc misc=optionalMisc.get();
      if(misc.equals(Misc.ENCODING)){
        this.setCenter(new EncodingPane());
        this.infoBox.getChildren().add(new Label(" "));
      }
    } else {
      this.infoBox.getChildren().add(LabelUtils.getTitle("Please choose on misc option on the left."));
    }
  }



  private VBox getToolPanel() {
    final VBox miscBox = new VBox();
    miscBox.setPadding(new Insets(5,5,5,5));
    miscBox.setSpacing(10);

    //info labels
    this.infoBox.getChildren().add(LabelUtils.getTitle("Choose o misc option on the left..."));

    miscBox.getChildren().addAll(this.infoBox);
    return miscBox;
  }


}
