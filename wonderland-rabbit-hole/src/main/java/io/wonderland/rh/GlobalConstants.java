package io.wonderland.rh;

import io.wonderland.base.ApplicationException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateFactory;
import javafx.geometry.Insets;
import javafx.stage.Screen;

public class GlobalConstants {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  public static final String SECRET_KEY = "Secret key";
  public static final String PRIVATE_KEY = "Private key";
  public static final String PUBLIC_KEY = "Public key";
  public static final Insets DEFAULT_INSETS = new Insets(10, 10, 10, 10);
  public static final Insets BORDER_PANE_INSETS = new Insets(5, 5, 5, 5);
  public static final double SCENE_WIDTH = getReportedScreenWidth() * 0.7;
  public static final double SCENE_HEIGHT = getReportedScreenHeight() * 0.9;
  public static final int SPACING = 10;
  public static final int DIALOG_SPACING = 5;
  public static final int[] KEY_EXPORT_DIALOG_SIZE = new int[]{350, 250};
  public static final int[] DIALOG_SIZE = new int[]{400, 400};
  public static final int[] NEW_CONN_WINDOW_SIZE = new int[]{400, 300};
  public static final int[] MANAGE_CONN_WINDOW_SIZE = new int[]{650, 500};


  public static CertificateFactory CERTIFICATE_FACTORY;

  static {
    try {
      CERTIFICATE_FACTORY = CertificateFactory.getInstance("X.509", "BC");
    } catch (Exception e) {
      throw new ApplicationException(e);
    }
  }


  private GlobalConstants() {
  }


  public static double getReportedScreenHeight() {
    return Screen.getPrimary().getBounds().getHeight();
  }

  public static double getReportedScreenWidth() {
    return Screen.getPrimary().getBounds().getWidth();
  }

}
