package com.licola.model.llogger;

import com.licola.llogger.LLogger;
import java.io.File;

/**
 * Created by LiCola on 2018/5/22.
 */
public class JavaMain {


  public static void main(String[] args) {

    File logDir = new File("log");
    LLogger.init(true, "Java", logDir);

    testJavaEnv();

    RunEffect.testEffect();
  }

  private static void testJavaEnv() {
    LLogger.v();
    LLogger.d();
    LLogger.d("debug");
    LLogger.trace();
  }


}
