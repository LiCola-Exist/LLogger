package com.licola.model.llogger;

import com.licola.llogger.LLogger;
import com.licola.llogger.LSupplier;
import java.io.File;
import org.json.JSONException;

/**
 * Created by LiCola on 2018/5/22.
 */
public class JavaMain {


  public static void main(String[] args) throws JSONException {

    File logDir = new File("log");
    LLogger.init(true, "Java", logDir);

    testJavaEnv();

    testSupplier();

//    RunEffect.testEffect();
  }

  private static void testSupplier() {
    LLogger.d(new LSupplier<String>() {
      @Override
      public String get() {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        throw new RuntimeException("假设当尝试使用该内容就会阻塞线程，并抛出异常");
      }
    });
  }

  private static void testJavaEnv() throws JSONException {
    LLogger.v();
    LLogger.d();
    LLogger.d("debug");
    LLogger.trace();

    File logDir = new File("log");
    doThing((Object[]) logDir.list());
  }

  private static void doThing(Object... args) {
    LLogger.d(args.length);
  }


}
