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
 * Log日志工具类 1:支持打印行号、方法、内部类名 2:支持在Logcat中的点击行号跳转代码 3:支持空参，单一参数，多参数打印
 * 4:支持log日志信息写入本地文件,以时间为节点，避免日志过长，且支持打包log文件 5:支持Java环境log打印，如在android的test本地单元测试中打印
 * 6:支持JSON字符串、JSON对象、JSON数组友好格式化打印 7:支持超长4000+字符串长度打印
 *
 * 基于：https://github.com/ZhaoKaiQiang/KLog项目改造
 */
public final class LLogger {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  private static final String DEFAULT_MESSAGE = "execute";
  private static final String ARGUMENTS = "argument";
  private static final String NULL = "null";

  private static final String SUFFIX_JAVA = ".java";
  private static final String ANONYMITY_JAVA_FLAG = "$";

  static final String DEFAULT_TAG = "LLogger";
  private static final boolean DEFAULT_SHOW_LINE = true;

  private static final long FETCH_ALL_LOG = 0;
  private static final long HOUR_TIME = 60 * 60 * 1000;

  private static final int JSON_INDENT = 4;

  public static final int V = 0x1;
  public static final int D = 0x2;
  public static final int I = 0x3;
  public static final int W = 0x4;
  public static final int E = 0x5;
  public static final int A = 0x6;

  private static LLogger LLOGGER;

  private final boolean showLine;
  private final Logger logger;
  private final FileLog fileLog;

  public LLogger(boolean showLine, Logger logger, FileLog fileLog) {
    this.showLine = showLine;
    this.logger = logger;
    this.fileLog = fileLog;
  }

  public static LLogger create() {
    return new LLogger(DEFAULT_SHOW_LINE, Logger.findPlatform(DEFAULT_TAG), null);
  }

  public static LLogger create(boolean showLine) {
    return new LLogger(showLine, Logger.findPlatform(DEFAULT_TAG), null);
  }

  public static LLogger create(boolean showLine, String tag) {
    return new LLogger(showLine, Logger.findPlatform(tag), null);
  }

  public static LLogger create(boolean showLine, String tag, File logFileDir) {
    return new LLogger(showLine, Logger.findPlatform(tag), new FileLog(tag, logFileDir));
  }

  public static void init() {
    LLOGGER = create();
  }

  /**
   * 配置方法
   */
  public static void init(boolean showLine) {
    LLOGGER = create(showLine);
  }

  /**
   * 配置方法
   *
   * @param tag log的tag显示
   */
  public static void init(boolean showLine, String tag) {
    LLOGGER = create(showLine, tag);
  }

  /**
   * 配置方法
   *
   * @param tag log的tag显示
   * @param logFileDir log
   */
  public static void init(boolean showLine, String tag, File logFileDir) {
    LLOGGER = create(showLine, tag, logFileDir);
  }

  public static void v() {
    if (LLOGGER != null) {
      LLOGGER.printLog(V);
    }
  }

  public static void v(Object msg) {
    if (LLOGGER != null) {
      LLOGGER.printLog(V, msg);
    }
  }

  public static void v(Object... objects) {
    if (LLOGGER != null) {
      LLOGGER.printLog(V, objects);
    }
  }

  public static void d() {
    if (LLOGGER != null) {
      LLOGGER.printLog(D);
    }
  }

  public static void d(Object msg) {
    if (LLOGGER != null) {
      LLOGGER.printLog(D, msg);
    }
  }

  public static void d(Object... objects) {
    if (LLOGGER != null) {
      LLOGGER.printLog(D, objects);
    }
  }

  public static void i() {
    if (LLOGGER != null) {
      LLOGGER.printLog(I);
    }
  }

  public static void i(Object msg) {
    if (LLOGGER != null) {
      LLOGGER.printLog(I, msg);
    }
  }

  public static void i(Object... objects) {
    if (LLOGGER != null) {
      LLOGGER.printLog(I, objects);
    }
  }

  public static void w() {
    if (LLOGGER != null) {
      LLOGGER.printLog(W);
    }
  }

  public static void w(Object msg) {
    if (LLOGGER != null) {
      LLOGGER.printLog(W, msg);
    }
  }

  public static void w(Object... objects) {
    if (LLOGGER != null) {
      LLOGGER.printLog(W, objects);
    }
  }

  public static void e() {
    if (LLOGGER != null) {
      LLOGGER.printLog(E);
    }
  }

  public static void e(Object msg) {
    if (LLOGGER != null) {
      LLOGGER.printLog(E, msg);
    }
  }

  public static void e(Throwable throwable) {
    if (LLOGGER != null) {
      LLOGGER.printLog(E, Utils.getStackTraceString(throwable));
    }
  }

  public static void e(Object... objects) {
    if (LLOGGER != null) {
      LLOGGER.printLog(E, objects);
    }
  }

  public static void a() {
    if (LLOGGER != null) {
      LLOGGER.printLog(A);
    }
  }

  public static void a(Object msg) {
    if (LLOGGER != null) {
      LLOGGER.printLog(A, msg);
    }
  }

  public static void a(Object... objects) {
    if (LLOGGER != null) {
      LLOGGER.printLog(A, objects);
    }
  }

  /**
   * @see #trace(String)
   */
  public static void trace() {
    if (LLOGGER != null) {
      LLOGGER.printTrace();
    }
  }

  /**
   * 打印代码调用栈，在引入的混淆导致无法正确获取代码信息
   */
  public static void trace(String msg) {
    if (LLOGGER != null) {
      LLOGGER.printTrace(msg);
    }
  }

  public static void json(JSONObject jsonObject) {
    if (LLOGGER != null) {
      LLOGGER.printJson(jsonObject);
    }
  }

