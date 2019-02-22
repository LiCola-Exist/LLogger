package com.licola.llogger;

import android.util.Log;

/**
 * Created by LiCola on 2018/5/14. Android环境下的log日志打印工具类 并提供android环境判断
 */
public class AndroidLogger extends Logger {

  public AndroidLogger(String logTag) {
    super(logTag);
  }

  @Override
  public void log(int type, String tag, String sub) {
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
      default:
        Log.v(tag, sub);
        break;
    }
  }

}
