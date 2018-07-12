package com.licola.model.llogger;

import com.licola.llogger.LLogger;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author LiCola
 * @date 2018/7/12
 */
public class ProguardTarget {

  public void invoke(){
    LLogger.d("ProguardTarget debug");
    LLogger.trace("ProguardTarget tract");
//    CrashReport.testJavaCrash();
  }
}
