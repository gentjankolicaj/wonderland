module alice {
  requires wonderland.base;
  requires com.beanit.asn1bean;

  exports io.wonderland.alice.asn1.affine;
  exports io.wonderland.alice.asn1.algorithm;
  exports io.wonderland.alice.asn1.caesar;
  exports io.wonderland.alice.asn1.otp;
  exports io.wonderland.alice.asn1.railfence;
  exports io.wonderland.alice.asn1.vernam;
  exports io.wonderland.alice.asn1.vigenere;
  exports io.wonderland.alice.asn1.rsa;
  exports io.wonderland.alice.crypto;
  exports io.wonderland.alice.crypto.key.keypair;
  exports io.wonderland.alice.crypto.key;
  exports io.wonderland.alice.crypto.key.keypair.gen;
  exports io.wonderland.alice.crypto.key.secretkey;
  exports io.wonderland.alice.crypto.params;
  exports io.wonderland.alice.crypto.mode;
  exports io.wonderland.alice.codec;
  exports io.wonderland.alice.exception;
  exports io.wonderland.alice.jca.asymmetric;
  exports io.wonderland.alice.jca.asymmetric.rsa;
  exports io.wonderland.alice.jca.symmetric.permutation;
  exports io.wonderland.alice.jca.asymmetric.dh;
  exports io.wonderland.alice.jca.symmetric;
  exports io.wonderland.alice.jca;

  //Directive to be a service provider so other modules can consume.
  provides java.security.Provider with io.wonderland.alice.jca.AliceProvider;
}