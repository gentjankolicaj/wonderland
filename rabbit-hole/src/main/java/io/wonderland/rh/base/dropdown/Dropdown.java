package io.wonderland.rh.base.dropdown;

import io.wonderland.rh.base.TypeObserver;
import io.wonderland.rh.utils.GuiUtils;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

@SuppressWarnings("unchecked")
public class Dropdown<K,T,R> extends HBox {
  private final SearchableComboBox<DropdownElement<K,T,R>> comboBox=new SearchableComboBox<>();
  private final DropdownEventHandler eventHandler=new DropdownEventHandler() ;
  private final String title;
  private final R outputRegion;
  private final TypeObserver<T> typeObserver;
  private final DropdownElement<K,T,R>[] elements;
  @SafeVarargs
  public Dropdown(String title,K initialSelect,R outputRegion, TypeObserver<T> typeObserver,DropdownElement<K,T,R>...elements){
    Objects.requireNonNull(elements);
    Objects.requireNonNull(outputRegion);
    Objects.requireNonNull(typeObserver);
    this.outputRegion=outputRegion;
    this.typeObserver=typeObserver;
    this.elements=elements;
    this.title=title;
    this.build(initialSelect);
  }

  public Dropdown(String title,K initialSelect,R outputRegion, TypeObserver<T> typeObserver, List<DropdownElement<K,T,R>> elements){
    Objects.requireNonNull(elements);
    Objects.requireNonNull(outputRegion);
    Objects.requireNonNull(typeObserver);
    this.outputRegion=outputRegion;
    this.typeObserver=typeObserver;
    this.elements= elements.toArray(DropdownElement[]::new);
    this.title=title;
    this.build(initialSelect);
  }


  private void build(K initialSelect){
    Label label= GuiUtils.getTitle(title);
    this.comboBox.setConverter(new DropdownElementConverter());
    this.comboBox.setItems(FXCollections.observableArrayList(elements));
    this.comboBox.setEditable(false);
    this.comboBox.setOnAction(eventHandler);
    if(initialSelect !=null ){
       this.comboBox.getSelectionModel().select(getElementIndex(initialSelect));
    }else {
      this.comboBox.getSelectionModel().selectFirst();
    }
    this.getChildren().addAll(label,comboBox);
    this.setSpacing(5);
  }

  private int getElementIndex(K key){
    for(int i=0;i<elements.length;i++){
      DropdownElement<K,T,R> element=elements[i];
      if(key.equals(element.getKey())){
        return i;
      }
    }
    return 0;
  }


  private class DropdownElementConverter extends StringConverter<DropdownElement<K,T,R>>{
    @Override
    public String toString(DropdownElement<K, T,R> element) {
      return element==null? null : element.getKey().toString();
    }

    @Override
    public DropdownElement<K, T,R> fromString(String s) {
      for(DropdownElement<K,T,R> element:elements){
        if(element!=null && element.getKey().equals(s)){
          return element;
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
