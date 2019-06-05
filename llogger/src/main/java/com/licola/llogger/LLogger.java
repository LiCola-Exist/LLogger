package com.licola.llogger;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Log日志工具类
 *
 * 基于：https://github.com/ZhaoKaiQiang/KLog项目改造
 */
public final class LLogger implements Logger {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  private static final String DEFAULT_MESSAGE = "execute";
  private static final Object[] DEFAULT_MESSAGES = new Object[]{DEFAULT_MESSAGE};
  private static final String ARGUMENTS = "argument";
  private static final String NULL = "null";

  private static final String SUFFIX_JAVA = ".java";
  private static final String ANONYMITY_JAVA_FLAG = "$";

  static final String DEFAULT_TAG = "LLogger";
  private static final boolean DEFAULT_SHOW_LINE = true;

  private static final int JSON_INDENT = 4;

  private static Logger LOGGER;

  private final boolean showLine;
  private final PlatformLogger platformLogger;
  private final FileLogger fileLogger;

  LLogger(boolean showLine, PlatformLogger platformLogger, FileLogger fileLogger) {
    this.showLine = showLine;
    this.platformLogger = platformLogger;
    this.fileLogger = fileLogger;
  }

  public static Logger create() {
    return new LLogger(DEFAULT_SHOW_LINE, PlatformLogger.findPlatform(DEFAULT_TAG), null);
  }

  public static Logger create(boolean showLine) {
    return new LLogger(showLine, PlatformLogger.findPlatform(DEFAULT_TAG), null);
  }

  public static Logger create(boolean showLine, String tag) {
    return new LLogger(showLine, PlatformLogger.findPlatform(tag), null);
  }

  public static Logger create(boolean showLine, String tag, File logFileDir) {
    return new LLogger(showLine, PlatformLogger.findPlatform(tag), new FileLogger(tag, logFileDir));
  }

  public static void init() {
    LOGGER = create();
  }

  /**
   * 配置方法
   */
  public static void init(boolean showLine) {
    LOGGER = create(showLine);
  }

  /**
   * 配置方法
   *
   * @param tag log的tag显示
   */
  public static void init(boolean showLine, String tag) {
    LOGGER = create(showLine, tag);
  }

  /**
   * 配置方法，会在当前类的加载日志对象，如果不显示调起就不会输出日志
   *
   * @param tag log的tag显示
   * @param logFileDir log
   */
  public static void init(boolean showLine, String tag, File logFileDir) {
    LOGGER = create(showLine, tag, logFileDir);
  }

  public static void v() {
    if (LOGGER != null) {
      LOGGER.printLog(V);
    }
  }

  public static void v(Object msg) {
    if (LOGGER != null) {
      LOGGER.printLog(V, msg);
    }
  }

  public static void v(LSupplier<String> supplier) {
    if (LOGGER != null) {
      LOGGER.printLog(V, supplier.get());
    }
  }

  public static void v(Object... objects) {
    if (LOGGER != null) {
      LOGGER.printLog(V, objects);
    }
  }

  public static void d() {
    if (LOGGER != null) {
      LOGGER.printLog(D);
    }
  }

  public static void d(Object msg) {
    if (LOGGER != null) {
      LOGGER.printLog(D, msg);
    }
  }

  public static void d(LSupplier<String> supplier) {
    if (LOGGER != null) {
      LOGGER.printLog(D, supplier.get());
    }
  }

  public static void d(Object... objects) {
    if (LOGGER != null) {
      LOGGER.printLog(D, objects);
    }
  }

  public static void i() {
    if (LOGGER != null) {
      LOGGER.printLog(I);
    }
  }

  public static void i(Object msg) {
    if (LOGGER != null) {
      LOGGER.printLog(I, msg);
    }
  }

  public static void i(LSupplier<String> supplier) {
    if (LOGGER != null) {
      LOGGER.printLog(I, supplier.get());
    }
  }

  public static void i(Object... objects) {
    if (LOGGER != null) {
      LOGGER.printLog(I, objects);
    }
  }

  public static void w() {
    if (LOGGER != null) {
      LOGGER.printLog(W);
    }
  }

  public static void w(Object msg) {
    if (LOGGER != null) {
      LOGGER.printLog(W, msg);
    }
  }

  public static void w(LSupplier<String> supplier) {
    if (LOGGER != null) {
      LOGGER.printLog(W, supplier.get());
    }
  }

  public static void w(Object... objects) {
    if (LOGGER != null) {
      LOGGER.printLog(W, objects);
    }
  }

