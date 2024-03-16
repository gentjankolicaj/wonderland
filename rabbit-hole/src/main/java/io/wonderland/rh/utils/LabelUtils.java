package io.wonderland.rh.utils;


import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public final class LabelUtils {

  private LabelUtils(){}

  public static Label getTitle(String text){
    Label label = new javafx.scene.control.Label(text);
    label.setFont(Font.font("ARIAL", FontWeight.BOLD, 13));
    return label;
  }

  public static Label getTitle(String text,Font font){
    Label label = new javafx.scene.control.Label(text);
    label.setFont(font);
    return label;
  }


}
