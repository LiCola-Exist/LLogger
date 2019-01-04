package com.licola.llogger;

import static com.licola.llogger.LLogger.mapperType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LiCola on 2018/5/21. Java环境下的log日志打印工具类
 */
class JavaLogger extends Logger {

  private static final ThreadLocal<SimpleDateFormat> FORMAT_INFO = new ThreadLocal<SimpleDateFormat>() {
    @Override
    protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.CHINA);
    }
  };

  @Override
  public void logType(int type, String tag, String msg) {
    String timePrefix = FORMAT_INFO.get().format(new Date(System.currentTimeMillis()));
    String threadName = Thread.currentThread().getName();
    String out = timePrefix + " " + threadName + " " + mapperType(type) + "/" + tag + ": " + msg;
    System.out.println(out);
  }

  @Override
  void startMonitor(long timeOut) {
    throw new UnsupportedOperationException("Java 环境不支持UI线程检测");
  }

  @Override
  void stopMonitor() {
    throw new UnsupportedOperationException("Java 环境不支持UI线程检测");
  }

}
