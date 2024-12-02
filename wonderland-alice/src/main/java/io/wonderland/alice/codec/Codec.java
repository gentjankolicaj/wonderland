package io.wonderland.alice.codec;

public interface Codec<E, D> {

  E encoder();

  D decoder();

}