  public static void e() {
    if (LOGGER != null) {
      LOGGER.printLog(E);
    }
  }

  public static void e(Object msg) {
    if (LOGGER != null) {
      LOGGER.printLog(E, msg);
    }
  }

  public static void e(LSupplier<String> supplier) {
    if (LOGGER != null) {
      LOGGER.printLog(E, supplier.get());
    }
  }

  public static void e(Throwable throwable) {
    if (LOGGER != null) {
      LOGGER.printLog(E, throwable);
    }
  }

  public static void e(Object... objects) {
    if (LOGGER != null) {
      LOGGER.printLog(E, objects);
    }
  }

  public static void a() {
    if (LOGGER != null) {
      LOGGER.printLog(A);
    }
  }

  public static void a(Object msg) {
    if (LOGGER != null) {
      LOGGER.printLog(A, msg);
    }
  }

  public static void a(LSupplier<String> supplier) {
    if (LOGGER != null) {
      LOGGER.printLog(A, supplier.get());
    }
  }

  public static void a(Object... objects) {
    if (LOGGER != null) {
      LOGGER.printLog(A, objects);
    }
  }

  public static void trace() {
    if (LOGGER != null) {
      LOGGER.printTrace();
    }
  }

  public static void trace(String msg) {
    if (LOGGER != null) {
      LOGGER.printTrace(msg);
    }
  }

  public static void json(JSONObject jsonObject) {
    if (LOGGER != null) {
      LOGGER.printJson(jsonObject);
    }
  }

  public static void jsonObject(LSupplier<JSONObject> supplier) {
    if (LOGGER != null) {
      LOGGER.printJson(supplier.get());
    }
  }

  public static void json(JSONArray jsonArray) {
    if (LOGGER != null) {
      LOGGER.printJson(jsonArray);
    }
  }

  public static void jsonArray(LSupplier<JSONArray> supplier) {
    if (LOGGER != null) {
      LOGGER.printJson(supplier.get());
    }
  }

  @Override
  public void printLog(int type) {
    printLog(type, DEFAULT_MESSAGES);
  }

  @Override
  public void printLog(int type, Object object) {
    printLog(type, new Object[]{object});
  }

  @Override
  public void printLog(int type, Throwable throwable) {
    printLog(type, Utils.getStackTraceString(throwable));
  }

  @Override
  public void printLog(int type, Object... objects) {
    String message;

    if (showLine) {
      String headString = wrapperContent();
      message = headString + ((objects == null) ? NULL : getObjectsString(objects));
    } else {
      message = (objects == null) ? NULL : getObjectsString(objects);
    }

    platformLogger.log(type, message);

    if (fileLogger != null) {
      try {
        String fileLogPath = fileLogger.printLog(type, message);
        if (fileLogPath != null) {
          platformLogger.log(I, "create log file " + fileLogPath);
        }
      } catch (IOException e) {
        platformLogger.log(E, Utils.getStackTraceString(e));
      }
    }
  }

  @Override
  public void printJson(JSONObject object) {
    int type = I;
    String msg;
    try {
      msg = object.toString(JSON_INDENT);
    } catch (JSONException e) {
      type = E;
      msg = Utils.getStackTraceString(e);
    }

    printLog(type, "JSONObject" + LINE_SEPARATOR + msg);
  }

  @Override
  public void printJson(JSONArray object) {
    int type = I;
    String msg;
    try {
      msg = object.toString(JSON_INDENT);
    } catch (JSONException e) {
      type = E;
      msg = Utils.getStackTraceString(e);
    }

    printLog(type, "JSONArray" + LINE_SEPARATOR + msg);
  }

  @Override
  public void printTrace() {
    String headString = wrapperContent();
    printLog(D, headString + Utils.getStackTrace());
  }

  @Override
  public void printTrace(String msg) {
    String headString = wrapperContent();
    printLog(D, headString + msg + Utils.getStackTrace());
  }

  /**
   * @see #fetchLogList()
   */
  public static List<File> logList() throws FileNotFoundException {
    if (LOGGER != null) {
      return LOGGER.fetchLogList();
    }

    return Collections.emptyList();
  }

  /**
   * @see #fetchLogList(int)
   */
  public static List<File> logList(int lastHour) throws FileNotFoundException {
    if (LOGGER != null) {
      return LOGGER.fetchLogList(lastHour);
    }

    return Collections.emptyList();
  }

