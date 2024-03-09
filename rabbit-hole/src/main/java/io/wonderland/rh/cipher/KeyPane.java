package io.wonderland.rh.cipher;

import io.wonderland.rh.cipher.key.DefaultKeyPane;
import io.wonderland.rh.cipher.key.AbstractKeyPane;
import io.wonderland.rh.common.HTogglePane;
import io.wonderland.rh.exception.ServiceException;
import io.wonderland.rh.keygen.KeygenPane;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.Key;
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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.SearchableComboBox;

@Slf4j
public class KeyPane extends TitledPane {
   private Stage stage;
   private boolean keyStaleState =true;
   private String cipherName;
   private BorderPane containerPane =new BorderPane();

   private HTogglePane<RadioButton> keyOriginPane = new HTogglePane<>("Key : ", 10,s -> new RadioButton(s),
      Map.of("new", () -> setGenerateKeyPane(), "existing", () -> setInsertKeyPane()));

  public KeyPane(Stage stage,String title,String cipherName){
    this.stage=stage;
    this.cipherName=cipherName;
    this.setText(title);
    this.build();
  }


  private void build(){
    this.containerPane.setTop(keyOriginPane);
    this.setContent(containerPane);
  }


  private void setGenerateKeyPane(){
    this.containerPane.setCenter(new GenerateKeyPane());

  }

  private void setInsertKeyPane(){
   updateInputKeyPane(cipherName);
  }

  class GenerateKeyPane extends BorderPane implements KeySource {
    private SearchableComboBox<String> keyComboBox=new SearchableComboBox<>();
    private HBox hBox=new HBox();
    private Label chooseLbl;

    GenerateKeyPane(){
     this.build();
    }

    private void build(){
      this.keyComboBox=new SearchableComboBox<>(FXCollections.observableArrayList(getKeyTypesString()));
      this.keyComboBox.setOnAction(new KeygenComboBoxEventHandler());
      this.chooseLbl=new Label("["+keyComboBox.getItems().size()+"] , generator :");
      this.hBox.getChildren().addAll(chooseLbl,keyComboBox);
      this.setTop(hBox);
    }



    @Override
    public Key getKey() {
      return null;
    }

    @Override
    public boolean removeKey() {
      return false;
    }


    List<String> getKeyTypesString(){
      List<String> allKeyTypes=new ArrayList<>();
      Arrays.stream(Security.getProviders()).forEach(p->allKeyTypes.addAll(p.getServices().stream()
          .filter(e->isValidServiceName(e.getAlgorithm(),e.getType())).sorted(Comparator.comparing(e -> e.getAlgorithm().charAt(0))).map(e-> e.getAlgorithm()).collect(Collectors.toList())));
      return allKeyTypes;
    }

    protected boolean isValidServiceName(String name,String type) {
      if (StringUtils.isEmpty(name)) {
        return false;
      } else {
        return (!(name.contains(".") || name.contains("OID")))&&(type.equals("KeyGenerator")||type.equals("KeyPairGenerator"));
      }
    }

    class KeygenComboBoxEventHandler implements EventHandler<ActionEvent>{
      @Override
      public void handle(ActionEvent actionEvent) {
        updateKeyPane(keyComboBox.getValue());
      }
    }

    private void updateKeyPane(String keygenName){
      try {
        Optional<Object> optionalKeygen = getKeyGeneratorInstance(keygenName);
        this.setCenter(new KeygenPane(stage,optionalKeygen));
      } catch (Exception e) {
        log.error(e.getMessage());
        this.setCenter(new Label(e.getMessage()));
      }
    }

    Optional<Object> getKeyGeneratorInstance(String keygenName){
      try{
        return Optional.ofNullable(KeyGenerator.getInstance(keygenName));
      }catch (Exception e){
        log.error(e.getMessage());
      }
      try{
        return Optional.ofNullable(KeyPairGenerator.getInstance(keygenName));
      }catch (Exception e){
        log.error(e.getMessage());
      }
      return Optional.empty();
    }

  }


  private void updateInputKeyPane(String cipher) {
    Class<?> keyPaneClass = CipherConstants.getKeyPaneMappings().computeIfAbsent(cipher, k -> DefaultKeyPane.class);
    try {
      Consumer<?> consumer = s -> setKeyStaleState(true);
      Constructor<?> constructor = keyPaneClass.getDeclaredConstructor(Consumer.class);
      AbstractKeyPane<?> newKeyPane = (AbstractKeyPane<?>) constructor.newInstance(consumer);

      this.containerPane.setCenter(newKeyPane);
    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new ServiceException("Failed to changed KeyGeneratorPane", e);
    }
  }

   void setKeyStaleState(boolean state) {
    this.keyStaleState =state;
  }

  boolean getKeyStaleState(){
    return keyStaleState;
  }

  Key getKey(){
    return ((KeySource) containerPane.getCenter()).getKey();
  }

  boolean clearKey(){
    return true;
  }

}





