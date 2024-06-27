package io.wonderland.alice.crypto.symmetric;

import io.wonderland.alice.crypto.TabulaRecta;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TabulaRectaTest {

  @Test
  void encryptDecrypt() {
    TabulaRecta.printTable(3, 31, 127);
  }

}
