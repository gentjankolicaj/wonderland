package io.wonderland.garden.resource;

import static org.assertj.core.api.Assertions.assertThat;

import io.atlassian.fugue.Pair;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class GardenFilesResourceTest {


  @Test
  void byteLetterFreqResource() throws IOException, URISyntaxException {
    Map<String, Map<String, Pair<Path, Function<Path, Optional<byte[]>>>>> map = GardenFilesResource.byteLetterFreqResource();

    Map<String, Pair<Path, Function<Path, Optional<byte[]>>>> en = map.get("en");
    assertThat(en).containsKeys("monogram", "digram", "trigram");
    assertThat(map).isNotEmpty();
  }

}