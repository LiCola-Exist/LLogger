package com.licola.llogger;

import android.util.Log;

/**
 * Created by LiCola on 2018/5/14. Android环境下的log日志打印工具类 并提供android环境判断
 */
public class AndroidLogger extends PlatformLogger {

   AndroidLogger(String logTag) {
    super(logTag);
  }

  @Override
  public void log(int type, String tag, String sub) {
    switch (type) {
      case Logger.V:
        Log.v(tag, sub);
        break;
      case Logger.D:
        Log.d(tag, sub);
        break;
      case Logger.I:
        Log.i(tag, sub);
        break;
      case Logger.W:
        Log.w(tag, sub);
        break;
      case Logger.E:
        Log.e(tag, sub);
        break;
      case Logger.A:
        Log.wtf(tag, sub);
        break;
      default:
        Log.v(tag, sub);
        break;
    }
  }

}
