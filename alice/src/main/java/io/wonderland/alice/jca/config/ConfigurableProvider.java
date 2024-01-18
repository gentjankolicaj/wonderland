package io.wonderland.alice.jca.config;


import java.util.Map;

/**
 * Implemented by the BC provider. This allows setting of hidden parameters, such as the ImplicitCA parameters from
 * X.962, if used.
 */
public interface ConfigurableProvider {

  void setParameter(String parameterName, Object parameter);

  void addAlgorithm(String key, String value);


  boolean hasAlgorithm(String type, String name);


  void addAttributes(String key, Map<String, String> attributeMap);
}
