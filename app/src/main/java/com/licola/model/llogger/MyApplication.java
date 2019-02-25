package com.licola.model.llogger;

import android.app.Application;
import com.licola.llogger.LLogger;
import java.io.File;

/**
 * Created by LiCola on 2018/5/14.
 */
public class MyApplication extends Application {

  public static final String LOG_FILE_DIR = "log-files";

  public static final String TAG = "Demo";

  @Override
  public void onCreate() {
    super.onCreate();

    /**
     * 空参初始化：使用默认tag，默认打印行号，不写本地日志文件。
     */
//    LLogger.init();

    /**
     * 初始化：显示行号，配置tag，不写本地日志文件
     */
//    LLogger.init(true,TAG);

    /**
     * 1：建议log文件存放在项目内部存储中，避免读写外部存储的权限处理
     * 2：建议在cache下指定二级目录 存放log文件 避免cache中文件杂乱
     */
    File logDir = new File(getCacheDir(), LOG_FILE_DIR);

    /**
     * 初始化：显示行号，配置tag，log信息写入本地目录
     */
    LLogger.init(true, TAG, logDir);

  }
}
