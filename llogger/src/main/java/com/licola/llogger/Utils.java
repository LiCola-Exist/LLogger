package com.licola.llogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * Created by LiCola on 2018/5/23.
 */
class Utils {

  private static final String TRACE_CLASS_END = "at com.licola.llogger";

  public static void checkNotEmpty(CharSequence str) {
    if (str == null || str.length() == 0){
      throw new IllegalArgumentException("value must be not empty");
    }
  }

  static void checkDirFile(File logFileDir) {
    if (logFileDir == null) {
      throw new NullPointerException("logFileDir == null");
    }

    if (logFileDir.exists() && !logFileDir.isDirectory()) {
      throw new IllegalArgumentException("logFileDir must be directory");
    }
  }

  static void checkAndCreateDir(File logFileDir) throws FileNotFoundException {
    if (logFileDir == null) {
      throw new FileNotFoundException("logFileDir == nul");
    }

    if (!logFileDir.exists()) {
      boolean mkdirs = logFileDir.mkdirs();
      if (!mkdirs) {
        throw new FileNotFoundException("logFileDir mkdirs failed");
      }
    }
  }

  static String getStackTraceString(Throwable tr) {
    if (tr == null) {
      return "";
    }

    Throwable t = tr;
    while (t != null) {
      if (t instanceof UnknownHostException) {
        return "";
      }
      t = t.getCause();
    }

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    tr.printStackTrace(pw);
    pw.flush();
    return sw.toString();
  }

  static String getStackTrace() {

    Throwable throwable = new Throwable();
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    throwable.printStackTrace(printWriter);
    printWriter.flush();
    String message = stringWriter.toString();

    String[] traces = message.split("\\n\\t");
    StringBuilder builder = new StringBuilder();
    builder.append("\n");
    for (String trace : traces) {
      if (trace.contains(TRACE_CLASS_END)) {//跳过 LLogger库的代码层
        continue;
      }
      builder.append("\t").append(trace).append("\n");
    }

    return builder.toString();

  }

}
