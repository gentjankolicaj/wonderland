package io.wonderland.rh.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;

public class ZxingUtils {
  private static final QRCodeWriter QR_CODE_WRITER=new QRCodeWriter();

  public static BufferedImage generateQRCodeImage(String barcode,int width,int height) throws Exception {
    BitMatrix bitMatrix = QR_CODE_WRITER.encode(barcode, BarcodeFormat.QR_CODE, width, height);
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }

}
