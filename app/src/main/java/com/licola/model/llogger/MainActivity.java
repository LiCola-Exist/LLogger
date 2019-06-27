package com.licola.model.llogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }


  public void onClickLogOperate(View view) {
    startActivity(new Intent(this, LoggerActivity.class));
  }

  public void onClickLogFileOperate(View view) {
    startActivity(new Intent(this, FileLoggerActivity.class));
  }

  public void onClickLogOtherOperate(View view) {
    startActivity(new Intent(this, OtherActivity.class));
  }


}
