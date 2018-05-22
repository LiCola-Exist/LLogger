package com.licola.llogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LiCola on 2018/5/21.
 * Java环境下的log日志打印工具类
 */
public class JavaLogger extends Logger {

  @Override
  public void logType(int type, String tag, String msg) {
    String timePrefix = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.CHINA)
        .format(new Date(System.currentTimeMillis()));
    String threadName = Thread.currentThread().getName();
    String out = timePrefix + " " + threadName + " " + mapperType(type) + "/" + tag + ": " + msg;
    System.out.println(out);
  }

  private static String mapperType(int type) {
    String typeStr;
    switch (type) {
      case LLogger.V:
        typeStr = "Verbose";
        break;
      case LLogger.D:
        typeStr = "Debug";
        break;
      case LLogger.I:
        typeStr = "Info";
        break;
      case LLogger.W:
        typeStr = "Warn";
        break;
      case LLogger.E:
        typeStr = "Error";
        break;
      case LLogger.A:
        typeStr = "Assert";
        break;
      default:
        typeStr = "Unknown";
    }
    return typeStr;
  }
}
