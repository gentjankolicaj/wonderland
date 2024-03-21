package io.wonderland.rh.base.common;

import io.wonderland.rh.base.TypeObserver;
import io.wonderland.rh.utils.LabelUtils;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

public class Dropdown<K,T,R> extends HBox {
  private final SearchableComboBox<DropdownElement<K,T,R>> comboBox=new SearchableComboBox<>();
  private final DropdownEventHandler eventHandler=new DropdownEventHandler() ;
  private final String title;
  private final R outputRegion;
  private final TypeObserver<T> typeObserver;
  private final DropdownElement<K,T,R>[] elements;

  public Dropdown(String title,R outputRegion, TypeObserver<T> typeObserver,DropdownElement<K,T,R>...elements){
    Objects.requireNonNull(elements);
    Objects.requireNonNull(outputRegion);
    Objects.requireNonNull(typeObserver);
    this.outputRegion=outputRegion;
    this.typeObserver=typeObserver;
    this.elements=elements;
    this.title=title;
    this.build();
  }


  private void build(){
    Label label= LabelUtils.getTitle(title);
    this.comboBox.setConverter(new DropdownElementConverter());
    this.comboBox.setItems(FXCollections.observableArrayList(elements));
    this.comboBox.setEditable(false);
    this.comboBox.setOnAction(eventHandler);
    this.comboBox.getSelectionModel().selectFirst();
    this.getChildren().addAll(label,comboBox);
    this.setSpacing(10);
  }


  private class DropdownElementConverter extends StringConverter<DropdownElement<K,T,R>>{

    @Override
    public String toString(DropdownElement<K, T,R> element) {
      return element==null? null : element.getKey().toString();
    }

    @Override
    public DropdownElement<K, T,R> fromString(String s) {
      for(DropdownElement<K,T,R> var:elements){
        if(var!=null && var.getKey().equals(s)){
          return var;
        }
      }
      return null;
    }
  }

  private class DropdownEventHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
      DropdownElement<K,T,R> element=comboBox.getValue();
      if(Objects.nonNull(element) && Objects.nonNull(element.getBiConsumer())){
        element.getBiConsumer().accept(typeObserver,outputRegion);
      }
    }

  }

  public DropdownElement<K,T,R> getSelectedDropdownElement(){
    return this.comboBox.getSelectionModel().getSelectedItem();
  }

}
