package io.wonderland.alice.exception;

public final class ExceptionMessages {


  public static final String ARRAY_OFFSET_NOT_VALID = "Array offset can't be less than 0.";
  public static final String PLAINTEXT_NOT_VALID = "Plaintext can't be empty.";
  public static final String CIPHERTEXT_NOT_VALID = "Ciphertext can't be empty.";
  public static final String KEY_NOT_VALID = "Key can't be null or empty.";
  public static final String KEY_ARGS_NOT_VALID = "Key arguments can't be null or empty.";
  public static final String KEY_MODULUS_NOT_VALID = "Key modulus less | equal to zero.Modulus must be bigger than 0.";

  private ExceptionMessages() {
  }

}
