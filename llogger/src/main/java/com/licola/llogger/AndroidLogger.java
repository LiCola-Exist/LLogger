package com.licola.llogger;

import android.util.Log;

/**
 * Created by LiCola on 2018/5/14.
 * Android环境下的log日志打印工具类
 * 并提供android环境判断
 */
public class AndroidLogger extends Logger {


  private static final boolean ANDROID_LOG_AVAILABLE;

  static {
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

    ANDROID_LOG_AVAILABLE = android;
  }


  public static boolean isAndroidLogAvailable() {
    return ANDROID_LOG_AVAILABLE;
  }

  public void logType(int type, String tag, String sub) {
    switch (type) {
      case LLogger.V:
        Log.v(tag, sub);
        break;
      case LLogger.D:
        Log.d(tag, sub);
        break;
      case LLogger.I:
        Log.i(tag, sub);
        break;
      case LLogger.W:
        Log.w(tag, sub);
        break;
      case LLogger.E:
        Log.e(tag, sub);
        break;
      case LLogger.A:
        Log.wtf(tag, sub);
        break;
    }
  }


}
