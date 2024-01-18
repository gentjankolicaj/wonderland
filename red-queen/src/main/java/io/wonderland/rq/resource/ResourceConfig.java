package io.wonderland.rq.resource;

import io.wonderland.rq.cryptanalysis.FrequencyResource;
import io.wonderland.rq.type.Language;
import java.util.EnumMap;
import java.util.Map;


public class ResourceConfig {

  public static final String MONOGRAPH = "monograph";
  public static final String DIGRAPH = "digraph";
  public static final String TRIGRAPH = "trigraph";
  public static final Map<Language, Map<String, FrequencyResource>> LANGUAGE_RESOURCES = createResourceMap();


  private static Map<Language, Map<String, FrequencyResource>> createResourceMap() {
    Map<Language, Map<String, FrequencyResource>> map = new EnumMap<>(Language.class);

    //todo: to calculate albanian frequencies from https://wortschatz-leipzig.de/en
    //Add albanian resources entries
    map.put(Language.AL, Map.of(MONOGRAPH, new FrequencyResource("al/monograph_freq.txt", 4_500_000_000L)
        , DIGRAPH, new FrequencyResource("al/digraph_freq.txt", 4_500_000_000L)));

    //Add english resources entries , values received from :
    // http://www.practicalcryptography.com/cryptanalysis/letter-frequencies-various-languages/english-letter-frequencies/
    map.put(Language.EN, Map.of(MONOGRAPH, new FrequencyResource("en/monograph_freq.txt", 4_500_000_000L)
        , DIGRAPH, new FrequencyResource("en/digraph_freq.txt", 4_500_000_000L)
        , TRIGRAPH, new FrequencyResource("en/trigraph_freq.txt", 4_500_000_000L)));

    return map;
  }


}