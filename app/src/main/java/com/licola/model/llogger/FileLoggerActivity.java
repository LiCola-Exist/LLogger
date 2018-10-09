package com.licola.model.llogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.licola.llogger.LLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class FileLoggerActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_file_logger);

    LLogger.d("进入Log的File文件操作相模块，写入示例日志");
  }

  public void onClickFetchLogListAll(View view) {

    try {
      List<File> logFileAll = LLogger.fetchLogList();
      LLogger.d(logFileAll);
    } catch (FileNotFoundException e) {
      LLogger.e(e);
    }
  }

  public void onClickFetchLogList(View view) {

    try {
      List<File> logFileAll = LLogger.fetchLogList(24);
      LLogger.d(logFileAll);
    } catch (FileNotFoundException e) {
      LLogger.e(e);
    }
  }

  public void onClickZipLogFile(View view) {

    try {
      File logZipFile = LLogger.makeLogZipFile("log.zip");
      LLogger.d("get log zip file:" + logZipFile.getAbsolutePath());
    } catch (IOException e) {
      LLogger.e(e);
    }
  }

  public void onClickZipLogFileWithTime(View view) {

    try {
      File logZipFile = LLogger.makeLogZipFile("log.zip", 24);//当前时间的前几个小时，如果为0表示当前小时
      LLogger.d("get log zip file:" + logZipFile.getAbsolutePath());
    } catch (IOException e) {
      LLogger.e(e);
    }
  }
}
