package com.licola.model.llogger;

import android.app.Application;
import com.licola.llogger.LLogger;
import java.io.File;

/**
 * Created by LiCola on 2018/5/14.
 */
public class MyApplication extends Application {

  public static final String LOG_FILE_PREFIX = "llogger_";
  public static final String LOG_FILE_DIR = "log-file";

  @Override
  public void onCreate() {
    super.onCreate();
    File logDir = new File(getCacheDir(), LOG_FILE_DIR);
    if (!logDir.exists()) {
      logDir.mkdir();
    }
    LLogger.init(true, "Demo", logDir, LOG_FILE_PREFIX);
  }
}
