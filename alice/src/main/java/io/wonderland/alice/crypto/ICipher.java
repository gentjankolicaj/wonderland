package io.wonderland.alice.crypto;


/**
 * Parent interface of all cipher implementations.
 */
public interface ICipher {

  String INVALID_KEY_TYPE_PARAMETER = "Invalid key type parameter to '%s' init.Key type parameter must be of '%s'";
  String INVALID_PARAMETER = "Invalid parameter to '%s' init.";
  String DISREGARDED_IV = "Disregarded 'IV' because '%s' doesn't need it.";


  /**
   * Return the name of the algorithm the cipher implements.
   *
   * @return the name of the algorithm the cipher implements.
   */
  String getAlgorithmName();

  /**
   * Returns names of key types cipher allows a parameters
   *
   * @return key type names
   */
  String[] getKeyTypeNames();


  default String invalidParamMessage() {
    return String.format(INVALID_PARAMETER, getAlgorithmName());
  }

  default String invalidKeyTypeParamMessage() {
    String keyTypeNames = String.join("", getKeyTypeNames());
    return String.format(INVALID_KEY_TYPE_PARAMETER, getAlgorithmName(), keyTypeNames);
  }

}