  public static void json(JSONArray jsonArray) {
    if (LLOGGER != null) {
      LLOGGER.printJson(jsonArray);
    }
  }

  public void printTrace() {
    String headString = wrapperContent();
    printLog(D, headString + Utils.getStackTrace());
  }

  public void printTrace(String msg) {
    String headString = wrapperContent();
    printLog(D, headString + msg + Utils.getStackTrace());
  }

  public void printLog(int type) {
    printLog(type, new Object[]{DEFAULT_MESSAGE});
  }

  public void printLog(int type, Object object) {
    printLog(type, new Object[]{object});
  }

  public void printLog(int type, Object... objects) {

    String message;

    if (showLine) {
      String headString = wrapperContent();
      message = headString + ((objects == null) ? NULL : getObjectsString(objects));
    } else {
      message = (objects == null) ? NULL : getObjectsString(objects);
    }

    logger.log(type, message);

    if (fileLog != null) {
      try {
        String fileLogPath = fileLog.printLog(type, message);
        if (fileLogPath != null) {
          logger.log(I, "create log file " + fileLogPath);
        }
      } catch (IOException e) {
        logger.log(E, e.toString());
      }
    }
  }

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


  /**
   * 获取日志目录下的日志文件，空参没有限定时间，即所有的log日志文件
   *
   * @return 符合限定时间的文件列表
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   */
  public static List<File> logList() throws FileNotFoundException {

    if (LLOGGER != null) {
      return LLOGGER.fetchLogList();
    }

    return Collections.EMPTY_LIST;
  }

  /**
   * 获取当前小时的前lastHour小时的log文件列表
   *
   * @param lastHour lastHour 当前时间的前几个小时，如果为0表示当前小时
   * @return 符合限定时间的文件列表
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   */
  public static List<File> logList(int lastHour) throws FileNotFoundException {

    if (LLOGGER != null) {
      return LLOGGER.fetchLogList(lastHour);
    }

    return Collections.EMPTY_LIST;
  }

  /**
   * 获取限定时间节点的log文件列表
   *
   * @param beginTime 限定的日志开始时间 即[beginTime(开始时间)~当前时间]，如果为0表示没有时间限制，返回所有的log文件列表
   * @return 符合限定时间的文件列表
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   */
  public static List<File> logList(long beginTime) throws FileNotFoundException {

    if (LLOGGER != null) {
      return LLOGGER.fetchLogList(beginTime);
    }

    return Collections.EMPTY_LIST;
  }

  public List<File> fetchLogList() throws FileNotFoundException {

    if (fileLog == null) {
      throw new FileNotFoundException("没有配置日志目录");
    }

    return fileLog.fetchLogFiles(FETCH_ALL_LOG);
  }

  public List<File> fetchLogList(int lastHour) throws FileNotFoundException {

    if (fileLog == null) {
      throw new FileNotFoundException("没有配置日志目录");
    }

    long curTime = System.currentTimeMillis();
    long beginTime = curTime / HOUR_TIME * HOUR_TIME - (HOUR_TIME * lastHour);

    return fileLog.fetchLogFiles(beginTime);
  }

  public List<File> fetchLogList(long beginTime) throws FileNotFoundException {

    if (fileLog == null) {
      throw new FileNotFoundException("没有配置日志目录");
    }

    return fileLog.fetchLogFiles(beginTime);
  }

  /**
   * 压缩打包没有时间限制的log文件，即打包所有log文件
   *
   * @param zipFileName 压缩包文件名
   * @throws IOException 压缩文件操作异常
   * @see com.licola.llogger.LLogger#logList()
   */
  public static File logZipFile(String zipFileName)
      throws IOException {
    if (LLOGGER != null) {
      return LLOGGER.makeLogZipFile(zipFileName, FETCH_ALL_LOG);
    }
    return null;
  }


  /**
   * 压缩打包当前小时的前lastHour小时的log文件
   *
   * @param zipFileName 压缩包文件名
   * @param lastHour 当前时间的前几个小时，如果为0表示当前小时
   * @return 压缩的log文件zip
   * @throws IOException 压缩文件操作异常
   * @see com.licola.llogger.LLogger#logList(int)
   */
  public static File logZipFile(String zipFileName, int lastHour)
      throws IOException {

    if (LLOGGER != null) {
      long curTime = System.currentTimeMillis();
      long beginTime = curTime / HOUR_TIME * HOUR_TIME - (HOUR_TIME * lastHour);
      return LLOGGER.makeLogZipFile(zipFileName, beginTime);
    }

    return null;
  }

  /**
   * 压缩打包限定时间节点的log文件
   *
   * @param zipFileName 压缩包文件名
   * @param beginTime 限定的日志开始时间 即[beginTime(开始时间)~当前时间]，如果为0表示没有时间限制，打包所有的log文件
   * @return 压缩的log文件zip
   * @throws IOException 压缩文件操作异常
   * @see LLogger#logList(int)
   */
  public File makeLogZipFile(String zipFileName, long beginTime)
      throws IOException {

    if (fileLog == null) {
      throw new FileNotFoundException("没有配置日志目录");
    }

    return fileLog.makeZipFile(zipFileName, beginTime);
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
      case LLogger.V:
        typeStr = "Verbose";
        break;
      case LLogger.D:
        typeStr = "Debug";
        break;
      case LLogger.I:
        typeStr = "Info";
        break;
      case LLogger.W:
        typeStr = "Warn";
        break;
      case LLogger.E:
        typeStr = "Error";
        break;
      case LLogger.A:
        typeStr = "Assert";
        break;
      default:
        typeStr = "Unknown";
    }
    return typeStr;
  }

}
