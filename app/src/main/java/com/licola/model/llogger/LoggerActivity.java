package com.licola.model.llogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.licola.llogger.LLogger;
import com.licola.llogger.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoggerActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_logger);

  }

  public void onClickLogV(View view) {
    LLogger.v();
    LLogger.v("verbose");
    LLogger.v("verbose", view);
  }

  public void onClickLogD(View view) {
    LLogger.d();
    LLogger.d("debug");
    LLogger.d("debug", view);
  }

  public void onClickLogI(View view) {
    LLogger.i();
    LLogger.i("info");
    LLogger.i("info", view);
  }

  public void onClickLogW(View view) {
    LLogger.w();
    LLogger.w("warn");
    LLogger.w("warn", view);
  }

  public void onClickLogE(View view) {
    LLogger.e();
    LLogger.e("error");
    try {
      throw new RuntimeException("故意抛出的异常");
    } catch (Throwable throwable) {
      LLogger.e(throwable);
    }
  }

  public void onClickLogA(View view) {
    LLogger.a();
    LLogger.a("assert");
    LLogger.a("assert", view);
  }

  public void onClickLogJson(View view) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("key1", "value1");
      jsonObject.put("key2", "value2");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    LLogger.json(jsonObject);

    JSONArray jsonArray = new JSONArray();
    try {
      jsonArray.put(0, "index1");
      jsonArray.put(1, "index2");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    LLogger.json(jsonArray);

  }

  public void onClickLogTrace(View view) {
    LLogger.trace();
    ProguardTarget target = new ProguardTarget();
    target.invoke();
  }

  public void onClickLogInnerClass(View view) {
    MyRunnable myRunnable = new MyRunnable();
    Thread newThread = new Thread(myRunnable);
    newThread.start();
  }

  /**
   * 创建一个新的日志实例，用于某些特定的组件日志打印，可以另外指定日志tag和日志文件输出
   */
  public void onClickInstanceOther(View view) throws JSONException {
    Logger logger = LLogger.create(false, "Other", new File(getCacheDir(), "other"));
    try {
      File otherLogZip = logger.makeLogZipFile("other_log");

      logger.printLog(LLogger.I, otherLogZip);
    } catch (IOException e) {
      logger.printLog(LLogger.E, e);
    }

    logger.printLog(LLogger.V);
    logger.printLog(LLogger.D);
    logger.printJson(new JSONObject().put("key", "value"));
    logger.printTrace();

    try {
      List<File> files = logger.fetchLogList();//获取全部日志

      for (File file : files) {
        logger.printLog(LLogger.I, file);
      }

    } catch (FileNotFoundException e) {
      logger.printLog(LLogger.E, e);
    }

  }

  public void onClickLogLongText(View view) {

    String subText = "long-text-1234567890";
    StringBuilder longText = new StringBuilder();
    for (int i = 0; i < 500; i++) {
      longText.append(subText);
    }

    LLogger.d(longText.toString());
  }

  class MyRunnable implements Runnable {

    @Override
    public void run() {
      LLogger.d("inner class");
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          LLogger.d("inner-inner class");
        }
      });
    }
  }
}
