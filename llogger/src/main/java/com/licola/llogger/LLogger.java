package com.licola.llogger;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Log日志工具类
 * 1:支持打印行号、方法、内部类名
 * 2:支持在Logcat中的点击行号跳转代码
 * 3:支持空参，单一参数，多参数打印
 * 4:支持log日志信息写入本地文件,以时间为节点，避免日志过长，且支持打包log文件
 * 5:支持Java环境log打印，如在android的test本地单元测试中打印
 * 6:支持JSON字符串、JSON对象、JSON数组友好格式化打印 7:支持超长4000+字符串长度打印
 *
 * 基于：https://github.com/ZhaoKaiQiang/KLog项目改造
 */
public final class LLogger {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  private static final String DEFAULT_MESSAGE = "execute";
  private static final String DEFAULT_TRACE = "trace";
  private static final String ARGUMENTS = "argument";
  private static final String NULL = "null";

  private static final String SUFFIX_JAVA = ".java";
  private static final String ANONYMITY_JAVA_FLAG = "$";

  private static final String DEFAULT_TAG = "LLogger";
  private static final long FETCH_ALL_LOG = 0;
  private static final long HOUR_TIME = 60 * 60 * 1000;

  private static final int JSON_INDENT = 4;

  public static final int V = 0x1;
  public static final int D = 0x2;
  public static final int I = 0x3;
  public static final int W = 0x4;
  public static final int E = 0x5;
  public static final int A = 0x6;


  private static boolean mShowLog = true;//默认显示log
  static String TAG = DEFAULT_TAG;

  private static Logger logger;
  private static FileLog fileLog;

  static {
    boolean androidAvailable = AndroidLogger.isAndroidLogAvailable();
    logger = androidAvailable ? new AndroidLogger() : new JavaLogger();
  }

  /**
   * 配置方法
   *
   * @param showLog true：打印log false：不打印
   */
  public static void init(boolean showLog) {
    init(showLog, DEFAULT_TAG);
  }

  /**
   * 配置方法
   *
   * @param showLog showLog true：打印log false：不打印
   * @param tag log的tag显示
   */
  public static void init(boolean showLog, String tag) {
    mShowLog = showLog;
    TAG = tag;
  }

  /**
   * 配置方法
   *
   * @param showLog showLog true：打印log false：不打印
   * @param tag log的tag显示
   * @param logFileDir log
   */
  public static void init(boolean showLog, String tag, File logFileDir) {
    init(showLog, tag, logFileDir, FileLog.DEFAULT_FILE_PREFIX);
  }

  public static void init(boolean showLog, String tag, File logFileDir, String logFilePrefix) {
    mShowLog = showLog;
    TAG = tag;
    fileLog = new FileLog(logFileDir, logFilePrefix);
  }


  public static void v() {
    printLog(V, DEFAULT_MESSAGE);
  }

  public static void v(Object msg) {
    printLog(V, msg);
  }

  public static void v(Object... objects) {
    printLog(V, objects);
  }

  public static void d() {
    printLog(D, DEFAULT_MESSAGE);
  }

  public static void d(Object msg) {
    printLog(D, msg);
  }

  public static void d(Object... objects) {
    printLog(D, objects);
  }

  public static void i() {
    printLog(I, DEFAULT_MESSAGE);
  }

  public static void i(Object msg) {
    printLog(I, msg);
  }

  public static void i(Object... objects) {
    printLog(I, objects);
  }

  public static void w() {
    printLog(W, DEFAULT_MESSAGE);
  }

  public static void w(Object msg) {
    printLog(W, msg);
  }

  public static void w(Object... objects) {
    printLog(W, objects);
  }

  public static void e() {
    printLog(E, DEFAULT_MESSAGE);
  }

  public static void e(Object msg) {
    printLog(E, msg);
  }

  public static void e(Object... objects) {
    printLog(E, objects);
  }

  public static void a() {
    printLog(A, DEFAULT_MESSAGE);
  }

  public static void a(Object msg) {
    printLog(A, msg);
  }

  public static void a(Object... objects) {
    printLog(A, objects);
  }

  /**
   * @see #trace(String)
   */
  public static void trace() {
    printStackTrace(DEFAULT_TRACE);
  }

  /**
   * 打印代码调用栈，在引入的混淆导致无法正确获取代码信息
   */
  public static void trace(String msg) {
    printStackTrace(msg);
  }

  /**
   * 开启检测主线程耗时任务功能
   */
  public static void startMonitor() {
    logger.startMonitor(AndroidUIMonitor.TIME_OUT_LIMIT);
  }

  /**
   * 开启检测主线程耗时任务功能 ，如果任务执行超过指定时间，会log打印出相关代码行
   *
   * @param timeOut 指定主线程任务执行最大时间
   */
  public static void startMonitor(long timeOut) {
    logger.startMonitor(timeOut);
  }

