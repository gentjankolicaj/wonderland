package io.wonderland.rh.base.common;

import io.wonderland.rh.base.Observer;
import io.wonderland.rh.cipher.CipherConstants;
import io.wonderland.rh.cipher.key.AbstractKeyPane;
import io.wonderland.rh.cipher.key.DefaultKeyPane;
import io.wonderland.rh.exception.ServiceException;
import io.wonderland.rh.keygen.KeygenPane;
import io.wonderland.rh.utils.GuiUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.crypto.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.SearchableComboBox;

@Slf4j
public class KeyPane extends TitledPane {

  private final Stage stage;
  private final String cipherName;
  private final BorderPane containerPane = new BorderPane();
  private final HToggleBox<RadioButton> keyOriginPane = new HToggleBox<>("Key source : ", 10, RadioButton::new,
      Map.of("new", this::setNewKeyPane, "old", this:: setOldKeyPane));
  private final Observer<Integer,Object> keyObserver;

  public KeyPane(Stage stage, String title, String cipherName,Observer<Integer,Object> keyObserver) {
    this.stage = stage;
    this.cipherName = cipherName;
    this.keyObserver=keyObserver;
    this.setText(title);
    this.build();
  }


  private void build() {
    this.containerPane.setTop(keyOriginPane);
    this.setContent(containerPane);
  }


  private void setNewKeyPane() {
    this.containerPane.setCenter(new GenerateKeyPane());
  }

  private void setOldKeyPane() {
    updateInputKeyPane(cipherName);
  }

  class GenerateKeyPane extends BorderPane  {

    private SearchableComboBox<String> keyComboBox = new SearchableComboBox<>();
    private final HBox hBox = new HBox();

    GenerateKeyPane() {
      this.build();
    }

    private void build() {
      this.keyComboBox = new SearchableComboBox<>(FXCollections.observableArrayList(getKeyTypesString()));
      this.keyComboBox.setOnAction(new KeygenComboBoxEventHandler());
      this.hBox.getChildren().addAll(GuiUtils.getTitle("Key generator : "), keyComboBox);
      this.setTop(hBox);
    }

    private List<String> getKeyTypesString() {
      List<String> allKeyTypes = new ArrayList<>();
      Arrays.stream(Security.getProviders()).forEach(p -> allKeyTypes.addAll(p.getServices().stream()
          .filter(e -> isValidServiceName(e.getAlgorithm(), e.getType()))
          .sorted(Comparator.comparing(e -> e.getAlgorithm().charAt(0)))
          .map(e -> {
            String sb = e.getAlgorithm() + " ("
                + e.getProvider().getName()
                + '-'
                + e.getProvider().getVersionStr()
                + ')';
            return sb;
          }).collect(Collectors.toList())));
      return allKeyTypes;
    }

    protected boolean isValidServiceName(String name, String type) {
      if (StringUtils.isEmpty(name)) {
        return false;
      } else {
        return (!(name.contains(".") || name.contains("OID"))) &&
            (type.equals("KeyGenerator") || type.equals("KeyPairGenerator"));
      }
    }

    class KeygenComboBoxEventHandler implements EventHandler<ActionEvent> {

      @Override
      public void handle(ActionEvent actionEvent) {
        updateKeyPane(keyComboBox.getValue());
      }

      private void updateKeyPane(String keygenName) {
        try {
          Optional<Object> optionalKeyGenerator = getKeyGeneratorInstance(keygenName);
          setCenter(new KeygenPane(stage, optionalKeyGenerator,keyObserver));
        } catch (Exception e) {
          log.error(e.getMessage());
          setCenter(new Label(e.getMessage()));
        }
      }
    }

    Optional<Object> getKeyGeneratorInstance(String keyGeneratorInfo) {
      if(StringUtils.isNotEmpty(keyGeneratorInfo)) {
        String algorithm = keyGeneratorInfo.substring(0, keyGeneratorInfo.indexOf(" ("));
        try {
          return Optional.of(KeyGenerator.getInstance(algorithm));
        } catch (Exception e) {
          log.error(e.getMessage());
        }
        try {
          return Optional.ofNullable(KeyPairGenerator.getInstance(algorithm));
        } catch (Exception e) {
          log.error(e.getMessage());
        }
      }
      return Optional.empty();
    }

  }

  private void updateInputKeyPane(String cipher) {
    Class<?> keyPaneClass = CipherConstants.getKeyPaneMappings().computeIfAbsent(cipher, k -> DefaultKeyPane.class);
    try {
      Consumer<?> consumer = s -> {};
      Constructor<?> constructor = keyPaneClass.getDeclaredConstructor(Consumer.class);
      //todo: to add keygen observer
      AbstractKeyPane<?> newKeyPane = (AbstractKeyPane<?>) constructor.newInstance(consumer);
      this.containerPane.setCenter(newKeyPane);
    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new ServiceException("Failed to changed KeyGeneratorPane", e);
    }
  }

}





