package io.wonderland.garden.api;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.wonderland.base.Client;
import io.wonderland.garden.domain.Grapheme;
import io.wonderland.protos.GraphemeRequest;
import io.wonderland.protos.GraphemeResponse;
import io.wonderland.protos.LetterFreqGrpc;
import io.wonderland.protos.LetterFreqGrpc.LetterFreqBlockingStub;
import io.wonderland.protos.LetterFreqResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GardenClient extends Client {

  private final LetterFreqBlockingStub letterFreqBlockingStub;

  public GardenClient(ManagedChannel managedChannel) {
    this.letterFreqBlockingStub = LetterFreqGrpc.newBlockingStub(managedChannel);
  }

  public List<String> getLetterFreqLangCodes() {
    return map(letterFreqBlockingStub.getLang(Empty.newBuilder().build()));
  }

  public Grapheme getGrapheme(String langCode) {
    return map(langCode,
        letterFreqBlockingStub.getGrapheme(GraphemeRequest.newBuilder().setKey(langCode).build()));
  }

  public List<String> map(LetterFreqResponse response) {
    return response.getLangCodesList();
  }

  public Grapheme map(String langCode, Iterator<GraphemeResponse> responseIterator) {
    Map<String, byte[]> freq = new HashMap<>();
    while (responseIterator.hasNext()) {
      GraphemeResponse reply = responseIterator.next();
      freq.put(reply.getGramType(), reply.getFreq().toByteArray());
    }
    return new Grapheme(langCode, freq);
  }

  @Override
  public Void ping() {
    //testing if server is reachable
    getLetterFreqLangCodes();
    return null;
  }

}
