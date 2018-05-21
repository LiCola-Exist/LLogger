package com.licola.llogger;

import android.util.Log;

/**
 * Created by LiCola on 2018/5/14.
 */
public class AndroidLogger implements Logger{


  private static final boolean ANDROID_LOG_AVAILABLE;

  static {
    boolean android = false;
    try {
      android = Class.forName("android.util.Log") != null;
    } catch (ClassNotFoundException e) {
      //not android environment
    }

    if (android){
      //Test环境 有android的mock
      try {
        Log.i(LLogger.DEFAULT_TAG,"android environment");
      }catch (RuntimeException e){
        android=false;
      }
    }

    ANDROID_LOG_AVAILABLE = android;
  }


  public static boolean isAndroidLogAvailable() {
    return ANDROID_LOG_AVAILABLE;
  }


  @Override
  public void log(int type, String tag, String msg) {
    int index = 0;
    int length = msg.length();
    int countOfSub = length / MAX_LENGTH;

    if (countOfSub > 0) {
      for (int i = 0; i < countOfSub; i++) {
        String sub = msg.substring(index, index + MAX_LENGTH);
        printSub(type, tag, sub);
        index += MAX_LENGTH;
      }
      printSub(type, tag, msg.substring(index, length));
    } else {
      printSub(type, tag, msg);
    }
  }

  private static void printSub(int type, String tag, String sub) {
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
