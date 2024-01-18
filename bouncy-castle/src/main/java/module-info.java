module bouncy.castle {
  requires java.base;
  requires org.apache.commons.lang3;
  requires org.apache.commons.collections4;
  requires org.apache.commons.io;
  requires lombok;
  requires org.slf4j;
  requires org.bouncycastle.pkix;
  requires org.bouncycastle.provider;
  requires org.bouncycastle.util;

  exports io.wonderland.bc;
  exports io.wonderland.bc.cert;
  exports io.wonderland.bc.store;
}