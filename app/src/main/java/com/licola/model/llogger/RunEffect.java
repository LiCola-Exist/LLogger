package com.licola.model.llogger;

import com.licola.llogger.LLogger;
import java.util.ArrayList;

/**
 * 日志写入运行效率测试
 * @author LiCola
 * @date 2018/8/13
 */
public class RunEffect {

  private static final String CONTENT;

  static {

    int size = 50;
    StringBuilder stringBuilder = new StringBuilder(size);

    for (int i = 0; i < size; i++) {
      stringBuilder.append(i);
    }

    CONTENT = stringBuilder.toString();
  }

  public static void testEffect() {
    for (int i = 0; i < 100; i++) {
      LLogger.d(CONTENT);
    }

    ArrayList<String> results = new ArrayList<>();
    for (int i = 10000; i > 0; i = i / 10) {
      long all = runLogTime(i);
      float time = (all / (i * 1.0f));
      results.add("times:" + i + " total:" + all + "ms" + " average:" + time + "ms");
    }

    for (String result : results) {
      LLogger.d(result);
    }
  }


  private static long runLogTime(int times) {
    long start = System.currentTimeMillis();
    for (int i = 0; i < times; i++) {
      LLogger.d(CONTENT);
    }
    long end = System.currentTimeMillis();
    return end - start;
  }

}
