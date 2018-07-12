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

  private static final boolean showLog = BuildConfig.DEBUG;

  private static final String TAG = "Demo";

  @Override
  public void onCreate() {
    super.onCreate();

    /**
     * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
     *  输出详细的Bugly SDK的Log；
     *  每一条Crash都会被立即上报；
     *  自定义日志将会在Logcat中输出。
     * 建议在测试阶段建议设置成true，发布时设置为false。
     */
    CrashReport.initCrashReport(getApplicationContext(), "90697d4cad", true);

//    LLogger.init(showLog);//打开log显示
//    LLogger.init(showLog, TAG);//打开log显示 配置Tag

    /**
     * 1：建议log文件存放在项目内部存储中，避免读写外部存储的权限处理
     * 2：建议在cache下指定二级目录 存放log文件 避免cache中文件杂乱
     */
    File logDir = new File(getCacheDir(), LOG_FILE_DIR);
//    LLogger.init(showLog, TAG, logDir);//打开log显示 配置Tag log信息写入本地目录
    LLogger.init(showLog, TAG, logDir, LOG_FILE_PREFIX);//打开log显示 配置tag log信息写入本地目录 并固定log文件后缀

    //开启主线程耗时任务检测
//    LLogger.startMonitor();

  }
}
