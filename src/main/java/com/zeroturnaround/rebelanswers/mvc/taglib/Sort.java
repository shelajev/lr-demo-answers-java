package com.zeroturnaround.rebelanswers.mvc.taglib;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Sort {
  public static String[] sortStringArray(String[] strings) {
    List<String> list = Arrays.asList(strings);
    Collections.sort(list);
    String[] result = new String[strings.length];
    return list.toArray(result);
  }
}
