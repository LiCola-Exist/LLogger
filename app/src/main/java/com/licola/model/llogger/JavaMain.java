package com.licola.model.llogger;

import com.licola.llogger.LLogger;
import java.io.File;
import org.json.JSONException;

/**
 * Created by LiCola on 2018/5/22.
 */
public class JavaMain {

  public static void main(String[] args) throws JSONException {

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
