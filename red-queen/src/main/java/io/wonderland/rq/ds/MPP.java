package io.wonderland.rq.ds;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Data structure class used on MPP (Most probable pair ) algorithm
 *
 * @param <K>
 * @param <V>
 */
@AllArgsConstructor
public class MPP<K, V> implements Map.Entry<K, V> {

  private K key;
  private V value;

  public static <K, V> MPP<K, V> of(K key, V value) {
    return new MPP<>(key, value);
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public V setValue(V v) {
    return this.value = v;
  }

  @RequiredArgsConstructor
  @Getter
  @Builder
  @EqualsAndHashCode
  public static class MPPValue<T> implements Comparable<T> {

    private final T key;
    @EqualsAndHashCode.Exclude
    private final Double freq;
    @EqualsAndHashCode.Exclude
    private final Double freqDiff;

    public static <K> MPPValue of(K key, Double freq, Double freqDiff) {
      return builder().key(key).freq(freq).freqDiff(freqDiff).build();
    }

    @Override
    public int compareTo(T t) {
      return key.toString().compareTo(t.toString());
    }
  }

}
