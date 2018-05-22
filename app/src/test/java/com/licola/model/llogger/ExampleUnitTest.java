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
    LLogger.v("verbose");
    LLogger.d("debug");
  }


}