package io.wonderland.rh.base.barcode;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.rh.base.codec.Base10Blank;
import org.junit.jupiter.api.Test;

class QRCodeTest {


  @Test
  void getName() {
    Barcode barcode = new QRCode(new Base10Blank());
    assertThat(barcode.getName()).isEqualTo("QRCode");
  }

}