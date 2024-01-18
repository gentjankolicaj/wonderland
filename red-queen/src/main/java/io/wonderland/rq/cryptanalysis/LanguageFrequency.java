package io.wonderland.rq.cryptanalysis;

import io.wonderland.rq.exception.ResourceException;
import io.wonderland.rq.type.Dichar;
import io.wonderland.rq.type.Monochar;
import io.wonderland.rq.type.Trichar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * This class is helper class for getting character frequencies of different human language
 */
public final class LanguageFrequency {

  private LanguageFrequency() {
  }


  public static Map<Monochar, Integer> monocharFreq(FrequencyResource res) {
    File file = FileUtils.getFile(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(res.getFilePath()))
            .getPath());
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      Map<Monochar, Integer> freq = new HashMap<>();
      final String separator = ":";
      String str;
      while ((str = br.readLine()) != null) {
        str = str.trim();
        String finalStr = str;
        freq.computeIfAbsent(new Monochar(str.substring(0, str.indexOf(separator))),
            k -> k == null ? 0 : Integer.parseInt(StringUtils.trim(finalStr.substring(finalStr.indexOf(separator) + 1
            ))));
      }
      return freq;
    } catch (IOException e) {
      throw new ResourceException(e);
    }
  }

  public static Map<Monochar, Double> monocharFreqPct(FrequencyResource res) {
    File file = FileUtils.getFile(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(res.getFilePath()))
            .getPath());
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      Map<Monochar, Double> freq = new HashMap<>();
      final String separator = ":";
      String str;
      while ((str = br.readLine()) != null) {
        str = str.trim();
        String finalStr = str;
        freq.computeIfAbsent(new Monochar(str.substring(0, str.indexOf(separator))),
            k -> Double.parseDouble(StringUtils.trim(finalStr.substring(finalStr.indexOf(separator) + 1
            ))));
      }
      freq.entrySet().forEach(entry -> entry.setValue(entry.getValue() / res.getCharsetSize()));
      return freq;
    } catch (IOException e) {
      throw new ResourceException(e);
    }
  }


  public static Map<Dichar, Integer> dicharFreq(FrequencyResource res) {
    File file = FileUtils.getFile(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(res.getFilePath()))
            .getPath());
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      Map<Dichar, Integer> freq = new HashMap<>();
      final String separator = ":";
      String str;
      while ((str = br.readLine()) != null) {
        str = str.trim();
        String finalStr = str;
        freq.computeIfAbsent(new Dichar(str.substring(0, 1), str.substring(1, str.indexOf(separator))),
            k -> Integer.parseInt(StringUtils.trim(finalStr.substring(finalStr.indexOf(separator) + 1
            ))));
      }
      return freq;
    } catch (IOException e) {
      throw new ResourceException(e);
    }
  }

  public static Map<Dichar, Double> dicharFreqPct(FrequencyResource res) {
    File file = FileUtils.getFile(
        Thread.currentThread().getContextClassLoader().getResource(res.getFilePath()).getPath());
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      Map<Dichar, Double> freq = new HashMap<>();
      final String separator = ":";
      String str;
      while ((str = br.readLine()) != null) {
        str = str.trim();
        String finalStr = str;
        freq.computeIfAbsent(new Dichar(str.substring(0, 1), str.substring(1, str.indexOf(separator))),
            k -> Double.parseDouble(StringUtils.trim(finalStr.substring(finalStr.indexOf(separator) + 1
            ))));
      }
      freq.entrySet().forEach(entry -> entry.setValue(entry.getValue() / res.getCharsetSize()));
      return freq;
    } catch (IOException e) {
      throw new ResourceException(e);
    }
  }


  public static Map<Trichar, Integer> tricharFreq(FrequencyResource res) {
    File file = FileUtils.getFile(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(res.getFilePath()))
            .getPath());
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      Map<Trichar, Integer> freq = new HashMap<>();
      final String separator = ":";
      String str;
      while ((str = br.readLine()) != null) {
        str = str.trim();
        String finalStr = str;
        freq.computeIfAbsent(
            new Trichar(str.substring(0, 1), str.substring(1, 2), str.substring(2, str.indexOf(separator))),
            k -> Integer.parseInt(StringUtils.trim(finalStr.substring(finalStr.indexOf(separator) + 1
            ))));
      }
      return freq;
    } catch (IOException e) {
      throw new ResourceException(e);
    }
  }

  public static Map<Trichar, Double> tricharFreqPct(FrequencyResource res) {
    File file = FileUtils.getFile(
        Thread.currentThread().getContextClassLoader().getResource(res.getFilePath()).getPath());
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      Map<Trichar, Double> freq = new HashMap<>();
      final String separator = ":";
      String str;
      while ((str = br.readLine()) != null) {
        str = str.trim();
        String finalStr = str;
        freq.computeIfAbsent(
            new Trichar(str.substring(0, 1), str.substring(1, 2), str.substring(2, str.indexOf(separator))),
            k -> Double.parseDouble(StringUtils.trim(finalStr.substring(finalStr.indexOf(separator) + 1
            ))));
      }
      freq.entrySet().forEach(entry -> entry.setValue(entry.getValue() / res.getCharsetSize()));
      return freq;
    } catch (IOException e) {
      throw new ResourceException(e);
    }
  }


}
