package io.wonderland.rh.cryptanalysis;

import io.wonderland.rh.common.LabelPane;
import io.wonderland.rq.type.Language;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.collections4.CollectionUtils;

public class CryptanalysisTab extends Tab {

  private static final Map<String, Pane> WORK_PANES = createWorkPanes();
  private final Stage primaryStage;
  private final StackPane contextWorkPane = new StackPane();
  private SplitPane rootSplitter;

  public CryptanalysisTab(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.setText("Cryptanalysis");
    this.setClosable(false);

    SplitPane splitPane = new SplitPane();

    //stack pane
    final StackPane stackPane = new StackPane();
    stackPane.getChildren().add(createAnalysisOptionsPane());

    this.contextWorkPane.getChildren().add(WORK_PANES.get("Analysis"));

    splitPane.getItems().addAll(stackPane, contextWorkPane);
    splitPane.setDividerPositions(0.3f, 0.6f);

    this.setContent(splitPane);
  }


  public static Map<String, Pane> createWorkPanes() {
    Map<String, Pane> map = new HashMap<>();
    map.put("Analysis", new LabelPane("Welcome to cryptanalysis."));
    map.put("Language freq", new LabelPane("Select language for frequency analysis"));
    map.put("Ciphertext analysis", new CiphertextAnalysisPane());
    return map;
  }


  private StackPane createAnalysisOptionsPane() {
    TreeItem<String> rootItem = new TreeItem<>("~/", null);
    rootItem.setExpanded(true);

    //add language freq item
    TreeItem<String> languageFreqItem = new TreeItem<>("Language freq", null);
    rootItem.getChildren().add(languageFreqItem);
    languageFreqItem.getChildren().addAll(getLanguageTreeItems());

    //Add cipher text analysis
    TreeItem<String> ciphertextItem = new TreeItem<>("Ciphertext analysis", null);
    rootItem.getChildren().add(ciphertextItem);

    TreeView<String> treeView = new TreeView<>(rootItem);
    treeView.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends TreeItem<String>> observableValue,
            TreeItem<String> oldItem, TreeItem<String> newItem) -> selectWorkPane(oldItem, newItem));

    StackPane cipherStackPane = new StackPane();
    cipherStackPane.getChildren().add(treeView);
    return cipherStackPane;
  }

  private List<TreeItem<String>> getLanguageTreeItems() {
    return Arrays.stream(Language.values()).map(e -> new TreeItem<>(e.getName(), null)).collect(Collectors.toList());
  }

  private void selectWorkPane(TreeItem<String> oldItem, TreeItem<String> newItem) {
    if (CollectionUtils.isNotEmpty(this.contextWorkPane.getChildren())) {
      this.contextWorkPane.getChildren().removeAll(this.contextWorkPane.getChildren());
    }

    if (WORK_PANES.get(newItem.getValue()) == null) {
      //build language pane
      this.contextWorkPane.getChildren().add(new LanguageFrequencyPane(newItem.getValue()));
    } else {
      this.contextWorkPane.getChildren().add(WORK_PANES.get(newItem.getValue()));
    }
  }


}

