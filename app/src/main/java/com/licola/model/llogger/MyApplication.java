package com.licola.model.llogger;

import android.app.Application;
import com.licola.llogger.LLogger;
import com.tencent.bugly.crashreport.CrashReport;
import java.io.File;

/**
 * Created by LiCola on 2018/5/14.
 */
public class MyApplication extends Application {

  public static final String LOG_FILE_PREFIX = "LLogger_";
  public static final String LOG_FILE_DIR = "log-files";

  private static final boolean SHOW_LOG = true;

  private static final String TAG = "Demo";

  @Override
  public void onCreate() {
    super.onCreate();

    /**
     * 打开log显示
     */
//    LLogger.init(SHOW_LOG);

    /**
     * 打开log显示 配置Tag
     */
    LLogger.init(SHOW_LOG, TAG);


    /**
     * 1：建议log文件存放在项目内部存储中，避免读写外部存储的权限处理
     * 2：建议在cache下指定二级目录 存放log文件 避免cache中文件杂乱
     */
    File logDir = new File(getCacheDir(), LOG_FILE_DIR);

    /**
     *打开log显示 配置Tag log信息写入本地目录
     */
//    LLogger.init(SHOW_LOG, TAG, logDir);

    /**
     * 打开log显示 配置tag log信息写入本地目录 并固定log文件前缀
     */
    LLogger.init(SHOW_LOG, TAG, logDir, LOG_FILE_PREFIX);

  }
}
