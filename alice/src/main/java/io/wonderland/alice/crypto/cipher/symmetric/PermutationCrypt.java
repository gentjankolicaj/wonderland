package io.wonderland.alice.crypto.cipher.symmetric;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.BlockCipher;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.crypto.TranspositionCipher;
import io.wonderland.alice.crypto.key.secretkey.PermutationKey;
import io.wonderland.alice.crypto.padding.BlockPadding;
import io.wonderland.alice.crypto.padding.ZeroPadding;
import io.wonderland.alice.crypto.params.KeyParameter;
import io.wonderland.alice.crypto.params.ParameterList;
import io.wonderland.alice.exception.CipherException;
import io.wonderland.alice.exception.DataLengthException;
import io.wonderland.alice.exception.ExceptionMessages;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;


//todo : padding removal
public class PermutationCrypt implements TranspositionCipher, BlockCipher {

  private final BlockPadding<Byte> blockPadding;
  private int[] columnOrders;
  private boolean encryption;

  public PermutationCrypt() {
    this.blockPadding = new ZeroPadding();
    this.encryption = true;
  }

  public PermutationCrypt(BlockPadding<Byte> blockPadding, boolean encryption) {
    this.blockPadding = blockPadding;
    this.encryption = encryption;
  }

  @Override
  public void transpose(byte[] in, byte[] out) throws CipherException {
    if (ArrayUtils.isEmpty(in)) {
      throw new IllegalArgumentException(ExceptionMessages.PLAINTEXT_NOT_VALID);
    }
    if (ArrayUtils.isEmpty(out)) {
      throw new IllegalArgumentException(ExceptionMessages.CIPHERTEXT_NOT_VALID);
    }
    if (ArrayUtils.isEmpty(columnOrders)) {
      throw new IllegalArgumentException(
          ExceptionMessages.KEY_NOT_VALID + " Column order not valid.");
    }
    if (encryption) {
      encrypt(in, out);
    } else {
      decrypt(in, out);
    }
  }

  private void encrypt(byte[] in, byte[] out) {
    int columnNum = columnOrders.length;
    int rowNum =
        in.length % columnOrders.length == 0 ? in.length / columnOrders.length
            : (in.length / columnOrders.length) + 1;
    byte[][] matrix = new byte[rowNum][columnNum];

    //distribute plaintext alphabets in matrix
    for (int i = 0, len = in.length; i < len; i++) {
      matrix[i / columnNum][i % columnNum] = in[i];
    }

    //calculate the number of paddings needed & columns to pad
    int padNum = padNum(columnOrders, in);

    for (int i = rowNum - 1; i < rowNum; i++) {
      for (int j = columnNum - 1; j >= columnNum - padNum; j--) {
        matrix[i][j] = blockPadding.getPad();
      }
    }

    //select permuted alphabets in matrix based on column key values
    //columnOrder[i]%columnOrder.length => To make it a ring
    //  because columnOrder values can be bigger than columnOrder.length
    for (int i = 0; i < columnOrders.length; i++) {
      int order = columnOrders[i] % columnOrders.length;
      for (int j = 0; j < rowNum; j++) {
        out[order * rowNum + j] = matrix[j][i];
      }
    }
  }

