package io.wonderland.alice.crypto.symmetric;

import static io.wonderland.alice.exception.ExceptionMessages.ARRAY_OFFSET_NOT_VALID;
import static io.wonderland.alice.exception.ExceptionMessages.PLAINTEXT_NOT_VALID;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.CipherParameter;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.secretkey.RailfenceKey;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.KeyWithIVParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.crypto.params.RawKeyParameter;
import io.wonderland.alice.exception.RuntimeCipherException;
import io.wonderland.base.IntUtils;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Rail fence cipher implementation.
 *
 * <br>Notation:
 * <br> Key: K
 * <br> Plaintext: P
 * <br> Ciphertext: C
 * <br> Keyspace: #
 * <br> Alphabet size: m
 *
 * <br>
 * <br> Rail fence cipher function:
 * <br> K ∈ {0,1,2,3...26}
 * <br> Keyspace : # = ?
 * <br> Encryption :
 * <p>In the rail fence cipher, the plaintext is written downwards diagonally on successive "rails"
 * of an imaginary fence, then moving up when the bottom rail is reached, down again when the top
 * rail is reached, and so on until the whole plaintext is written out. The ciphertext is then read
 * off in rows.</p>
 * <br> Decryption :
 * <p>Let N be the number of rails used during encryption.Observe that as the plaintext is written,
 * the sequence of each letter's vertical position on the rails varies up and down in a repeating
 * cycle. In the above example N=3 the vertical position repeats with a period of 4. In general the
 * sequence repeats with a period of 2(N-1). Let L be the length of the string to be decrypted.
 * Suppose for a moment that L is a multiple of 2(N-1) and let K=L/2(N-1).One begins splitting the
 * ciphertext into strings such that the length of the first and last string is K and the length of
 * each intermediate string is 2K For above example L=24 and we have K=6, so se split text into 6 12
 * 6</p>
 * <br>
 * For more read : <br>
 * <a href="https://en.wikipedia.org/wiki/Rail_fence_cipher">Rail fence link</a>
 * <a
 * href="https://www.baeldung.com/cs/cryptography-rail-fence-cipher">cryptography-rail-fence-cipher</a>
 */
@Slf4j
public final class RailfenceCrypt implements StreamCipher {

  private int rails;
  private boolean encryption;

  public RailfenceCrypt() {
  }


  public int encrypt(byte[] in, int inOff, byte[] out, int outOff) throws RuntimeCipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(PLAINTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ARRAY_OFFSET_NOT_VALID);
    }
    Byte[][] matrix = new Byte[rails][in.length];
    int row = 0;
    boolean dirDown = false;
    for (int i = 0, len = in.length - inOff; i < len; i++) {
      if (row == 0 || row == rails - 1) {
        dirDown = !dirDown;
      }
      matrix[row][i] = in[inOff + i];
      if (dirDown) {
        row++;
      } else {
        row--;
      }
    }
    List<Byte> transposedBytes = new ArrayList<>();
    for (Byte[] arr : matrix) {
      for (Byte b : arr) {
        if (b != null) {
          transposedBytes.add(b);
        }
      }
    }

    for (int i = 0, len = transposedBytes.size(); i < len; i++) {
      out[i] = transposedBytes.get(i);
    }
    return out.length;
  }


  public int decrypt(byte[] in, int inOff, byte[] out, int outOff) throws RuntimeCipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(PLAINTEXT_NOT_VALID);
    }
    if (inOff < 0 || outOff < 0) {
      throw new IllegalArgumentException(ARRAY_OFFSET_NOT_VALID);

    }

    Byte[][] matrix = new Byte[rails][in.length];

    //fill candidate slots with marker
    int row = 0;
    int col = 0;
    boolean dirDown = true;
    for (int i = 0, len = in.length - inOff; i < len; i++) {
      if (row == 0) {
        dirDown = true;
      }
      if (row == rails - 1) {
        dirDown = false;
      }
      matrix[row][col++] = '*';
      if (dirDown) {
        row++;
      } else {
        row--;
      }
    }
    //Replace marker with character value on matrix
    int index = 0;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        if (matrix[i][j] != null && matrix[i][j] == '*') {
          matrix[i][j] = in[index];
          index++;
        }
      }
    }

    //Add zig-zag value into list
    List<Byte> nonTransposedBytes = new ArrayList<>();
    row = 0;
    col = 0;
    for (int i = 0, len = in.length - inOff; i < len; i++) {
      if (row == 0) {
        dirDown = true;
      }
      if (row == rails - 1) {
        dirDown = false;
      }
      nonTransposedBytes.add(matrix[row][col++]);
      if (dirDown) {
        row++;
      } else {
        row--;
      }
    }

    for (int i = 0, len = nonTransposedBytes.size(); i < len; i++) {
      out[i] = nonTransposedBytes.get(i);
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
          if (key instanceof RailfenceKey) {
            this.rails = ((RailfenceKey) key).getRails();
            this.encryption = encryption;
            return;
          } else {
            throw new IllegalArgumentException(invalidKeyTypeParamMessage());
          }
        } else if (param instanceof KeyWithIVParameter) {
          Key key = ((KeyWithIVParameter<?>) param).getKey();
          if (key instanceof RailfenceKey) {
            this.rails = ((RailfenceKey) key).getRails();
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
      if (key instanceof RailfenceKey) {
        this.rails = ((RailfenceKey) key).getRails();
        this.encryption = encryption;
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
    } else if (params instanceof KeyWithIVParameter) {
      Key key = ((KeyWithIVParameter<?>) params).getKey();
      if (key instanceof RailfenceKey) {
        this.rails = ((RailfenceKey) key).getRails();
        this.encryption = encryption;
      } else {
        throw new IllegalArgumentException(invalidKeyTypeParamMessage());
      }
    } else if (params instanceof RawKeyParameter) {
      RawKeyParameter rawKeyParameter = (RawKeyParameter) params;
      this.rails = IntUtils.parseInt(rawKeyParameter.getKey());
      this.encryption = encryption;
    } else {
      throw new IllegalArgumentException(invalidParamMessage());
    }
  }

  @Override
  public String getAlgorithmName() {
    return Algorithms.RAILFENCE.getName();
  }

  @Override
  public String[] getKeyTypeNames() {
    return new String[]{RailfenceKey.class.getName()};
  }

  @Override
  public byte processByte(byte in) {
    return in;
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
    //No need to reset railfence cipher
  }

}