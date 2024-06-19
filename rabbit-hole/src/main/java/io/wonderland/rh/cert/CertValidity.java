package io.wonderland.rh.cert;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CertValidity {

  private final Date notBefore;
  private final Date notAfter;

  public static CertValidity ofYears(final int count) {
    final ZonedDateTime zdtNotBefore = ZonedDateTime.now();
    final ZonedDateTime zdtNotAfter = zdtNotBefore.plusYears(count);
    return of(zdtNotBefore.toInstant(), zdtNotAfter.toInstant());
  }

  public static CertValidity of(final Instant notBefore, final Instant notAfter) {
    return new CertValidity(Date.from(notBefore), Date.from(notAfter));
  }
}

