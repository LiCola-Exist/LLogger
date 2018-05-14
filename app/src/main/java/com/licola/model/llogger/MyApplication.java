package com.licola.model.llogger;

import android.app.Application;
import com.licola.llogger.LLogger;

/**
 * Created by LiCola on 2018/5/14.
 */
public class MyApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    LLogger.init(true,"Demo");
  }
}
