package com.licola.model.llogger;

import com.licola.llogger.LLogger;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

  @Before
  public void setUp(){
    LLogger.init();
  }

  @Test
  public void LLoggerTest() throws JSONException {
    LLogger.v("verbose");
    LLogger.d("debug");
    JSONObject jsonObject=new JSONObject();
    jsonObject.put("key","value");
    jsonObject.put("key1","value1");
    LLogger.json(jsonObject);
  }

  @Test
  public void traceTest(){
    LLogger.trace("throw error");
  }

}