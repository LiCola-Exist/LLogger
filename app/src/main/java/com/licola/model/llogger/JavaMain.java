package com.licola.model.llogger;

import com.licola.llogger.LLogger;
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

//    RunEffect.testEffect();
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
