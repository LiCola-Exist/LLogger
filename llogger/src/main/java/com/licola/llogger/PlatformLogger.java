package com.licola.llogger;

import android.util.Log;

/**
 * Created by LiCola on 2018/5/21. 抽象的日志打印工具
 */
abstract class PlatformLogger {

  private static final int MAX_LENGTH = 4000;

  private final String logTag;

  PlatformLogger(String logTag) {
    this.logTag = logTag;
  }

  /**
   * 抽象的日志输出方法
   *
   * @param type 日志类型
   * @param tag tag
   * @param msg 日志消息
   */
  public abstract void log(int type, String tag, String msg);

  static PlatformLogger findPlatform(String logTag) {
    boolean android = false;
    try {
      android = Class.forName("android.util.Log") != null;
    } catch (ClassNotFoundException e) {
      //java environment
    }

    if (android) {
      //Test环境 有android的mock
      try {
        android = Log.i(LLogger.DEFAULT_TAG, "LLogger in android environment") > 0;
      } catch (RuntimeException e) {
        android = false;
      }
    }

    if (android) {
      return new AndroidLogger(logTag);
    } else {
      return new JavaLogger(logTag);
    }
  }

  public void log(int type, String msg) {
    int index = 0;
    int length = msg.length();
    int countOfSub = length / MAX_LENGTH;

    if (countOfSub > 0) {
      //超长msg检测 处理
      for (int i = 0; i < countOfSub; i++) {
        String sub = msg.substring(index, index + MAX_LENGTH);
        log(type, logTag, sub);
        index += MAX_LENGTH;
      }
      //除不尽的 打印剩余长度
      log(type, logTag, msg.substring(index, length));
    } else {
      log(type, logTag, msg);
    }
  }


}
