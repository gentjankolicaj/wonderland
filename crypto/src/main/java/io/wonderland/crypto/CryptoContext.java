package io.wonderland.crypto;

public interface CryptoContext {


  String getProvider();


  class StaticContext implements CryptoContext {

    @Override
    public String getProvider() {
      return "";
    }
  }

}
