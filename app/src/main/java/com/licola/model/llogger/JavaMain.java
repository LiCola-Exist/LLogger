package com.licola.model.llogger;

import com.licola.llogger.LLogger;
import java.io.File;
import org.json.JSONException;

/**
 * Created by LiCola on 2018/5/22.
 */
public class JavaMain {

  public static void main(String[] args){

    LLogger.init(true, "Java", new File("log"));
    LLogger.v();
    LLogger.d();
    LLogger.d("debug");
    LLogger.trace();
  }
}
