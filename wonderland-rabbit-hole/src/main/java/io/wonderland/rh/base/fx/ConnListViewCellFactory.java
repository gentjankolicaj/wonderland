package io.wonderland.rh.base.fx;

import java.nio.file.Path;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public final class ConnListViewCellFactory implements Callback<ListView<Path>, ListCell<Path>> {

  @Override
  public ListCell<Path> call(ListView<Path> paths) {
    return new ListCell<>() {
      @Override
      protected void updateItem(Path item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
          setGraphic(null);
        } else {
          setText(null);
          setGraphic(new ConnListViewCell(item));
        }
      }
    };
  }

}
