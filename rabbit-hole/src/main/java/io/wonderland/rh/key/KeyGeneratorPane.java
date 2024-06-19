package io.wonderland.rh.key;

import io.atlassian.fugue.Either;
import io.wonderland.rh.base.fx.ExceptionDialog;
import io.wonderland.rh.base.observer.EitherKeyObserver;
import io.wonderland.rh.keygen.KeygenPane;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javax.crypto.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.SearchableComboBox;

@Slf4j
public class KeyGeneratorPane extends BorderPane {

  private final SearchableComboBox<String> keyGeneratorComboBox = new SearchableComboBox<>();
  private final EitherKeyObserver eitherKeyObserver;

  public KeyGeneratorPane(EitherKeyObserver eitherKeyObserver) {
    this.eitherKeyObserver = eitherKeyObserver;
    this.build();
  }

  private void build() {
    this.keyGeneratorComboBox.setItems(
        FXCollections.observableArrayList(getKeyGeneratorAlgorithms()));
    this.keyGeneratorComboBox.setOnAction(new KeygenComboBoxEventHandler());
    this.keyGeneratorComboBox.setPromptText("Choose key generator :");
    this.setTop(new HBox(keyGeneratorComboBox));
  }

  private List<String> getKeyGeneratorAlgorithms() {
    List<String> allKeyTypes = new ArrayList<>();
    Arrays.stream(Security.getProviders()).forEach(p -> allKeyTypes.addAll(p.getServices().stream()
        .filter(e -> isValidKeyGenerator(e.getAlgorithm(), e.getType()))
        .sorted(Comparator.comparing(e -> e.getAlgorithm().charAt(0)))
        .map(e -> e.getAlgorithm() + " (" + e.getProvider().getName() + '-' + e.getProvider()
            .getVersionStr() + ')')
        .collect(Collectors.toList())));
    return allKeyTypes;
  }

  protected boolean isValidKeyGenerator(String name, String type) {
    if (StringUtils.isEmpty(name)) {
      return false;
    } else {
      return (!(name.contains(".") || name.contains("OID"))) &&
          (type.equals("KeyGenerator") || type.equals("KeyPairGenerator"));
    }
  }


  private class KeygenComboBoxEventHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
      updateKeyPane(keyGeneratorComboBox.getValue());
    }

    private void updateKeyPane(String keygenName) {
      try {
        if (StringUtils.isNotEmpty(keygenName)) {
          setCenter(new KeygenPane(createKeygen(keygenName), eitherKeyObserver));
        }
      } catch (Exception e) {
        ExceptionDialog ed = new ExceptionDialog(e);
        ed.showAndWait();
      }
    }

    private Either<KeyGenerator, KeyPairGenerator> createKeygen(String keyGeneratorInfo) {
      String algorithm = keyGeneratorInfo.substring(0, keyGeneratorInfo.indexOf(" ("));
      List<Exception> exceptions = new ArrayList<>();
      try {
        return Either.left(KeyGenerator.getInstance(algorithm));
      } catch (Exception e) {
        exceptions.add(e);
      }
      try {
        return Either.right(KeyPairGenerator.getInstance(algorithm));
      } catch (Exception e) {
        exceptions.add(e);
      }
      log.error("KeyGenerator ex : ", exceptions.get(0));
      log.error("KeyPairGenerator ex : ", exceptions.get(1));
      throw new IllegalStateException("Failed to instantiate KeyGenerator/KeyPairGenerator");
    }
  }

}
