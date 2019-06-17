package com.licola.model.llogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.licola.llogger.LLogger;
import com.licola.llogger.LSupplier;
import com.licola.llogger.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
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
    LLogger.v(new LSupplier<String>() {
      @Override
      public String get() {
        return "懒求值verbose";
      }
    });
    LLogger.v(() -> "懒求值verbose Java8写法");
  }

  public void onClickLogD(View view) {
    LLogger.d();
    LLogger.d("debug");
    LLogger.d("debug", view);
    LLogger.d(() -> "懒求值debug");
  }

  public void onClickLogI(View view) {
    LLogger.i();
    LLogger.i("info");
    LLogger.i("info", view);
    LLogger.i(() -> "懒求值info");
  }

  public void onClickLogW(View view) {
    LLogger.w();
    LLogger.w("warn");
    LLogger.w("warn", view);
    LLogger.i(() -> "懒求值warn");
  }

  public void onClickLogE(View view) {
    LLogger.e();
    LLogger.e("error");
    LLogger.e(() -> "懒求值error");
    try {
      throw new RuntimeException("故意抛出的异常打印线程栈信息");
    } catch (Throwable throwable) {
      LLogger.e(throwable);
    }
  }

  public void onClickLogA(View view) {
    LLogger.a();
    LLogger.a("assert");
    LLogger.a("assert", view);
    LLogger.a(() -> "懒求值assert");
  }

  public void onClickLogJson(View view) throws JSONException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("key1", "value1");
    LLogger.json(jsonObject);
    LLogger.d(jsonObject);

    JSONArray jsonArray = new JSONArray();
    jsonArray.put(0, "index1");
    LLogger.json(jsonArray);
    LLogger.i(jsonArray);
  }

  public void onClickLogMoreType(View view) {

    LLogger.d(Arrays.asList("list-1", "list-2", "list-3"));

    LLogger.d(new int[]{1, 2, 3});
    LLogger.d(new long[]{100, 200, 300});
    LLogger.d(new boolean[]{false, true});
    LLogger.d(new char[]{'a', 'b', 'c'});
    LLogger.d(new String[]{"string1", "string2"});

    Map<String, Integer> maps = new HashMap<>();
    maps.put("key1", 100);
    maps.put("key2", 200);
    LLogger.d(maps);

    Set<String> sets = new TreeSet<>();
    sets.add("b");
    sets.add("a");
    LLogger.d(sets);

    Queue<String> queues = new ArrayDeque<>();
    queues.add("first");
    queues.add("last");
    LLogger.d(queues);
  }

  public void onClickLogTrace(View view) {
    LLogger.trace();
    ProguardTarget target = new ProguardTarget();
    target.invoke();
  }

  public void onClickLogInnerClass(View view) {
    MyRunnable myRunnable = new MyRunnable();
    new Thread(myRunnable).start();
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
    logger.printLog(Logger.I, new JSONObject().put("key1", "value1"));
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

      runOnUiThread(() -> LLogger.d("inner-inner Lambda"));
    }
  }
}