  /**
   * 停止主线程耗时任务检测
   */
  public static void stopMonitor() {
    logger.stopMonitor();
  }

  public static void json(JSONObject jsonObject) {
    printJson(jsonObject);
  }

  public static void json(JSONArray jsonArray) {
    printJson(jsonArray);
  }

  private static void printLog(int type, Object... objects) {
    if (!mShowLog) {
      return;
    }

    String headString = wrapperContent();
    String msg = (objects == null) ? NULL : getObjectsString(objects);

    String message = headString + msg;

    logger.log(type, TAG, message);

    if (fileLog != null) {
      printFile(type, TAG, message);
    }
  }

  public static void printJson(Object object) {

    if (!mShowLog) {
      return;
    }

    String headString = wrapperContent();

    int type = I;
    String msg;
    try {
      if (object instanceof JSONObject) {
        msg = ((JSONObject) object).toString(JSON_INDENT);
      } else if (object instanceof JSONArray) {
        msg = ((JSONArray) object).toString(JSON_INDENT);
      } else {
        throw new JSONException("非Json类型");
      }
    } catch (JSONException e) {
      type = E;
      msg = Utils.getStackTraceString(e);
    }

    String message = headString + LINE_SEPARATOR + msg;
    logger.log(type, TAG, message);

    if (fileLog != null) {
      printFile(type, TAG, message);
    }

  }

  private static void printFile(int type, String tag, String msg) {

    long timeMillis = System.currentTimeMillis();
    try {
      String fileLogPath = fileLog.printFileLog(timeMillis, type, tag, msg);
      if (fileLogPath != null) {
        logger.log(I, TAG, "create log file " + fileLogPath);
      }
    } catch (IOException e) {
      logger.log(E, TAG, e.toString());
    }
  }

  /**
   * 获取日志目录下的日志文件，空参没有限定时间，即所有的log日志文件
   *
   * @return 符合限定时间的文件列表
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   */
  public static List<File> fetchLogList() throws FileNotFoundException {
    return fetchLogList(FETCH_ALL_LOG);
  }

  /**
   * 获取当前小时的前lastHour小时的log文件列表
   *
   * @param lastHour lastHour 当前时间的前几个小时，如果为0表示当前小时
   * @return 符合限定时间的文件列表
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   */
  public static List<File> fetchLogList(int lastHour) throws FileNotFoundException {
    long curTime = System.currentTimeMillis();
    long beginTime = curTime / HOUR_TIME * HOUR_TIME - (HOUR_TIME * lastHour);
    return fetchLogList(beginTime);
  }

  /**
   * 获取限定时间节点的log文件列表
   *
   * @param beginTime 限定的日志开始时间 即[beginTime(开始时间)~当前时间]，如果为0表示没有时间限制，返回所有的log文件列表
   * @return 符合限定时间的文件列表
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   */
  public static List<File> fetchLogList(long beginTime) throws FileNotFoundException {
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
   * @see com.licola.llogger.LLogger#fetchLogList()
   */
  public static File makeLogZipFile(String zipFileName)
      throws IOException {
    return makeLogZipFile(zipFileName, FETCH_ALL_LOG);
  }


  /**
   * 压缩打包当前小时的前lastHour小时的log文件
   *
   * @param zipFileName 压缩包文件名
   * @param lastHour 当前时间的前几个小时，如果为0表示当前小时
   * @return 压缩的log文件zip
   * @throws IOException 压缩文件操作异常
   * @see com.licola.llogger.LLogger#fetchLogList(int)
   */
  public static File makeLogZipFile(String zipFileName, int lastHour)
      throws IOException {
    long curTime = System.currentTimeMillis();
    long beginTime = curTime / HOUR_TIME * HOUR_TIME - (HOUR_TIME * lastHour);
    return makeLogZipFile(zipFileName, beginTime);
  }

  /**
   * 压缩打包限定时间节点的log文件
   *
   * @param zipFileName 压缩包文件名
   * @param beginTime 限定的日志开始时间 即[beginTime(开始时间)~当前时间]，如果为0表示没有时间限制，打包所有的log文件
   * @return 压缩的log文件zip
   * @throws IOException 压缩文件操作异常
   * @see com.licola.llogger.LLogger#fetchLogList(long)
   */
  public static File makeLogZipFile(String zipFileName, long beginTime)
      throws IOException {

    if (fileLog == null) {
      throw new FileNotFoundException("没有配置日志目录");
    }

    return fileLog.makeZipFile(zipFileName, beginTime);
  }


  private static void printStackTrace(String msg) {
    if (!mShowLog) {
      return;
    }

    String headString = wrapperContent();
    logger.log(D, TAG, headString + msg + Utils.getStackTrace());
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
