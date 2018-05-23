package com.licola.llogger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * Created by LiCola on 2018/5/23.
 */
public class StackTraceUtils {

  private static final String TRACE_CLASS_END = "at com.licola.llogger";

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
      builder.append(trace).append("\n");
    }

    return builder.toString();

  }

}
