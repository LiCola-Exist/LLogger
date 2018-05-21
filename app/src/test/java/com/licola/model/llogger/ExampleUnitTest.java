package com.licola.model.llogger;

import com.licola.llogger.LLogger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

  @Test
  public void LLoggerTest() {
    System.out.println("05-21 14:54:58.039 17473-17473 D/Demo: [ (ExampleUnitTest.java:17)#lloggerTest ] ");
    System.out.println("05-21 14:54:58.039 17473-17473 D/Demo: [ (ExampleUnitTest.java:18)#lloggerTest ] debug");
//    System.out.println("05-21 18:43:30:296 main D/LLogger:   [ (ExampleUnitTest.java:19)#lloggerTest ] test java");
    LLogger.d("debug");
  }


}