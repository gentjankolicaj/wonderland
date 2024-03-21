module rabbit.hole {
  requires java.se;
  requires javafx.controls;
  requires javafx.swing;
  requires org.controlsfx.controls;
  requires lombok;
  requires org.apache.commons.lang3;
  requires org.apache.commons.collections4;
  requires org.apache.commons.io;
  requires org.apache.commons.codec;
  requires org.slf4j;
  requires red.queen;
  requires commons;
  requires alice;
  requires org.jfree.chart.fx;
  requires org.jfree.chart3d.fx;
  requires org.jfree.jfreechart;
  requires com.google.zxing.javase;
  requires com.google.zxing;
  requires org.reflections;

  //csp
  requires org.bouncycastle.provider;
  requires org.bouncycastle.pkix;
  requires org.conscrypt;

  exports io.wonderland.rh;

}