  /**
   * @see #fetchLogList(long)
   */
  public static List<File> logList(long beginTime) throws FileNotFoundException {
    if (LOGGER != null) {
      return LOGGER.fetchLogList(beginTime);
    }

    return Collections.emptyList();
  }

  /**
   * @see #makeLogZipFile(String)
   */
  public static File logZipFile(String zipFileName)
      throws IOException {
    if (LOGGER != null) {
      return LOGGER.makeLogZipFile(zipFileName);
    }
    return null;
  }


  /**
   * @see #makeLogZipFile(String, int)
   */
  public static File logZipFile(String zipFileName, int lastHour)
      throws IOException {
    if (LOGGER != null) {
      return LOGGER.makeLogZipFile(zipFileName, lastHour);
    }

    return null;
  }

  /**
   * @see #makeLogZipFile(String, long)
   */
  public static File logZipFile(String zipFileName, long beginTime)
      throws IOException {
    if (LOGGER != null) {
      return LOGGER.makeLogZipFile(zipFileName, beginTime);
    }

    return null;
  }


  @Override
  public List<File> fetchLogList() throws FileNotFoundException {
    if (fileLogger != null) {
      return fileLogger.fetchLogFiles();
    }

    return Collections.emptyList();
  }


  @Override
  public List<File> fetchLogList(int lastHour) throws FileNotFoundException {
    if (fileLogger != null) {
      return fileLogger.fetchLogFiles(lastHour);
    }

    return Collections.emptyList();
  }


  @Override
  public List<File> fetchLogList(long beginTime) throws FileNotFoundException {
    if (fileLogger != null) {
      return fileLogger.fetchLogFiles(beginTime);
    }
    return Collections.emptyList();
  }


  @Override
  public File makeLogZipFile(String zipFileName) throws IOException {

    if (fileLogger != null) {
      return fileLogger.makeZipFile(zipFileName);
    }

    return null;
  }


  @Override
  public File makeLogZipFile(String zipFileName, int lastHour) throws IOException {

    if (fileLogger != null) {
      return fileLogger.makeZipFile(zipFileName, lastHour);
    }

    return null;
  }


  @Override
  public File makeLogZipFile(String zipFileName, long beginTime) throws IOException {

    if (fileLogger != null) {
      return fileLogger.makeZipFile(zipFileName, beginTime);
    }

    return null;
  }


  private static String wrapperContent() {

    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

    int stackTraceIndex = Utils.findInvokeStackIndex(stackTraceElements);

    StackTraceElement targetElement = stackTraceElements[stackTraceIndex];
    String classFileName = targetElement.getFileName();
    String className = targetElement.getClassName();

    String[] classNameInfo = className.split("\\.");
    if (classNameInfo.length > 0) {
      className = classNameInfo[classNameInfo.length - 1] + SUFFIX_JAVA;
    }

    String innerClassName = null;
    if (!className.equals(classFileName) && className.contains(ANONYMITY_JAVA_FLAG)) {
      //内部类
      int index = className.indexOf(ANONYMITY_JAVA_FLAG);
      innerClassName = className.substring(index);
    }

    String methodName = targetElement.getMethodName();
    int lineNumber = targetElement.getLineNumber();

    if (lineNumber < 0) {
      lineNumber = 0;
    }

    return "[ ("
        + classFileName
        + ':'
        + lineNumber
        + ')'
        + (innerClassName == null ? "#" : innerClassName + "#")
        + methodName
        + " ] ";
  }


  private static String getObjectsString(Object[] objects) {

    if (objects.length > 1) {
      StringBuilder builder = new StringBuilder();
      builder.append("\n");
      for (int i = 0, length = objects.length; i < length; i++) {
        Object object = objects[i];
        builder.append("\t").append(ARGUMENTS).append("[").append(i).append("]").append("=");
        if (object == null) {
          builder.append(NULL);
        } else {
          builder.append(object.toString());
        }
        if (i != length - 1) {
          builder.append("\n");
        }
      }
      return builder.toString();
    } else {
      Object object = objects[0];
      return object == null ? NULL : object.toString();
    }
  }

  static String mapperType(int type) {
    String typeStr;
    switch (type) {
      case V:
        typeStr = "Verbose";
        break;
      case D:
        typeStr = "Debug";
        break;
      case I:
        typeStr = "Info";
        break;
      case W:
        typeStr = "Warn";
        break;
      case E:
        typeStr = "Error";
        break;
      case A:
        typeStr = "Assert";
        break;
      default:
        typeStr = "Unknown";
    }
    return typeStr;
  }

}
