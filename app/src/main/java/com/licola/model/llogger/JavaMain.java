package com.licola.model.llogger;

import com.licola.llogger.LLogger;
import java.io.File;

/**
 * Created by LiCola on 2018/5/22.
 */
public class JavaMain {

  public static void main(String[] args) {

    File file = new File("log");
    if (!file.exists()) {
      file.mkdirs();
    }
    LLogger.init(true, "Java", file);

    LLogger.d();
    new Thread(new Runnable() {
      @Override
      public void run() {
        LLogger.d("thread");
      }
    }).start();

  }
}
