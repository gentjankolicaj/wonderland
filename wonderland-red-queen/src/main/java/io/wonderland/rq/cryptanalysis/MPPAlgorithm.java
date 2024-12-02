package io.wonderland.rq.cryptanalysis;

import io.wonderland.rq.ds.MPP.MPPValue;
import io.wonderland.struct.Char;
import io.wonderland.struct.Dichar;
import io.wonderland.struct.Monochar;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;

/**
 * Most Probable Pair algorithm : sorts characters list based on most close frequency
 */
public class MPPAlgorithm {

  private MPPAlgorithm() {
  }

  public static Map<Monochar, List<MPPValue<? extends Char>>> monocharCalc(
      Map<Monochar, Double> langFreq,
      Map<Monochar, Double> ciphertextFreq) {
    if (MapUtils.isEmpty(langFreq) || MapUtils.isEmpty(ciphertextFreq)) {
      return (Map<Monochar, List<MPPValue<? extends Char>>>) MapUtils.EMPTY_SORTED_MAP;
    }
    Map<Monochar, List<MPPValue<? extends Char>>> mppMap = new HashMap<>();

    for (Map.Entry<Monochar, Double> langEntry : langFreq.entrySet()) {
      List<MPPValue<Monochar>> list = new ArrayList<>();
      for (Map.Entry<Monochar, Double> ciphertextEntry : ciphertextFreq.entrySet()) {
        list.add(MPPValue.of(ciphertextEntry.getKey(), ciphertextEntry.getValue(),
            calcFreqDiff(ciphertextEntry.getValue(), langEntry.getValue())));
      }
      List<MPPValue<? extends Char>> sortedList = list.stream()
          .sorted(Comparator.comparing(MPPValue::getFreqDiff))
          .collect(
              Collectors.toList());
      mppMap.put(langEntry.getKey(), sortedList);
    }
    return mppMap;
  }

  public static Map<Dichar, List<MPPValue<? extends Char>>> dicharCalc(Map<Dichar, Double> langFreq,
      Map<Dichar, Double> ciphertextFreq) {
    if (MapUtils.isEmpty(langFreq) || MapUtils.isEmpty(ciphertextFreq)) {
      return (Map<Dichar, List<MPPValue<? extends Char>>>) MapUtils.EMPTY_SORTED_MAP;
    }
    Map<Dichar, List<MPPValue<? extends Char>>> mppMap = new HashMap<>();

    for (Map.Entry<Dichar, Double> langEntry : langFreq.entrySet()) {
      List<MPPValue<Dichar>> list = new ArrayList<>();
      for (Map.Entry<Dichar, Double> ciphertextEntry : ciphertextFreq.entrySet()) {
        list.add(MPPValue.of(ciphertextEntry.getKey(), ciphertextEntry.getValue(),
            calcFreqDiff(ciphertextEntry.getValue(), langEntry.getValue())));
      }
      List<MPPValue<? extends Char>> sortedList = list.stream()
          .sorted(Comparator.comparing(MPPValue::getFreqDiff))
          .collect(
              Collectors.toList());
      mppMap.put(langEntry.getKey(), sortedList);
    }
    return mppMap;
  }

  public static Double calcFreqDiff(Double a, Double b) {
    if (a.equals(0)) {
      return 0.0;
    }
    return a - b;
  }


}
