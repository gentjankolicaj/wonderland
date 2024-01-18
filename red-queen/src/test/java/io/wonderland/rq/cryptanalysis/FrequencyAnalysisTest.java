package io.wonderland.rq.cryptanalysis;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import io.wonderland.rq.type.Dichar;
import io.wonderland.rq.type.Monochar;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class FrequencyAnalysisTest {

  @Test
  void monocharFreq() {
    byte[] text = "ABCDEBaDADADDBAabcdea".getBytes();
    Map<Monochar, Integer> freq = FrequencyAnalysis.monocharFreq(text, 1);
    assertThat(freq).isNotEmpty().hasSize(10);
    assertThat(freq).containsEntry(new Monochar('A'), 4);
    assertThat(freq).containsEntry(new Monochar('D'), 5);
    assertThat(freq).containsEntry(new Monochar('a'), 3);
    assertThat(freq).containsEntry(new Monochar('b'), 1);

    //With byte encoding number 2, we take 2 bytes at a time.
    byte[] text2 = "ABCDEBaDADADDBAabcdeaa".getBytes();
    Map<Monochar, Integer> freq2 = FrequencyAnalysis.monocharFreq(text2, 2);
    assertThat(freq2).isNotEmpty().hasSize(10);

    Map<Monochar, Double> freqPct = FrequencyAnalysis.monocharFreqPct(text, 1);
    assertThat(freqPct).isNotEmpty().hasSize(10);
    assertThat(freqPct).containsEntry(new Monochar('A'),(double) 4/text.length);
    assertThat(freqPct).containsEntry(new Monochar('D'),(double) 5/text.length);
    assertThat(freqPct).containsEntry(new Monochar('a'),(double) 3/text.length);
    assertThat(freqPct).containsEntry(new Monochar('b'),(double) 1/text.length);
  }

  @Test
  void dicharFreq() {
    byte[] text = "ABCDEBaDADDBABACFAWabDDAADDBAABabcdeaa".getBytes();
    Map<Dichar, Integer> freq = FrequencyAnalysis.dicharFreq(text, 1);
    assertThat(freq).isNotEmpty().hasSize(25);
    assertThat(freq).containsEntry(new Dichar('A','B'), 3);
    assertThat(freq).containsEntry(new Dichar('D','A'), 2);
    assertThat(freq).containsEntry(new Dichar('a','a'), 1);
    assertThat(freq).containsEntry(new Dichar('a','b'), 2);

    Map<Dichar, Double> freqPct = FrequencyAnalysis.dicharFreqPct(text, 1);
    assertThat(freqPct).isNotEmpty().hasSize(25);
    assertThat(freqPct).containsEntry(new Dichar('A','B'),(double) 3/text.length);
    assertThat(freqPct).containsEntry(new Dichar('D','A'),(double) 2/text.length);
    assertThat(freqPct).containsEntry(new Dichar('a','a'),(double) 1/text.length);
    assertThat(freqPct).containsEntry(new Dichar('a','b'),(double) 2/text.length);

    Map<Dichar, Integer> freq2 = FrequencyAnalysis.dicharFreq(text, 2);
    assertThat(freq2).isNotEmpty().hasSize(33);

  }
}