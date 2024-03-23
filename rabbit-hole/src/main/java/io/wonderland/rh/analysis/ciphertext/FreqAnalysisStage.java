package io.wonderland.rh.analysis.ciphertext;

import static io.wonderland.rh.analysis.ciphertext.CiphertextPane.CIPHERTEXT;

import io.wonderland.rq.cryptanalysis.FrequencyAnalysis;
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
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class FreqAnalysisStage extends Stage {

  private final String title;
  private final double width;
  private final double height;

  private final BorderPane rootPane = new BorderPane();
  private final ScrollPane scrollPane = new ScrollPane();
  private final BorderPane chartPane = new BorderPane();
  private final Scene scene;
  private String language;
  private String langGraph;
  private String ciphertextEncoding;
  private String ciphertext;


  public FreqAnalysisStage(String title, double width, double height) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.scene = initScene(title, width, height);
  }

  protected Scene initScene(String title, double width, double height) {
    //init scene & pane
    Scene scene = new Scene(rootPane, width, height);

    //Init scroll pane
    this.scrollPane.setFitToWidth(true);
    this.scrollPane.setFitToHeight(true);
    this.scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
    this.scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    this.scrollPane.setContent(chartPane);
    this.rootPane.setCenter(scrollPane);

    //init stage
    this.setTitle(title);
    this.setScene(scene);
    return scene;
  }


  public void createChart(String language, String langGraph, String ciphertextEncoding, String ciphertext) {
    XYSeriesCollection dataset = new XYSeriesCollection();

    XYSeries languageSeries = null;
    XYSeries ciphertextSeries = null;
    List<XYTextAnnotation> plaintextAnnotations = new ArrayList<>();
    List<XYTextAnnotation> ciphertextAnnotations = new ArrayList<>();

    try {
      if (ResourceConfig.MONOGRAPH.equals(langGraph)) {
        languageSeries = new XYSeries(language);
        ciphertextSeries = new XYSeries(CIPHERTEXT);

        Map<Monochar, Double> langMap = LanguageFrequency.monocharFreqPct(
            ResourceConfig.LANGUAGE_RESOURCES.get(Language.findByName(language)).get(langGraph));
        int i = 0;
        for (Entry<Monochar, Double> entry : langMap.entrySet()) {
          languageSeries.add(i = i + 1, entry.getValue() * 100);
          XYTextAnnotation textAnnotation = new XYTextAnnotation(entry.getKey().toString(), i + 0.2,
              entry.getValue() * 100);
          textAnnotation.setPaint(Color.BLACK);
          textAnnotation.setFont(new Font("ARIAL", Font.BOLD, 10));
          plaintextAnnotations.add(textAnnotation);
        }

        Map<Monochar, Double> ciphertextMap = FrequencyAnalysis.monocharFreqPct(ciphertext.getBytes(), 1);
        int j = 0;
        for (Entry<Monochar, Double> entry : ciphertextMap.entrySet()) {
          ciphertextSeries.add(j = j + 1, entry.getValue() * 100);
          XYTextAnnotation textAnnotation = new XYTextAnnotation(entry.getKey().toString(), j + 0.2,
              entry.getValue() * 100);
          textAnnotation.setPaint(Color.BLACK);
          textAnnotation.setFont(new Font("ARIAL", Font.BOLD, 10));
          ciphertextAnnotations.add(textAnnotation);
        }

      } else if (ResourceConfig.DIGRAPH.equals(langGraph)) {
        languageSeries = new XYSeries(language);
        ciphertextSeries = new XYSeries(CIPHERTEXT);

        Map<Dichar, Double> langMap = LanguageFrequency.dicharFreqPct(
            ResourceConfig.LANGUAGE_RESOURCES.get(Language.findByName(language)).get(langGraph));
        int i = 0;
        for (Entry<Dichar, Double> entry : langMap.entrySet()) {
          languageSeries.add(i = i + 1, entry.getValue() * 100);
          XYTextAnnotation textAnnotation = new XYTextAnnotation(entry.getKey().toString(), i + 0.3,
              entry.getValue() * 100);
          textAnnotation.setPaint(Color.BLACK);
          textAnnotation.setFont(new Font("ARIAL", Font.BOLD, 10));
          plaintextAnnotations.add(textAnnotation);
        }

        Map<Dichar, Double> ciphertextMap = FrequencyAnalysis.dicharFreqPct(ciphertext.getBytes(), 1);
        int j = 0;
        for (Entry<Dichar, Double> entry : ciphertextMap.entrySet()) {
          ciphertextSeries.add(j = j + 1, entry.getValue() * 100);
          XYTextAnnotation textAnnotation = new XYTextAnnotation(entry.getKey().toString(), j + 0.3,
              entry.getValue() * 100);
          textAnnotation.setPaint(Color.BLACK);
          textAnnotation.setFont(new Font("ARIAL", Font.BOLD, 10));
          ciphertextAnnotations.add(textAnnotation);
        }
      } else if (ResourceConfig.TRIGRAPH.equals(langGraph)) {
        languageSeries = new XYSeries(language);

        Map<Trichar, Double> map = LanguageFrequency.tricharFreqPct(
            ResourceConfig.LANGUAGE_RESOURCES.get(Language.findByName(language)).get(langGraph));
        int i = 0;
        for (Entry<Trichar, Double> entry : map.entrySet()) {
          languageSeries.add(i = i + 1, entry.getValue() * 100);
          XYTextAnnotation textAnnotation = new XYTextAnnotation(entry.getKey().toString(), i + 0.4,
              entry.getValue() * 100);
          textAnnotation.setPaint(Color.BLACK);
          textAnnotation.setFont(new Font("ARIAL", Font.BOLD, 10));
          plaintextAnnotations.add(textAnnotation);
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    //Add series to datasets
    if (Objects.nonNull(languageSeries)) {
      dataset.addSeries(languageSeries);
    }
    if (Objects.nonNull(ciphertextSeries)) {
      dataset.addSeries(ciphertextSeries);
    }

    JFreeChart chart = ChartFactory.createScatterPlot(langGraph + " frequency",
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

    renderer.setSeriesShape(1, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[0]);
    renderer.setSeriesPaint(1, Color.BLUE);

    //Add text annotation into coordinates
    plaintextAnnotations.stream().forEach(t -> plot.addAnnotation(t));
    ciphertextAnnotations.stream().forEach(t -> plot.addAnnotation(t));

    // Create chart viewer
    ChartCanvas chartCanvas = new ChartCanvas(chart);
    chartCanvas.widthProperty().bind(this.chartPane.widthProperty());
    chartCanvas.heightProperty().bind(this.chartPane.heightProperty());
    this.chartPane.setCenter(chartCanvas);
  }
}
