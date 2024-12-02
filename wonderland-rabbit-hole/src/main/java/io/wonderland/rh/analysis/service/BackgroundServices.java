package io.wonderland.rh.analysis.service;

import io.wonderland.base.ApplicationException;
import io.wonderland.base.FilesUtils;
import io.wonderland.base.Map.KeyOption;
import io.wonderland.garden.api.GardenClient;
import io.wonderland.garden.domain.Grapheme;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rq.cryptanalysis.FrequencyAnalysis;
import io.wonderland.rq.cryptanalysis.IndexCoincidence;
import io.wonderland.struct.GramType;
import io.wonderland.struct.Language;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartCanvas;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public final class BackgroundServices {

  public static final String FONT_NAME = "ARIAL";
  public static final String CREATING_CHART_LBL = "Creating chart...";
  private static final String FILE_DIR =
      System.getProperty("user.dir") + File.separatorChar + "letter_freq";

  @Setter
  private static GardenClient gardenClient = null;

  private BackgroundServices() {
  }

  public static String getFilePath(Language language, String gramType) {
    return FILE_DIR + File.separatorChar + language.getCode() + "_" + gramType;
  }


  @Slf4j
  @RequiredArgsConstructor
  public static class CiphertextFreqService extends Service<BorderPane> {

    private final StackPane rootContainer;
    private final BorderPane container;

    @Getter
    private Language language;

    @Getter
    private GramType gramType;

    @Getter
    private TextArea textArea;

    @Override
    protected Task<BorderPane> createTask() {
      return new Task<>() {
        @Override
        protected BorderPane call() {
          return createChart(getLanguage(), getGramType(), getTextArea().getText());
        }
      };
    }

    public void chartProcess(Language language, GramType gramType, TextArea textArea) {
      this.language = language;
      this.gramType = gramType;
      this.textArea = textArea;

      //setup progress indicator container
      VBox progressBox = new VBox();
      progressBox.setAlignment(Pos.CENTER);
      ProgressIndicator progressIndicator = new ProgressIndicator();
      Label label = new Label(CREATING_CHART_LBL);
      progressBox.getChildren().addAll(progressIndicator, label);
      rootContainer.getChildren().add(progressBox);
      container.setDisable(true);

      //bind properties
      progressIndicator.progressProperty().bind(progressProperty());
      progressIndicator.visibleProperty().bind(runningProperty());

      //start grapheme task to download resources
      if (!isRunning()) {
        restart();
      }

      //add listeners for each event on this service
      setOnSucceeded(event -> {
        //on succeeded event update with new chart
        container.setCenter((BorderPane) event.getSource().getValue());

        //remove progress box
        rootContainer.getChildren().remove(progressBox);
        container.setDisable(false);
      });
      setOnFailed(event -> {
        rootContainer.getChildren().remove(progressBox);
        container.setDisable(false);
        ExceptionDialog ed = new ExceptionDialog(event.getSource().getException());
        ed.showAndWait();
      });
      setOnCancelled(event -> {
        rootContainer.getChildren().remove(progressBox);
        container.setDisable(false);
      });
    }

    private BorderPane createChart(Language language, GramType gramType, String ciphertext) {
      //series definition
      XYSeries letterSeries = new XYSeries(gramType.getValue());
      XYSeries ciphertextSeries = new XYSeries("ciphertext");

      //text annotations
      List<XYTextAnnotation> letterAnn = new ArrayList<>();
      List<XYTextAnnotation> ciphertextAnn = new ArrayList<>();
      String letterFilePath;
      Map<String, Double> gramFreqMap = null;
      try {
        //loading form file system
        // if some error happens while loading resources , try to download form beginning.
        letterFilePath = getFilePath(language, gramType.getValue());
        gramFreqMap = FilesUtils.fileToMap(letterFilePath, KeyOption.OVERWRITE, ":", s -> s,
            Double::parseDouble);

        //if no resources try to download again
        if (MapUtils.isEmpty(gramFreqMap)) {
          GraphemeService.downloadSync(gardenClient, language);
          letterFilePath = getFilePath(language, gramType.getValue());
          gramFreqMap = FilesUtils.fileToMap(letterFilePath, KeyOption.OVERWRITE, ":", s -> s,
              Double::parseDouble);
        }
      } catch (Exception e) {
        log.error("{}", e.getMessage(), e);
      }

      Map<String, Double> gramFreqPct = FrequencyAnalysis.calcPct(
          MapUtils.emptyIfNull(gramFreqMap));
      //compute letter details
      int ctr = 0;
      for (Entry<String, Double> entry : gramFreqPct.entrySet()) {
        ctr = ctr + 1;
        letterSeries.add(ctr, entry.getValue() * 100);
        XYTextAnnotation ann = new XYTextAnnotation(entry.getKey(), ctr + 0.2,
            entry.getValue() * 100);
        ann.setPaint(Color.BLACK);
        ann.setFont(new Font(FONT_NAME, Font.BOLD, 10));
        letterAnn.add(ann);
      }

      //compute ciphertext details
      Map<String, Double> ciphertextMap = FrequencyAnalysis.calcFreqPct(ciphertext.getBytes(),
          gramType);

      int j = 0;
      for (Entry<String, Double> entry : ciphertextMap.entrySet()) {
        j = j + 1;
        ciphertextSeries.add(j, entry.getValue() * 100);
        XYTextAnnotation textAnnotation = new XYTextAnnotation(entry.getKey(), j + 0.3,
            entry.getValue() * 100);
        textAnnotation.setPaint(Color.BLACK);
        textAnnotation.setFont(new Font(FONT_NAME, Font.BOLD, 10));
        ciphertextAnn.add(textAnnotation);
      }

      //dataset
      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(letterSeries);
      dataset.addSeries(ciphertextSeries);
      JFreeChart chart = ChartFactory.createScatterPlot(gramType.getValue() + " frequency",
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

      //render dots in plot
      renderer.setSeriesShape(0, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[0]);
      renderer.setSeriesPaint(0, Color.RED);

      renderer.setSeriesShape(1, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE[1]);
      renderer.setSeriesPaint(1, Color.BLUE);

      //Add text annotation into coordinates
      letterAnn.forEach(plot::addAnnotation);
      ciphertextAnn.forEach(plot::addAnnotation);

      //create canvas container
      BorderPane chartContainer = new BorderPane();

      // Create chart viewer
      ChartCanvas chartCanvas = new ChartCanvas(chart);
      chartCanvas.widthProperty().bind(chartContainer.widthProperty());
      chartCanvas.heightProperty().bind(chartContainer.heightProperty());
      chartContainer.setCenter(chartCanvas);
      return chartContainer;
    }
  }


  @RequiredArgsConstructor
  public static class IOCService extends Service<BorderPane> {

    static final String Y_AXIS_LABEL = "Index of Coincidence";
    static final String X_AXIS_LABEL = "Key length";
    private final StackPane rootContainer;
    private final BorderPane container;


    @Getter
    private int keyMinLength;

    @Getter
    private int keyMaxLength;

    @Getter
    private TextArea textArea;

    @Override
    protected Task<BorderPane> createTask() {
      return new Task<>() {
        @Override
        protected BorderPane call() {
          return createChart(getKeyMinLength(), getKeyMaxLength(), getTextArea().getText());
        }
      };
    }

    public void chartProcess(String keyMinLength, String keyMaxLength, TextArea textArea) {
      this.textArea = textArea;
      this.keyMinLength = Integer.parseInt(keyMinLength);
      this.keyMaxLength = Integer.parseInt(keyMaxLength);

      //setup progress indicator container
      VBox progressBox = new VBox();
      progressBox.setAlignment(Pos.CENTER);
      ProgressIndicator progressIndicator = new ProgressIndicator();
      Label label = new Label(CREATING_CHART_LBL);
      progressBox.getChildren().addAll(progressIndicator, label);
      rootContainer.getChildren().add(progressBox);
      container.setDisable(true);

      //bind properties
      progressIndicator.progressProperty().bind(progressProperty());
      progressIndicator.visibleProperty().bind(runningProperty());

      //start grapheme task to download resources
      if (!isRunning()) {
        restart();
      }

      //add listeners for each event on this service
      setOnSucceeded(event -> {
        //on succeeded event update with new chart
        container.setCenter((BorderPane) event.getSource().getValue());

        //remove progress box
        rootContainer.getChildren().remove(progressBox);
        container.setDisable(false);
      });
      setOnFailed(event -> {
        rootContainer.getChildren().remove(progressBox);
        container.setDisable(false);
        ExceptionDialog ed = new ExceptionDialog(event.getSource().getException());
        ed.showAndWait();
      });
      setOnCancelled(event -> {
        rootContainer.getChildren().remove(progressBox);
        container.setDisable(false);
      });
    }

    private BorderPane createChart(int keyMinLength, int keyMaxLength, String ciphertext) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
      Map<Integer, Double> ics = IndexCoincidence.calcKeyPeriodIC(ciphertext.getBytes(), 1,
          keyMinLength, keyMaxLength);
      if (MapUtils.isNotEmpty(ics)) {
        ics.forEach((key, value) -> dataset.addValue(value, X_AXIS_LABEL, key));
      }

      //add first dataset and render as bar values
      BarRenderer renderer = new BarRenderer();
      renderer.setSeriesPaint(0, Color.blue);

      CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis(X_AXIS_LABEL),
          new NumberAxis(Y_AXIS_LABEL),
          renderer);

      JFreeChart chart = new JFreeChart(plot);
      chart.addSubtitle(new TextTitle(
          "Ciphertext IOC for key length [" + keyMinLength + " - " + keyMaxLength + "]"));
      chart.setBackgroundPaint(Color.white);

      //create canvas container
      BorderPane chartContainer = new BorderPane();

      // Create chart viewer
      ChartCanvas chartCanvas = new ChartCanvas(chart);
      chartCanvas.widthProperty().bind(chartContainer.widthProperty());
      chartCanvas.heightProperty().bind(chartContainer.heightProperty());
      chartContainer.setCenter(chartCanvas);
      return chartContainer;
    }
  }


  @RequiredArgsConstructor
  public static class LetterFreqService extends Service<BorderPane> {

    private final StackPane rootContainer;
    private final BorderPane container;

    @Getter
    private Language language;

    @Getter
    private GramType gramType;

    @Override
    protected Task<BorderPane> createTask() {
      return new Task<>() {
        @Override
        protected BorderPane call() {
          return createChart(getLanguage(), getGramType());
        }
      };
    }


    public void chartProcess(Language language, GramType gramType) {
      this.language = language;
      this.gramType = gramType;

      //setup progress indicator container
      VBox progressBox = new VBox();
      progressBox.setAlignment(Pos.CENTER);
      ProgressIndicator progressIndicator = new ProgressIndicator();
      Label label = new Label(CREATING_CHART_LBL);
      progressBox.getChildren().addAll(progressIndicator, label);
      rootContainer.getChildren().add(progressBox);
      container.setDisable(true);

      //bind properties
      progressIndicator.progressProperty().bind(progressProperty());
      progressIndicator.visibleProperty().bind(runningProperty());

      //start grapheme task to download resources
      if (!isRunning()) {
        restart();
      }

      //add listeners for each event on this service
      setOnSucceeded(event -> {
        //on succeeded event update with new chart
        container.setCenter((BorderPane) event.getSource().getValue());

        //remove progress box
        rootContainer.getChildren().remove(progressBox);
        container.setDisable(false);
      });
      setOnFailed(event -> {
        rootContainer.getChildren().remove(progressBox);
        container.setDisable(false);
        ExceptionDialog ed = new ExceptionDialog(event.getSource().getException());
        ed.showAndWait();
      });
      setOnCancelled(event -> {
        rootContainer.getChildren().remove(progressBox);
        container.setDisable(false);
      });
    }

    private BorderPane createChart(Language language, GramType gramType) {
      XYSeries xySeries = new XYSeries(gramType.getValue());
      List<XYTextAnnotation> textAnnotations = new ArrayList<>();
      String filePath = getFilePath(language, gramType.getValue());
      Map<String, Double> gramFreqMap = FilesUtils.fileToMap(filePath, KeyOption.OVERWRITE, ":",
          s -> s,
          Double::parseDouble);
      Map<String, Double> gramFreqPct = FrequencyAnalysis.calcPct(gramFreqMap);
      int ctr = 0;
      for (Entry<String, Double> entry : gramFreqPct.entrySet()) {
        ctr = ctr + 1;
        xySeries.add(ctr, entry.getValue() * 100);
        XYTextAnnotation ann = new XYTextAnnotation(entry.getKey(), ctr + 0.2,
            entry.getValue() * 100);
        ann.setPaint(Color.BLACK);
        ann.setFont(new Font(FONT_NAME, Font.BOLD, 10));
        textAnnotations.add(ann);
      }

      XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(xySeries);
      JFreeChart chart = ChartFactory.createScatterPlot(gramType.getValue() + " frequency",
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
      textAnnotations.forEach(plot::addAnnotation);

      //create canvas container
      BorderPane chartContainer = new BorderPane();

      // Create chart viewer
      ChartCanvas chartCanvas = new ChartCanvas(chart);
      chartCanvas.widthProperty().bind(chartContainer.widthProperty());
      chartCanvas.heightProperty().bind(chartContainer.heightProperty());
      chartContainer.setCenter(chartCanvas);
      return chartContainer;
    }
  }


  @RequiredArgsConstructor
  public static class GraphemeService extends Service<Void> {

    private final Language language;
    private final StackPane container;
    private final BorderPane childContainer;

    protected static void downloadAsync(GardenClient gardenClient, Language language) {
      Grapheme grapheme = gardenClient.getGrapheme(language.getCode());
      if (MapUtils.isNotEmpty(grapheme.getFreq())) {
        if (createFileDir()) {
          for (Map.Entry<String, byte[]> entry : grapheme.getFreq().entrySet()) {
            String filePath = getFilePath(language, entry.getKey());
            FilesUtils.writeFileAsync(filePath, entry.getValue());
          }
        } else {
          throw new ApplicationException("Failed to create directory: " + FILE_DIR);
        }
      }
    }

    protected static void downloadSync(GardenClient gardenClient, Language language) {
      Grapheme grapheme = gardenClient.getGrapheme(language.getCode());
      if (MapUtils.isNotEmpty(grapheme.getFreq())) {
        if (createFileDir()) {
          for (Map.Entry<String, byte[]> entry : grapheme.getFreq().entrySet()) {
            String filePath = getFilePath(language, entry.getKey());
            FilesUtils.writeFile(filePath, entry.getValue());
          }
        } else {
          throw new ApplicationException("Failed to create directory: " + FILE_DIR);
        }
      }
    }

    private static boolean createFileDir() {
      File dir = new File(FILE_DIR);
      if (!dir.exists()) {
        return dir.mkdirs();
      }
      return true;
    }

    @Override
    protected Task<Void> createTask() {
      return new Task<>() {
        @Override
        protected Void call() {
          downloadAsync(gardenClient, language);
          return null;
        }
      };
    }

    public void downloadProcess() {
      //setup progress indicator container
      VBox progressBox = new VBox();
      progressBox.setAlignment(Pos.CENTER);
      ProgressIndicator progressIndicator = new ProgressIndicator();
      Label label = new Label("Downloading from garden server...");
      progressBox.getChildren().addAll(progressIndicator, label);
      container.getChildren().add(progressBox);
      childContainer.setDisable(true);

      //bind properties
      progressIndicator.progressProperty().bind(progressProperty());
      progressIndicator.visibleProperty().bind(runningProperty());

      //start grapheme task to download resources
      if (!isRunning()) {
        restart();
      }

      //add listeners for each event on this service
      setOnSucceeded(event -> {
        container.getChildren().remove(progressBox);
        childContainer.setDisable(false);
      });
      setOnFailed(event -> {
        container.getChildren().remove(progressBox);
        childContainer.setDisable(false);
        ExceptionDialog ed = new ExceptionDialog(event.getSource().getException());
        ed.showAndWait();
      });
      setOnCancelled(event -> {
        container.getChildren().remove(progressBox);
        childContainer.setDisable(false);
      });
    }
  }


}
