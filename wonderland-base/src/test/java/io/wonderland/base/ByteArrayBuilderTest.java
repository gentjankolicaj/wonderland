package io.wonderland.base;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ByteArrayBuilderTest {

  @Test
  void constructors() {
    ByteArrayBuilder builder = new ByteArrayBuilder();
    assertThat(builder.toByteArray()).hasSize(0);

    ByteArrayBuilder builder2 = new ByteArrayBuilder().append(new byte[1]).append(new byte[0])
        .append(new byte[2]);
    assertThat(builder2.toByteArray()).hasSize(3);

    ByteArrayBuilder builder3 = new ByteArrayBuilder(new byte[0]).append(new byte[1])
        .append(new byte[0]).append(new byte[2]);
    assertThat(builder3.toByteArray()).hasSize(3);

    ByteArrayBuilder builder4 = new ByteArrayBuilder(new byte[1000]).append(new byte[1])
        .append(new byte[0]).append(new byte[2]);
    assertThat(builder4.toByteArray()).hasSize(1003);
  }


  @Test
  void append() {
    ByteArrayBuilder builder = new ByteArrayBuilder().append(1).append(1024).append('a');
    assertThat(builder.toByteArray()).hasSize(3);

    ByteArrayBuilder builder1 = new ByteArrayBuilder(new byte[100]).append(1).append(1024)
        .append('a');
    assertThat(builder1.toByteArray()).hasSize(103);
  }


  @Test
  void appendTwoBytes() {
    ByteArrayBuilder builder = new ByteArrayBuilder().append(1).appendTwoBytes(1024).append('a');
    assertThat(builder.toByteArray()).hasSize(4);

    ByteArrayBuilder builder1 = new ByteArrayBuilder(new byte[100]).append(1).appendTwoBytes(1024)
        .append('a');
    assertThat(builder1.toByteArray()).hasSize(104);
  }

  @Test
  void appendThreeBytes() {
    ByteArrayBuilder builder = new ByteArrayBuilder().append(1).appendThreeBytes(1024).append('a');
    assertThat(builder.toByteArray()).hasSize(5);

    ByteArrayBuilder builder1 = new ByteArrayBuilder(new byte[100]).append(1).appendThreeBytes(1024)
        .append('a');
    assertThat(builder1.toByteArray()).hasSize(105);
  }

  @Test
  void appendFourBytes() {
    ByteArrayBuilder builder = new ByteArrayBuilder().append(1).appendFourBytes(1024).append('a');
    assertThat(builder.toByteArray()).hasSize(6);

    ByteArrayBuilder builder1 = new ByteArrayBuilder(new byte[100]).append(1).appendFourBytes(1024)
        .append('a');
    assertThat(builder1.toByteArray()).hasSize(106);
  }

  @Test
  void appendOptional() {
    ByteArrayBuilder builder = new ByteArrayBuilder().appendOptimal(65536).appendFourBytes(1024)
        .appendOptimal('a');
    assertThat(builder.toByteArray()).hasSize(8);

    ByteArrayBuilder builder1 = new ByteArrayBuilder(new byte[100]).appendOptimal(65536)
        .appendFourBytes(1024).appendOptimal('a');
    assertThat(builder1.toByteArray()).hasSize(108);
  }

  @Test
  void toByteArray() {
    List<byte[]> empty = List.of();
    assertThat(ByteArrayBuilder.toByteArray(empty)).hasSize(0);

    List<byte[]> list = List.of(new byte[]{1, 2, 3, 4, 5}, new byte[0], new byte[]{6},
        new byte[]{7, 8, 9, 0}, new byte[]{'1', '2'});
    assertThat(ByteArrayBuilder.toByteArray(list)).hasSize(12)
        .containsExactly(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0, '1', '2'});
  }

}