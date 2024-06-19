package io.wonderland.rh.base.barcode;

import io.wonderland.rh.base.codec.Base10Blank;
import io.wonderland.rh.base.codec.CodecAlg;
import io.wonderland.rh.base.codec.StringASCII;
import io.wonderland.rh.base.codec.StringBig5;
import io.wonderland.rh.base.codec.StringUTF16;
import io.wonderland.rh.base.codec.StringUTF16LE;
import io.wonderland.rh.base.codec.StringUTF8;
import java.util.List;

@SuppressWarnings("all")
public final class Barcodes {

  private static final List<CodecAlg> IMPL_BARCODE_ALGS = createBarcodes();

  private Barcodes() {
  }

  private static List<CodecAlg> createBarcodes() {
    return List.of(new QRCode(new Base10Blank()), new QRCode(new StringASCII()),
        new QRCode(new StringUTF8()),
        new QRCode(new StringUTF16LE()), new QRCode(new StringUTF16()),
        new QRCode(new StringBig5()),
        new AztecCode(new Base10Blank()), new AztecCode(new StringASCII()),
        new AztecCode(new StringUTF8()),
        new AztecCode(new StringUTF16LE()), new AztecCode(new StringUTF16()),
        new AztecCode(new StringBig5()),
        new DataMatrixCode(new Base10Blank()), new DataMatrixCode(new StringASCII()),
        new DataMatrixCode(new StringUTF8()), new DataMatrixCode(new StringUTF16LE()),
        new DataMatrixCode(new StringUTF16()), new DataMatrixCode(new StringBig5()));
  }

  public static List<CodecAlg> getImplementedBarcodeAlgs() {
    return IMPL_BARCODE_ALGS;
  }

}
