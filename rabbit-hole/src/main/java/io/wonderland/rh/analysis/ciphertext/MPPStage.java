package io.wonderland.rh.analysis.ciphertext;

import io.wonderland.rq.cryptanalysis.FrequencyAnalysis;
import io.wonderland.rq.cryptanalysis.LanguageFrequency;
import io.wonderland.rq.cryptanalysis.MPPAlgorithm;
import io.wonderland.rq.ds.MPP.MPPValue;
import io.wonderland.rq.resource.ResourceConfig;
import io.wonderland.rq.type.Char;
import io.wonderland.rq.type.Dichar;
import io.wonderland.rq.type.Language;
import io.wonderland.rq.type.Monochar;
import io.wonderland.rq.type.Trichar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
public class MPPStage extends Stage {

  private final String title;
  private final double width;
  private final double height;
  private final BorderPane rootPane = new BorderPane();
  private final BorderPane borderPane = new BorderPane();
  private final Scene scene;
  private String language;
  private String langGraph;
  private String ciphertextEncoding;
  private String ciphertext;


  public MPPStage(String title, double width, double height) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.scene = initScene(title, width, height);
  }

  protected Scene initScene(String title, double width, double height) {
    //init scene & pane
    Scene scene = new Scene(this.rootPane, width, height);

    //init stage
    this.setTitle(title);
    this.setScene(scene);
    return scene;
  }


  public void createTable(String language, String langGraph, String ciphertextEncoding, String ciphertext) {
    if (Objects.nonNull(this.borderPane.getCenter())) {
      this.borderPane.getChildren().remove(this.borderPane.getCenter());
    }
    this.language = language;
    this.langGraph = langGraph;
    this.ciphertextEncoding = ciphertextEncoding;
    this.ciphertext = ciphertext;

    List<TableColumn> tableColumns = null;
    ObservableList<Map> tableItems = null;
    try {
      if (ResourceConfig.MONOGRAPH.equals(langGraph)) {
        Map<Monochar, Double> langMap = LanguageFrequency.monocharFreqPct(
            ResourceConfig.LANGUAGE_RESOURCES.get(Language.findByName(language)).get(langGraph));
        Map<Monochar, Double> ciphertextMap = FrequencyAnalysis.monocharFreqPct(ciphertext.getBytes(), 1);
        Map<Monochar, List<MPPValue<? extends Char>>> analysisMap = MPPAlgorithm.monocharCalc(langMap, ciphertextMap);
        tableColumns = createTableColumns(analysisMap);
        tableItems = createTableItems(analysisMap);

      } else if (ResourceConfig.DIGRAPH.equals(langGraph)) {
        Map<Dichar, Double> langMap = LanguageFrequency.dicharFreqPct(
            ResourceConfig.LANGUAGE_RESOURCES.get(Language.findByName(language)).get(langGraph));
        Map<Dichar, Double> ciphertextMap = FrequencyAnalysis.dicharFreqPct(ciphertext.getBytes(), 1);
        Map<Dichar, List<MPPValue<? extends Char>>> analysisMap = MPPAlgorithm.dicharCalc(langMap, ciphertextMap);
        tableColumns = createTableColumns(analysisMap);
        tableItems = createTableItems(analysisMap);

      } else if (ResourceConfig.TRIGRAPH.equals(langGraph)) {
        Map<Trichar, Double> langMap = LanguageFrequency.tricharFreqPct(
            ResourceConfig.LANGUAGE_RESOURCES.get(Language.findByName(language)).get(langGraph));
        tableColumns = createTableColumns(langMap);

      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    TableView tableView = new TableView();
    tableView.setEditable(true);
    tableView.setColumnResizePolicy(p -> true);
    tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    if (Objects.nonNull(tableColumns)) {
      tableView.getColumns().addAll(tableColumns);
    }
    if (Objects.nonNull(tableItems)) {
      tableView.setItems(tableItems);
    }
    this.borderPane.setCenter(tableView);
    this.rootPane.setCenter(this.borderPane);
  }


  private <T> ObservableList createTableItems(Map<? extends Char, List<MPPValue<? extends Char>>> analysisMap) {
    ObservableList<Map<String, String>> data = FXCollections.observableArrayList();
    OptionalInt maxListSize = analysisMap.entrySet().stream()
        .mapToInt(e -> Objects.nonNull(e.getValue()) ? e.getValue().size() : 0).max();
    maxListSize.ifPresent(max -> {
      for (int i = 0; i < max; i++) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<? extends Char, List<MPPValue<? extends Char>>> entry : analysisMap.entrySet()) {
          String key = entry.getKey().toString();
          MPPValue mppValue = (Objects.nonNull(entry.getValue()) && CollectionUtils.isNotEmpty(entry.getValue())
              && entry.getValue().size() > i) ? entry.getValue().get(i) : null;
          String value = Objects.nonNull(mppValue) ? mppValue.getKey().toString() : "";
          map.put(key, value);
        }
        data.add(map);
      }
    });
    return data;
  }


  public <T> List<TableColumn> createTableColumns(Map<? extends Char, T> analysisMap) {
    List<TableColumn> tableColumns = analysisMap.entrySet().stream().map(e -> {
      TableColumn tableColumn = new TableColumn(e.getKey().toString());
      tableColumn.setCellValueFactory(new MapValueFactory<>(e.getKey().toString()));
      return tableColumn;
    }).collect(Collectors.toList());
    log.info("table columns " + tableColumns.size());
    return tableColumns;
  }
}
