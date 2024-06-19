package io.wonderland.rh.menu;

import static io.wonderland.rh.GlobalConstants.MANAGE_CONN_WINDOW_SIZE;

import io.wonderland.rh.GlobalConstants;
import io.wonderland.rh.base.fx.BaseMenu;
import io.wonderland.rh.base.fx.BaseMenuItem;
import io.wonderland.rh.base.fx.BaseStage;
import io.wonderland.rh.utils.GuiUtils;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

public class AboutMenu extends BaseMenu {

  public AboutMenu() {
    super("About", new AuthorItem(), new LicenseItem());
  }


  static class AuthorItem extends BaseMenuItem {

    public AuthorItem() {
      super("Author", getActionConsumer());
    }

    static Consumer<ActionEvent> getActionConsumer() {
      return actionEvent -> new AuthorStage().show();
    }
  }

  static class LicenseItem extends BaseMenuItem {

    public LicenseItem() {
      super("Licenses", getActionConsumer());
    }

    static Consumer<ActionEvent> getActionConsumer() {
      return actionEvent -> new LicensesStage().show();
    }
  }

  @Slf4j
  static class AuthorStage extends BaseStage {

    public static final String TITLE = "About author";

    AuthorStage() {
      super(TITLE, false);
      this.setScene(new Scene(new AuthorPane(), MANAGE_CONN_WINDOW_SIZE[0],
          MANAGE_CONN_WINDOW_SIZE[1] * 0.3));
    }

    static class AuthorPane extends BorderPane {

      private final Label authorLbl = GuiUtils.getTitle("Author:");
      private final Label author = GuiUtils.getTitle("Gentjan Koliçaj");
      private final Label linkedinLbl = GuiUtils.getTitle("*Linkedin:");
      private final Label githubLbl = GuiUtils.getTitle("*Github:");

      AuthorPane() {
        this.build();
        BorderPane.setMargin(this, GlobalConstants.DEFAULT_INSETS);
      }

      private void build() {
        this.buildCenter();
      }

      private void buildCenter() {
        VBox vBox = new VBox();
        vBox.setSpacing(GlobalConstants.SPACING);
        HBox.setHgrow(author, Priority.ALWAYS);
        vBox.getChildren().add(new HBox(GlobalConstants.SPACING, authorLbl, author));
        vBox.getChildren().add(new HBox(GlobalConstants.SPACING, linkedinLbl,
            new Label("https://al.linkedin.com/in/gentjan-koli%C3%A7aj-559301160")));
        vBox.getChildren()
            .add(new HBox(GlobalConstants.SPACING, githubLbl,
                new Label("https://github.com/gentjankolicaj")));
        this.setCenter(vBox);
      }
    }

  }

  @Slf4j
  static class LicensesStage extends BaseStage {

    public static final String TITLE = "About licenses";

    LicensesStage() {
      super(TITLE, false);
      this.setScene(new Scene(new LicensesPane(), MANAGE_CONN_WINDOW_SIZE[0],
          MANAGE_CONN_WINDOW_SIZE[1] * 0.3));
    }

    static class LicensesPane extends BorderPane {

      private final Label noteLbl = GuiUtils.getTitle(
          "Note: there are 2 licenses for this project and can be found accordingly");
      private final Label l1Lbl = GuiUtils.getTitle("*License-1 (Apache 2.0):");
      private final Label l2Lbl = GuiUtils.getTitle("*License-2 (Custom):");

      LicensesPane() {
        this.build();
        BorderPane.setMargin(this, GlobalConstants.DEFAULT_INSETS);
      }

      private void build() {
        this.buildCenter();
      }

      private void buildCenter() {
        VBox vBox = new VBox();
        vBox.setSpacing(GlobalConstants.SPACING);
        vBox.getChildren().add(new HBox(GlobalConstants.SPACING, noteLbl));
        vBox.getChildren().add(new HBox(GlobalConstants.SPACING, l1Lbl,
            new Label("https://github.com/gentjankolicaj/wonderland/blob/master/LICENSE")));
        vBox.getChildren()
            .add(new HBox(GlobalConstants.SPACING, l2Lbl,
                new Label("https://github.com/gentjankolicaj/wonderland/blob/master/LICENSE_2")));
        this.setCenter(vBox);
      }
    }
  }

}
