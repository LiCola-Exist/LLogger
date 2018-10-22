package com.licola.model.llogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.licola.llogger.LLogger;
import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

public class OtherActivity extends AppCompatActivity {

  public static final String LOG_FILE_PREFIX = "LLogger_Other_";
  public static final String LOG_FILE_DIR = "log-files";

  private static final boolean SHOW_LOG = true;

  private static final String TAG = "Demo";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_other);

    /**
     * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
     *  输出详细的Bugly SDK的Log；
     *  每一条Crash都会被立即上报；
     *  自定义日志将会在Logcat中输出。
     * 建议在测试阶段建议设置成true，发布时设置为false。
     */
    CrashReport.initCrashReport(getApplicationContext(), "90697d4cad", true);

    LLogger.init(SHOW_LOG, TAG, new File(getCacheDir(), LOG_FILE_DIR), LOG_FILE_PREFIX);

    //开启主线程耗时任务检测
    LLogger.startMonitor();

    final UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread
        .getDefaultUncaughtExceptionHandler();

    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread t, Throwable e) {
        defaultUncaughtExceptionHandler.uncaughtException(t, e);
      }
    });
  }


  /**
   * 主线程长时间休眠，模拟主线程卡顿 通过LLogger.startMonitor()开启耗时任务检测 一旦发现输出Warn，由Android-UI-Monitor监听器发出
   */
  public void onClickCheckUIMonitor(View view) {

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  public void onClickCheckThrow(View view) {
    throw new IllegalStateException("throw exception");
  }

  public void onClickCheckBugly(View view) {
    BuglyLog.d("bugly-log", "log info");
    ProguardTarget target = new ProguardTarget();
    target.invoke();
  }

  public void onClickCheckEffectRun(View view) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        RunEffect.testEffect();
      }
    }).start();
  }
}