  private void decrypt(byte[] in, byte[] out) {
    int columnNum = columnOrders.length;
    int rowNum =
        in.length % columnOrders.length == 0 ? in.length / columnOrders.length
            : (in.length / columnOrders.length) + 1;
    byte[][] matrix = new byte[rowNum][columnNum];

    //calculate the number of paddings needed & columns to pad
    List<Integer> columnIndexToPad = getColumnIndexToPad(in, rowNum, columnNum);

    //sort columns to pad to avoid index out of bounds
    columnIndexToPad.sort(Integer::compareTo);

    //tmp list to hold input + padding at specific columns
    List<Byte> inputPadded = new ArrayList<>();
    for (byte b : in) {
      inputPadded.add(b);
    }

    //Pad input at column index specified
    for (Integer colIndex : columnIndexToPad) {
      int indexToPad = colIndex * rowNum + rowNum - 1;
      inputPadded.add(indexToPad, (byte) -1);
    }

    //distribute padded ciphertext alphabets in matrix
    for (int i = 0, len = inputPadded.size(); i < len; i++) {
      matrix[i % rowNum][i / rowNum] = inputPadded.get(i);
    }

    //plaintext based on key column order
    byte[] tmp = new byte[columnNum * rowNum];
    for (int i = 0, len = columnOrders.length; i < len; i++) {
      int order = columnOrders[i] % columnOrders.length;
      for (int j = 0; j < rowNum; j++) {
        tmp[i * rowNum + j] = matrix[j][order];
      }
    }

    //distribute plaintext alphabets in matrix
    for (int i = 0, len = tmp.length; i < len; i++) {
      matrix[i % rowNum][i / rowNum] = tmp[i];
    }

    for (int i = 0, len = out.length; i < len; i++) {
      out[i] = matrix[i / columnNum][i % columnNum];
    }
  }


  private List<Integer> getColumnIndexToPad(byte[] in, int rowNum, int columnNum) {
    //calculate if temp padding is in need to decrypt
    // tmp padding number is total index - input length.
    int paddingNum = rowNum * columnNum - in.length;
    List<Integer> colIndexToPad = new ArrayList<>();
    if (paddingNum > 0) {
      //columnOrder[i]%columnOrder.length => To make it a ring
      //because columnOrder values can be bigger than columnOrder.length and basically we need order
      for (int i = columnOrders.length - 1; i >= columnOrders.length - paddingNum; i--) {
        colIndexToPad.add(columnOrders[i] % columnOrders.length);
      }
    }
    return colIndexToPad;
  }

  void setColumnOrders(int[] columnOrders) {
    this.columnOrders = columnOrders;
  }

  boolean canPad(int[] columnOrder, byte[] in) {
    int columnNum = columnOrder.length;
    int rowNum =
        in.length % columnOrder.length == 0 ? in.length / columnOrder.length
            : (in.length / columnOrder.length) + 1;
    return columnNum * rowNum > in.length;
  }

  int padNum(int[] columnOrder, byte[] in) {
    int columnNum = columnOrder.length;
    int rowNum =
        in.length % columnOrder.length == 0 ? in.length / columnOrder.length
            : (in.length / columnOrder.length) + 1;
    return (columnNum * rowNum) - in.length;
  }

  byte[] pad(int[] columnOrder, byte[] in) {
    int diff = padNum(columnOrder, in);
    if (diff > 0) {
      byte[] newInput = new byte[in.length + diff];
      System.arraycopy(in, 0, newInput, 0, in.length);
      blockPadding.addPadding(newInput, in.length);
      return newInput;
    }
    return in;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void init(boolean encryption, CipherParameters params) throws IllegalArgumentException {
    if (params instanceof ParameterList) {
      ParameterList parameterList = (ParameterList) params;
      for (CipherParameters param : parameterList) {
        if (param instanceof KeyParameter) {
          Key key = ((KeyParameter<?>) param).getKey();
          if (key instanceof PermutationKey) {
            PermutationKey permutationKey = (PermutationKey) key;
            this.columnOrders = permutationKey.getColumnOrder();
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
      if (key instanceof PermutationKey) {
        PermutationKey permutationKey = (PermutationKey) key;
        this.columnOrders = permutationKey.getColumnOrder();
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
    return new String[]{PermutationKey.class.getName()};
  }

  @Override
  public int getBlockSize() {
    return 0;
  }


  @Override
  public int processBlock(byte[] in, int inOff, byte[] out, int outOff) throws DataLengthException {
    if (encryption) {
      encrypt(in, out);
    } else {
      decrypt(in, out);
    }
    return out.length - outOff;
  }

  @Override
  public void reset() {
    //do nothing
  }
}
