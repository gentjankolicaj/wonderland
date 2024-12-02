package io.wonderland.base;

import java.util.List;
import java.util.Optional;

/**
 * @param <T> model type
 * @param <I> model id type
 */
public interface Dao<T, I> {

  Optional<T> get(I id);

  List<T> getAll();

  void save(T t);

  void saveAll(T... args);

  void update(T t);

  void delete(T t);
}
