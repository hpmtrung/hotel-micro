package com.hba.hbacore.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class StringUtil {
  private StringUtil() {}

  public static List<Integer> underscoreDelimiterToList(String str) {
    return Arrays.stream(str.split("_")).map(Integer::parseInt).collect(Collectors.toList());
  }

}