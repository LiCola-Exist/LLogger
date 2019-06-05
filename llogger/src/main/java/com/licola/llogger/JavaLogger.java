package com.licola.llogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LiCola on 2018/5/21. Java环境下的log日志打印工具类
 */
class JavaLogger extends PlatformLogger {

  private static final ThreadLocal<SimpleDateFormat> FORMAT_INFO = new ThreadLocal<SimpleDateFormat>() {
    @Override
    protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.CHINA);
    }
  };

  JavaLogger(String logTag) {
    super(logTag);
  }

  @Override
  public void log(int type, String tag, String msg) {
    String timePrefix = FORMAT_INFO.get().format(new Date(System.currentTimeMillis()));
    String threadName = Thread.currentThread().getName();
    String out =
        timePrefix + " " + threadName + " " + LLogger.mapperType(type) + "/" + tag + ": " + msg;
    System.out.println(out);
  }

}
