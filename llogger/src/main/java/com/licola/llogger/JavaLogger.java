package com.licola.llogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LiCola on 2018/5/21.
 */
public class JavaLogger implements Logger {

  @Override
  public void log(int type, String tag, String msg) {
    String timePrefix = new SimpleDateFormat("MM-dd HH:mm:ss:SSS", Locale.CHINA)
        .format(new Date(System.currentTimeMillis()));
    String threadName = Thread.currentThread().getName();
    String out = timePrefix +" "+ threadName + " " + mapperType(type) + tag + ": " + msg;
    System.out.println(out);
  }

  private static String mapperType(int type) {
    return "D/";
  }
}
