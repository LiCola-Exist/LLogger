package com.licola.llogger;

/**
 * Created by LiCola on 2018/5/21.
 */
abstract class Logger {

  private static final int MAX_LENGTH = 4000;

  public void log(int type, String tag, String msg) {
    int index = 0;
    int length = msg.length();
    int countOfSub = length / MAX_LENGTH;

    if (countOfSub > 0) {
      for (int i = 0; i < countOfSub; i++) {
        String sub = msg.substring(index, index + MAX_LENGTH);
        logType(type, tag, sub);
        index += MAX_LENGTH;
      }
      logType(type, tag, msg.substring(index, length));
    } else {
      logType(type, tag, msg);
    }
  }

  abstract void logType(int type, String tag, String msg);
}
