package io.wonderland.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.crypto.SealedObject;

public final class SealedObjectUtils {

  private SealedObjectUtils() {
  }


  /**
   * Create a byte array from SealedObject.
   *
   * @param sealedObject object composed of serializable instance & cipher
   * @return serialization encrypted
   * @throws IOException
   */
  public static byte[] serialize(SealedObject sealedObject) throws IOException {
    try (
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(sealedObject);
      return baos.toByteArray();
    }
  }

  /**
   * Create an instance of SealedObject form an encrypted serialization.
   *
   * @param buffer encrypted serialization buffer
   * @return Sealed object from encrypted serialization
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static SealedObject deserialize(byte[] buffer) throws IOException, ClassNotFoundException {
    try (
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        ObjectInputStream ois = new ObjectInputStream(bais)) {
      return (SealedObject) ois.readObject();
    }
  }

}
