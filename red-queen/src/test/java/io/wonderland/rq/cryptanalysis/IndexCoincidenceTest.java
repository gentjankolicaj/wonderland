package io.wonderland.rq.cryptanalysis;

public class IndexCoincidenceTest {

  /**
   @Test void calcIC() {
   byte[] ciphertext="vptnvffuntshtarptymjwzirappljmhhqvsubwlzzygvtyitarptyiougxiuydtgzhhvvmumshwkzgstfmekvmpkswdgbilvjljmglmjfqwioiivknulvvfemioiemojtywdsajtwmtcgluysdsumfbieugmvalvxkjduetukatymvkqzhvqvgvptytjwwldyeevquhlulwpkt".getBytes();
   double ic= IndexCoincidence.calcIC(ciphertext,1);
   assertThat(ic).isLessThan(0.045);
   }

   @Test void calcCharIC(){
   Map<Monochar, Integer> freq = LetterFrequency.monocharFreq(
   ResourceConstants.LANGUAGE_RESOURCES.get(Language.EN).get(GramType.MONOGRAM));
   double ic= IndexCoincidence.calcCharIC(freq);
   assertThat(ic).isBetween(0.065,0.066);
   }

   @Test void calcKeyPeriodIC(){
   byte[] ciphertext="vptnvffuntshtarptymjwzirappljmhhqvsubwlzzygvtyitarptyiougxiuydtgzhhvvmumshwkzgstfmekvmpkswdgbilvjljmglmjfqwioiivknulvvfemioiemojtywdsajtwmtcgluysdsumfbieugmvalvxkjduetukatymvkqzhvqvgvptytjwwldyeevquhlulwpkt".getBytes();
   Map<Integer,Double> ics= IndexCoincidence.calcKeyPeriodIC(ciphertext ,1,1,20);
   assertThat(ics.get(1)).isBetween(0.043,0.045);
   assertThat(ics.get(7)).isBetween(0.089,0.091);
   }
   @Test void createCiphertextMat() {
   byte[] ciphertext="Hello world of cryptography".getBytes();
   List<Byte>[] mat1= IndexCoincidence.createCiphertextMat(ciphertext,1,2);
   assertThat(mat1).hasSize(2);
   assertThat(mat1[0]).containsAnyOf((byte)72);
   List<Byte>[] mat2= IndexCoincidence.createCiphertextMat(ciphertext,1,3);
   assertThat(mat2).hasSize(3);
   assertThat(mat2[2]).containsAnyOf((byte)121);
   List<Byte>[] mat3= IndexCoincidence.createCiphertextMat(ciphertext,1,4);
   assertThat(mat3).hasSize(4);
   assertThat(mat3[1]).containsAnyOf((byte)32);

   List<Byte>[] mat4= IndexCoincidence.createCiphertextMat(ciphertext,2,2);
   assertThat(mat4).hasSize(2);
   assertThat(mat4[1]).containsAnyOf((byte)32);
   List<Byte>[] mat5= IndexCoincidence.createCiphertextMat(ciphertext,2,3);
   assertThat(mat5).hasSize(3);
   assertThat(mat5[1]).containsAnyOf((byte)99);
   List<Byte>[] mat6= IndexCoincidence.createCiphertextMat(ciphertext,2,4);
   assertThat(mat6).hasSize(4);
   assertThat(mat6[2]).containsAnyOf((byte)102);

   List<Byte>[] mat7= IndexCoincidence.createCiphertextMat(ciphertext,3,2);
   assertThat(mat7).hasSize(2);
   assertThat(mat7[0]).containsAnyOf((byte)119);
   List<Byte>[] mat8= IndexCoincidence.createCiphertextMat(ciphertext,3,3);
   assertThat(mat8).hasSize(3);
   assertThat(mat8[1]).containsAnyOf((byte)32);
   List<Byte>[] mat9= IndexCoincidence.createCiphertextMat(ciphertext,3,4);
   assertThat(mat9).hasSize(4);
   assertThat(mat9[0]).containsAnyOf((byte)101);
   }


   @Test void createMat() {
   assertThatThrownBy(()-> IndexCoincidence.createMat(0,2)).isInstanceOf(IllegalArgumentException. class);
   assertThat(IndexCoincidence.createMat(2,1).length).isEqualTo(1);
   assertThat(IndexCoincidence.createMat(2,1)[0].length).isEqualTo(2);
   assertThat(IndexCoincidence.createMat(3,2).length).isEqualTo(2);
   assertThat(IndexCoincidence.createMat(3,2)[0].length).isEqualTo(2);
   assertThat(IndexCoincidence.createMat(3,2)[1].length).isEqualTo(1);
   assertThat(IndexCoincidence.createMat(4,2).length).isEqualTo(2);
   assertThat(IndexCoincidence.createMat(4,2)[0].length).isEqualTo(2);
   }

   @Test void getIterations() {
   assertThat(IndexCoincidence.getIterations(5,2,1)).isEqualTo(1);
   assertThat(IndexCoincidence.getIterations(5,5,3)).isEqualTo(0);
   assertThat(IndexCoincidence.getIterations(5,4,3)).isEqualTo(1);
   }
   */
}