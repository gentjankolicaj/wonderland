package io.wonderland.crypto;

import java.util.Date;

public class DateUtils {

  /**
   * Calculate a date in seconds (suitable for the PKIX profile - RFC 5280)
   *
   * @param hoursInFuture hours ahead of now, may be negative.
   * @return a Date set to now + (hoursInFuture * 60 * 60) seconds
   */
  public static Date calcDate(int hoursInFuture) {
    long secs = System.currentTimeMillis() / 1000;
    return new Date((secs + ((long) hoursInFuture * 60 * 60)) * 1000);
  }

}
