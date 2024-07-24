module crypto {
  requires wonderland.base;
  requires org.bouncycastle.pkix;
  requires org.bouncycastle.provider;
  requires org.bouncycastle.util;
  requires org.bouncycastle.mail;
  requires mail;

  exports io.wonderland.crypto;
  exports io.wonderland.crypto.store;
  exports io.wonderland.crypto.merkle;
  exports io.wonderland.crypto.bc;
}