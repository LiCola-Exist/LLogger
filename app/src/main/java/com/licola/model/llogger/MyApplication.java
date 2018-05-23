package com.licola.model.llogger;

import android.app.Application;
import com.licola.llogger.LLogger;
import java.io.File;

/**
 * Created by LiCola on 2018/5/14.
 */
public class MyApplication extends Application {

  public static final String LOG_FILE_PREFIX = "LLogger_";
  public static final String LOG_FILE_DIR = "log-file";

  private static final boolean showLog = BuildConfig.DEBUG;

  private static final String TAG = "Demo";

  @Override
  public void onCreate() {
    super.onCreate();
//    LLogger.init(showLog);//打开log显示
//    LLogger.init(showLog, TAG);//打开log显示 配置Tag

    //建议在cache下指定二级目录 存放log文件 避免cache中文件杂乱
    File logDir = new File(getCacheDir(), LOG_FILE_DIR);
//    LLogger.init(showLog, TAG, logDir);//打开log显示 配置Tag log信息写入本地目录
    LLogger.init(showLog, TAG, logDir, LOG_FILE_PREFIX);//打开log显示 配置tag log信息写入本地目录 并固定log文件后缀
  }
}
