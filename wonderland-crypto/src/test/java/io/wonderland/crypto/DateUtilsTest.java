package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

  @Test
  void calcDate() {
    Date tenHours = DateUtils.calcDate(10);
    assertThat(tenHours).isAfter(new Date());
  }
}