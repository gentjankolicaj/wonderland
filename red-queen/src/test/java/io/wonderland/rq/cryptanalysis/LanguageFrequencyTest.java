package io.wonderland.rq.cryptanalysis;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import io.wonderland.rq.resource.ResourceConfig;
import io.wonderland.rq.type.Dichar;
import io.wonderland.rq.type.Language;
import io.wonderland.rq.type.Monochar;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class LanguageFrequencyTest {

  @Test
  void monocharFreq() {
    Map<Monochar,Integer> freq= LanguageFrequency.monocharFreq(ResourceConfig.LANGUAGE_RESOURCES.get(Language.EN).get(ResourceConfig.MONOGRAPH));

    assertThat(freq).hasSize(26);
    assertThat(freq.get(new Monochar('E'))).isEqualTo(529117365);
    assertThat(freq.get(new Monochar('Q'))).isEqualTo(4550166);

    Map<Monochar,Double> freqPct= LanguageFrequency.monocharFreqPct(ResourceConfig.LANGUAGE_RESOURCES.get(Language.EN).get(ResourceConfig.MONOGRAPH));
    assertThat(freqPct).hasSize(26);
    assertThat(freqPct.get(new Monochar('E'))).isLessThan(0.117581638);
    assertThat(freqPct.get(new Monochar('Q'))).isLessThan(0.001011149);
  }

  @Test
  void dicharFreq() {
    Map<Dichar,Integer> freq= LanguageFrequency.dicharFreq(ResourceConfig.LANGUAGE_RESOURCES.get(Language.EN).get(ResourceConfig.DIGRAPH));

    assertThat(freq).hasSize(676);
    assertThat(freq.get(new Dichar('T','H'))).isEqualTo(116997844);
    assertThat(freq.get(new Dichar('Q','Z'))).isEqualTo(280);

    Map<Dichar,Double> freqPct= LanguageFrequency.dicharFreqPct(ResourceConfig.LANGUAGE_RESOURCES.get(Language.EN).get(ResourceConfig.DIGRAPH));
    assertThat(freqPct).hasSize(676);
    assertThat(freqPct.get(new Dichar('T','H'))).isLessThan(0.025999522);
    assertThat(freqPct.get(new Dichar('Q','Z'))).isLessThan(0.000000063);
  }
}