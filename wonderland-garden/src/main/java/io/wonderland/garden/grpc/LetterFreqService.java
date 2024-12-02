package io.wonderland.garden.grpc;


import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.wonderland.garden.dao.GraphemeDao;
import io.wonderland.garden.dao.LetterFreqDao;
import io.wonderland.garden.domain.Grapheme;
import io.wonderland.garden.domain.LetterFreq;
import io.wonderland.protos.GraphemeRequest;
import io.wonderland.protos.GraphemeResponse;
import io.wonderland.protos.LetterFreqGrpc;
import io.wonderland.protos.LetterFreqResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LetterFreqService extends LetterFreqGrpc.LetterFreqImplBase {

  private final LetterFreqDao letterFreqDao;
  private final GraphemeDao graphemeDao;

  static LetterFreqResponse getLetterFreqReply(LetterFreq letterFreq) {
    return LetterFreqResponse.newBuilder()
        .setKey(letterFreq.getKey())
        .addAllLangCodes(letterFreq.getLangCodes()).build();

  }

  static GraphemeResponse getGraphemeReply(String key, byte[] value) {
    return GraphemeResponse.newBuilder()
        .setGramType(key)
        .setFreq(ByteString.copyFrom(value))
        .build();

  }

  @Override
  public void getLang(Empty request, StreamObserver<LetterFreqResponse> responseObserver) {
    try {
      log.info("Received LF lang code request.");
      Optional<LetterFreq> optional = letterFreqDao.get(LetterFreq.DEFAULT_KEY);
      optional.ifPresent(letterFreq -> responseObserver.onNext(getLetterFreqReply(letterFreq)));
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(Status.ABORTED.asRuntimeException());
    }
  }

  @Override
  public void getGrapheme(GraphemeRequest request,
      StreamObserver<GraphemeResponse> responseObserver) {
    try {
      log.info("Received LF grapheme {} request.", request.getKey());
      Optional<Grapheme> optionalGrapheme = graphemeDao.get(request.getKey());
      if (optionalGrapheme.isPresent()) {
        Grapheme grapheme = optionalGrapheme.get();
        grapheme.getFreq().forEach((k, v) -> responseObserver.onNext(getGraphemeReply(k, v)));
        responseObserver.onCompleted();
      }
    } catch (Exception e) {
      responseObserver.onError(Status.ABORTED.asRuntimeException());
    }
  }
}
