package com.zeroturnaround.rebelanswers.mvc.tools;

import org.pegdown.PegDownProcessor;

public class ThreadSafePegDownProcessor {
  private final ThreadLocal<PegDownProcessor> processor = new ThreadLocal<>();

  public String markdownToHtml(String text) {
    PegDownProcessor p = processor.get();
    if (null == p) {
      p = new PegDownProcessor();
      processor.set(p);
    }

    return p.markdownToHtml(text);
  }
}
