package io.wonderland.rh.analysis.language;

import io.wonderland.rh.analysis.AnalysisPane;
import io.wonderland.rh.base.pane.HToggleBox;
import io.wonderland.rh.utils.GuiUtils;
import io.wonderland.rq.cryptanalysis.LanguageFrequency;
import io.wonderland.rq.resource.ResourceConfig;
import io.wonderland.rq.type.Dichar;
import io.wonderland.rq.type.Language;
import io.wonderland.rq.type.Monochar;
import io.wonderland.rq.type.Trichar;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.fx.ChartCanvas;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LanguageFrequencyPane extends AnalysisPane<BorderPane> {

  private final Language language;
  private final BorderPane chartPane = new BorderPane();

  public LanguageFrequencyPane(String language) {
    super(new BorderPane());
    this.language = Language.findByName(language);
    this.build();
  }

  public LanguageFrequencyPane() {
    super(new BorderPane());
    this.language = Language.EN;
    this.build();
  }

  private void build() {
    VBox controlBox = getControlBox();
    container.setTop(controlBox);
    container.setCenter(this.chartPane);
  }

  private VBox getControlBox() {
    VBox controlBox = new VBox();
    controlBox.setSpacing(10);
    controlBox.getChildren().addAll(GuiUtils.getTitle(language + " language frequency"), createCharGram());
    return controlBox;
  }


  private HBox createCharGram() {
    HToggleBox<RadioButton> toggleGroupPane = new HToggleBox<>("Character gram :", 10, s -> new RadioButton(s),
        Map.of("monogram", () -> {
        }, "digram", () -> {
        }, "trigram", () -> {
        }, "quad gram", () -> {
        }));
    ToggleGroup toggleGroup = toggleGroupPane.getToggleGroup();
    toggleGroup.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
      if (Objects.nonNull(toggleGroup.getSelectedToggle())) {
        RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (Objects.nonNull(radioButton) && StringUtils.isNotEmpty(radioButton.getText())) {
          //added progress indicator
          this.createChart(this.chartPane, this.language, radioButton.getText());
        }
      }
    });
    return toggleGroupPane;
  }

  public void createChart(BorderPane chartPane, Language language, String radioButtonGraph) {
    XYSeriesCollection dataset = new XYSeriesCollection();
    XYSeries xySeries = null;
    List<XYTextAnnotation> textAnnotations = new ArrayList<>();
    if (ResourceConfig.MONOGRAPH.equals(radioButtonGraph)) {
      xySeries = new XYSeries(radioButtonGraph);

      Map<Monochar, Double> map = LanguageFrequency.monocharFreqPct(
          ResourceConfig.LANGUAGE_RESOURCES.get(language).get(radioButtonGraph));
      int i = 0;
      for (Entry<Monochar, Double> entry : map.entrySet()) {
        xySeries.add(i = i + 1, entry.getValue() * 100);
        XYTextAnnotation textAnnotation = new XYTextAnnotation(entry.getKey().toString(), i + 0.2,
            entry.getValue() * 100);
        textAnnotation.setPaint(Color.BLACK);
        textAnnotation.setFont(new Font("ARIAL", Font.BOLD, 10));
        textAnnotations.add(textAnnotation);
      }

    } else if (ResourceConfig.DIGRAPH.equals(radioButtonGraph)) {
      xySeries = new XYSeries(radioButtonGraph);

      Map<Dichar, Double> map = LanguageFrequency.dicharFreqPct(
          ResourceConfig.LANGUAGE_RESOURCES.get(language).get(radioButtonGraph));
      int i = 0;
      for (Entry<Dichar, Double> entry : map.entrySet()) {
        xySeries.add(i = i + 1, entry.getValue() * 100);
        XYTextAnnotation textAnnotation = new XYTextAnnotation(entry.getKey().toString(), i + 0.2,
            entry.getValue() * 100);
        textAnnotation.setPaint(Color.BLACK);
        textAnnotation.setFont(new Font("ARIAL", Font.BOLD, 10));
        textAnnotations.add(textAnnotation);
      }
    } else if (ResourceConfig.TRIGRAPH.equals(radioButtonGraph)) {
      xySeries = new XYSeries(radioButtonGraph);

      Map<Trichar, Double> map = LanguageFrequency.tricharFreqPct(
          ResourceConfig.LANGUAGE_RESOURCES.get(language).get(radioButtonGraph));
      int i = 0;
      for (Entry<Trichar, Double> entry : map.entrySet()) {
        xySeries.add(i = i + 1, entry.getValue() * 100);
        XYTextAnnotation textAnnotation = new XYTextAnnotation(entry.getKey().toString(), i + 0.2,
            entry.getValue() * 100);
        textAnnotation.setPaint(Color.BLACK);
        textAnnotation.setFont(new Font("ARIAL", Font.BOLD, 10));
        textAnnotations.add(textAnnotation);
      }
    }

    if (Objects.nonNull(xySeries)) {

      dataset.addSeries(xySeries);
      JFreeChart chart = ChartFactory.createScatterPlot(radioButtonGraph + " frequency",
          "Counter", "Pct %", dataset, PlotOrientation.VERTICAL, true, true, false);

      //Changes background color
      XYPlot plot = (XYPlot) chart.getPlot();
      plot.setBackgroundPaint(Color.WHITE);
      plot.setDomainGridlinesVisible(true);
      plot.setRangeGridlinesVisible(true);
      plot.setRangeGridlinePaint(Color.black);
      plot.setDomainGridlinePaint(Color.black);
      XYItemRenderer renderer = plot.getRenderer();

      //render dots in plot
      renderer.setSeriesShape(0, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[1]);
      renderer.setSeriesPaint(0, Color.RED);

      //Add text annotation into coordinates
      textAnnotations.stream().forEach(t -> plot.addAnnotation(t));

      // Create chart viewer
      ChartCanvas chartCanvas = new ChartCanvas(chart);
      chartCanvas.widthProperty().bind(chartPane.widthProperty());
      chartCanvas.heightProperty().bind(chartPane.heightProperty());

      //Update chart pane
      chartPane.getChildren().removeAll();
      chartPane.setCenter(chartCanvas);
    }
  }

  @Override
  public Map<String, Class<? extends AnalysisPane>> getChildren() {
    return MapUtils.EMPTY_SORTED_MAP;
  }

  public String getKey() {
    return language.getName();
  }

}
