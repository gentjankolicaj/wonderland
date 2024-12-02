package io.wonderland.rh.base.fx;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class NodeItem<T> {

  private final String key;
  private final String text;
  private final List<NodeItem<T>> children = new ArrayList<>();
  private T node;

  public NodeItem(String key, String text) {
    this(key, text, null);
  }

  public NodeItem(String key, T node) {
    this(key, key, node);
  }

  public NodeItem(String key, String text, T node) {
    this.key = key;
    this.node = node;
    this.text = text;
  }

  public NodeItem(String key, NodeItem<T>... children) {
    this.key = key;
    this.text = key;
    this.children.addAll(List.of(children));
  }

  public void addChild(NodeItem<T> child) {
    this.children.add(child);
  }

  public void addChildren(NodeItem<T>... children) {
    this.children.addAll(List.of(children));
  }

}
