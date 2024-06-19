package io.wonderland.base;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AbstractServiceTest {


  @Test
  void childClassTest() {

    AbstractService<Long, Long> service = new SimpleService(new Dao<>() {
      @Override
      public Optional<Long> get(Long id) {
        return Optional.empty();
      }

      @Override
      public List<Long> getAll() {
        return List.of();
      }

      @Override
      public void save(Long aLong) {

      }

      @Override
      public void saveAll(Long... args) {

      }

      @Override
      public void update(Long aLong) {

      }

      @Override
      public void delete(Long aLong) {

      }
    });
    assertThat(service).isNotNull();

  }


  private static class SimpleService extends AbstractService<Long, Long> {

    public SimpleService(Dao<Long, Long> dao) {
      super(dao);
    }
  }

}