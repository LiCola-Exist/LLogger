package com.licola.llogger;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Printer;

/**
 * Created by LiCola on 2018/6/27.
 */
class AndroidUIMonitor {

  public static final long TIME_OUT_LIMIT = 1000L;

  private static final String MSG_RUN_START = ">>>>> Dispatching";
  private static final String MSG_RUN_END = "<<<<< Finished";

  private static final String NAME = "Android-UI-Monitor";

  private AndroidLogger androidLogger;
  private Looper mTargetLooper;

  private Handler mHandler;
  private long timeOut;

  AndroidUIMonitor(AndroidLogger androidLogger, Looper looper, long timeOut) {
    HandlerThread mHandlerThread = new HandlerThread(NAME);
    mHandlerThread.start();
    mHandler = new Handler(mHandlerThread.getLooper());
    this.androidLogger = androidLogger;
    this.mTargetLooper = looper;
    this.timeOut = timeOut;
  }

  private Runnable mTimeOutRunnable = new Runnable() {
    @Override
    public void run() {
      StackTraceElement[] stackTrace = mTargetLooper.getThread().getStackTrace();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("ui thread execute the task over time:")
          .append(timeOut)
          .append("ms")
          .append(" \n");
      for (StackTraceElement s : stackTrace) {
        stringBuilder.append(s.toString()).append("\n");
      }
      androidLogger.logType(LLogger.W, NAME, stringBuilder.toString());
    }
  };

  private void startMonitor() {
    mHandler.postDelayed(mTimeOutRunnable, timeOut);
  }

  private void removeMonitor() {
    mHandler.removeCallbacks(mTimeOutRunnable);
  }

  public void start() {

    mTargetLooper.setMessageLogging(new Printer() {
      @Override
      public void println(String x) {
        if (x.startsWith(MSG_RUN_START)) {
          startMonitor();
        }

        if (x.startsWith(MSG_RUN_END)) {
          removeMonitor();
        }
      }
    });
  }

  public void stop() {
    removeMonitor();
    mTargetLooper.setMessageLogging(null);
  }

}
