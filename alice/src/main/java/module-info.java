module alice {
  requires java.base;
  requires org.apache.commons.lang3;
  requires org.apache.commons.collections4;
  requires org.apache.commons.io;
  requires lombok;
  requires org.slf4j;
  requires jmathematics;
  requires common;

  provides java.security.Provider with io.wonderland.alice.jca.AliceProvider;
  exports io.wonderland.alice.jca.cipher;

}