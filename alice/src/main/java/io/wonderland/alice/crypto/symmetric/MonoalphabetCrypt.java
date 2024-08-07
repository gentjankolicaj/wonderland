package io.wonderland.alice.crypto.symmetric;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.CipherParameter;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.MonoalphabetKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.exception.ExceptionMessages;
import io.wonderland.alice.exception.KeyException;
import io.wonderland.alice.exception.RuntimeCipherException;
import java.security.Key;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;


/**
 * * Monoalphabet cipher implementation, works only for UTF-8.
 *
 * <br>Notation:
 * <br> Key: K
 * <br> Plaintext: P
 * <br> Ciphertext: C
 * <br> Keyspace: #
 * <br> Alphabet size: m
 *
 * <br>
 * <br> Monoalphabet cipher function:
 * <br> Key is a map with (key,value) pairs & pairs are characters.One-to-one relationship.
 * <br> Each 'value' character replaces 'key' character plaintext.
 * <br> Keyspace : # = m!, because each character is replaced by another character
 * <br> Encryption : Ci = KEY_MAP.getValue(Pi)
 * <br> Decryption : Pi = KEY_MAP.getKey(Ci)
 * <br>
 * <br>
 * For more check <a href="https://en.wikipedia.org/wiki/Substitution_cipher">Monoalphabet cipher
 * link</a>
 */
@Slf4j
public final class MonoalphabetCrypt implements StreamCipher {

  private Map<Integer, Integer> keys;
  private boolean encryption;


  public int encrypt(byte[] in, int inOff, byte[] out, int outOff) throws RuntimeCipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(ExceptionMessages.PLAINTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ExceptionMessages.ARRAY_OFFSET_NOT_VALID);

    }
    if (MapUtils.isEmpty(keys)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    Set<Integer> set = new TreeSet<>();
    for (int i = 0, len = in.length - inOff; i < len; i++) {
      byte p = in[inOff + i];
      Integer val = keys.get((int) p);
      if (val == null) {
        set.add((int) p);
      } else {
        out[outOff + i] = (byte) (int) val;
      }
    }
    if (CollectionUtils.isNotEmpty(set)) {
      StringBuilder sb = new StringBuilder("Not found mappings for characters : ");
      set.forEach(e -> sb.append((char) (int) e));
      log.warn(sb.toString());
    }
    return out.length;
  }


  public int decrypt(byte[] in, int inOff, byte[] out, int outOff) throws RuntimeCipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(ExceptionMessages.CIPHERTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ExceptionMessages.ARRAY_OFFSET_NOT_VALID);

    }
    Set<Integer> set = new TreeSet<>();
    for (int i = 0, len = in.length - inOff; i < len; i++) {
      byte c = in[inOff + i];
      boolean notFoundValue = true;
      for (Map.Entry<Integer, Integer> entry : keys.entrySet()) {
        if (entry.getValue() != null && entry.getValue() == c) {
          out[outOff + i] = (byte) (int) entry.getKey();
          notFoundValue = false;
        }
      }
      if (notFoundValue) {
        set.add((int) c);
      }
    }
    if (CollectionUtils.isNotEmpty(set)) {
      StringBuilder sb = new StringBuilder("Not found mappings for characters : ");
      set.forEach(e -> sb.append((char) (int) e));
      log.warn(sb.toString());
    }
    return out.length;
  }

  @Override
  public void init(boolean encryption, CipherParameter params) throws IllegalArgumentException {
    if (params instanceof ParameterList) {
      ParameterList parameterList = (ParameterList) params;
      for (CipherParameter param : parameterList) {
        if (param instanceof KeyParameter) {
          Key key = ((KeyParameter<?>) param).getKey();
          if (key instanceof MonoalphabetKey) {
            MonoalphabetKey monoalphabetKey = (MonoalphabetKey) key;
            this.keys = monoalphabetKey.getKey();
            this.encryption = encryption;
            return;
          } else {
            throw new IllegalArgumentException(invalidKeyTypeParamMessage());
          }
        } else if (param instanceof KeyWithIVParameter) {
          Key key = ((KeyWithIVParameter<?>) param).getKey();
          if (key instanceof MonoalphabetKey) {
            MonoalphabetKey monoalphabetKey = (MonoalphabetKey) key;
            this.keys = monoalphabetKey.getKey();
            this.encryption = encryption;
            return;
          } else {
            throw new IllegalArgumentException(invalidKeyTypeParamMessage());
          }
        } else {
          throw new IllegalArgumentException(invalidParamMessage());
        }
      }
    } else if (params instanceof KeyParameter) {
      Key key = ((KeyParameter<?>) params).getKey();
      if (key instanceof MonoalphabetKey) {
        MonoalphabetKey monoalphabetKey = (MonoalphabetKey) key;
        this.keys = monoalphabetKey.getKey();
        this.encryption = encryption;
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
    } else if (params instanceof KeyWithIVParameter) {
      Key key = ((KeyWithIVParameter<?>) params).getKey();
      if (key instanceof MonoalphabetKey) {
        MonoalphabetKey monoalphabetKey = (MonoalphabetKey) key;
        this.keys = monoalphabetKey.getKey();
        this.encryption = encryption;
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
    } else {
      throw new IllegalArgumentException(invalidParamMessage());
    }
  }

  @Override
  public String getAlgorithmName() {
    return Algorithms.MONOALPHABET.getName();
  }

  @Override
  public String[] getKeyTypeNames() {
    return new String[]{MonoalphabetKey.class.getName()};
  }

  @Override
  public byte processByte(byte in) {
    if (MapUtils.isEmpty(keys)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    if (encryption) {
      return (byte) (int) keys.get((int) in);
    } else {
      for (Map.Entry<Integer, Integer> entry : keys.entrySet()) {
        if (entry.getValue() != null && entry.getValue() == in) {
          return (byte) (int) entry.getKey();
        }
      }
      throw new KeyException("Key mapping not found for input : " + in);
    }
  }

  @Override
  public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff)
      throws RuntimeCipherException, IllegalStateException {
    if (encryption) {
      return encrypt(in, inOff, out, outOff);
    } else {
      return decrypt(in, inOff, out, outOff);
    }
  }

  @Override
  public void reset() {
    //nothing to be done to reset monoalphabet
  }

}
