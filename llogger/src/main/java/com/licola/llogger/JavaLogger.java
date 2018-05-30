package com.licola.llogger;

import static com.licola.llogger.LLogger.mapperType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LiCola on 2018/5/21.
 * Java环境下的log日志打印工具类
 */
public class JavaLogger extends Logger {

  private static final String DATE_FORMAT_LOG_INFO = "MM-dd HH:mm:ss.SSS";

  @Override
  public void logType(int type, String tag, String msg) {
    String timePrefix = new SimpleDateFormat(DATE_FORMAT_LOG_INFO, Locale.CHINA)
        .format(new Date(System.currentTimeMillis()));
    String threadName = Thread.currentThread().getName();
    String out = timePrefix + " " + threadName + " " + mapperType(type) + "/" + tag + ": " + msg;
    System.out.println(out);
  }


}
