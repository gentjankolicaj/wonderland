package io.wonderland.rh.analysis.ciphertext;

import io.wonderland.rq.cryptanalysis.IndexCoincidence;
import java.awt.Color;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.fx.ChartCanvas;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

@Slf4j
public class IndexCoincidenceStage extends Stage {

  private final String title;
  private final double width;
  private final double height;

  private final BorderPane rootPane = new BorderPane();
  private final ScrollPane scrollPane = new ScrollPane();
  private final BorderPane chartPane = new BorderPane();
  private Scene scene;
  private String language;
  private String ciphertextEncoding;
  private String ciphertext;


  public IndexCoincidenceStage(String title, double width, double height) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.scene = initScene(title, width, height);
  }

  protected Scene initScene(String title, double width, double height) {
    //init scene & pane
    scene = new Scene(rootPane, width, height);

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


  public void createChart(String language, String ciphertextEncoding, String ciphertext) {
    this.language = language;
    this.ciphertextEncoding = ciphertextEncoding;
    this.ciphertext = ciphertext;
    int minKeyLength = 1;
    int maxKeyLength = 20;
    String keyLength = "key length";
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    Map<Integer, Double> ics = IndexCoincidence.calcKeyPeriodIC(ciphertext.getBytes(), 1, minKeyLength, maxKeyLength);
    if (MapUtils.isNotEmpty(ics)) {
      ics.entrySet().forEach(e -> dataset.addValue(e.getValue(), keyLength, e.getKey()));
    }

    //add first dataset and render as bar values
    BarRenderer renderer = new BarRenderer();
    renderer.setSeriesPaint(0, Color.blue);

    CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis(keyLength), new NumberAxis("Index of Coincidence"),
        renderer);

    JFreeChart chart = new JFreeChart(plot);
    chart.addSubtitle(
        new TextTitle("Ciphertext IC calculated for key length from " + minKeyLength + " to " + maxKeyLength));
    chart.setBackgroundPaint(Color.white);

    // Create chart viewer
    ChartCanvas chartCanvas = new ChartCanvas(chart);
    chartCanvas.widthProperty().bind(this.chartPane.widthProperty());
    chartCanvas.heightProperty().bind(this.chartPane.heightProperty());
    this.chartPane.setCenter(chartCanvas);
  }
}
