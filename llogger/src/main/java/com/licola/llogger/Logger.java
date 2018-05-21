package com.licola.llogger;

/**
 * Created by LiCola on 2018/5/21.
 */
interface Logger {
  int MAX_LENGTH = 4000;

  void log(int type, String tag, String msg);
}
