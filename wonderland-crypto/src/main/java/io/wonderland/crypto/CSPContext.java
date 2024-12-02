package io.wonderland.crypto;

import java.util.Map;
import java.util.Objects;


public interface CSPContext extends Context {

  String getProvider();

  class CSPContextImpl implements CSPContext {

    private final String provider;
    private final Map<Class<?>, Object> components;

    public CSPContextImpl(String provider, Map<Class<?>, Object> components) {
      Objects.requireNonNull(provider);
      Objects.requireNonNull(components);
      this.provider = provider;
      this.components = components;
    }

    public CSPContextImpl(String provider) {
      Objects.requireNonNull(provider);
      this.provider = provider;
      this.components = Map.of();
    }

    @Override
    public String getProvider() {
      return provider;
    }

    @Override
    public Map<Class<?>, Object> components() {
      return components;
    }
  }


}
