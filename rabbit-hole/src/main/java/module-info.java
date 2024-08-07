//opened all module because I needed reflection access to build jmx beans
open module rabbit.hole {
  requires javafx.controls;
  requires javafx.swing;
  requires org.controlsfx.controls;

  requires org.jfree.chart.fx;
  requires org.jfree.chart3d.fx;
  requires org.jfree.jfreechart;
  requires com.google.zxing.javase;
  requires com.google.zxing;

  //my modules
  requires alice;
  requires wonderland.base;
  requires wonderland.struct;
  requires wonderland.grpc;
  requires wonderland.redqueen;
  requires wonderland.garden;

  //csp
  requires org.bouncycastle.provider;
  requires org.bouncycastle.pkix;
  requires org.bouncycastle.tls;
  requires org.conscrypt;
  requires com.amazon.corretto.crypto.provider;

  //grpc
  requires io.grpc;
  requires jsr305;
  requires com.google.common;
  requires io.netty.common;
  requires java.management;

  exports io.wonderland.rh;
  exports io.wonderland.rh.misc;
  exports io.wonderland.rh.exception;
  exports io.wonderland.rh.analysis;
  exports io.wonderland.rh.analysis.ciphertext;

}