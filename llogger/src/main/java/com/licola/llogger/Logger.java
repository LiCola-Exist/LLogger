package com.licola.llogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author LiCola
 * @date 2019-06-05
 */
public interface Logger {

  int V = 0x1;
  int D = 0x2;
  int I = 0x3;
  int W = 0x4;
  int E = 0x5;
  int A = 0x6;

  /**
   * @param type 参见V/D/I/W/E/A各种日志类型
   */
  void printLog(int type);

  /**
   * @param type 参见V/D/I/W/E/A各种日志类型
   * @param object 内容对象,即调起{@link #toString()}方法打印内容
   */
  void printLog(int type, Object object);

  /**
   * @param type 参见V/D/I/W/E/A各种日志类型
   * @param objects 内容对象可变数组,即批量调起{@link #toString()}方法打印内容
   */
  void printLog(int type, Object... objects);

  /**
   * @param type 参见V/D/I/W/E/A各种日志类型
   * @param throwable 默认会打印全部的异常堆栈信息
   */
  void printLog(int type, Throwable throwable);

  /**
   * 友好格式化的JSONObject，即带缩进的打印内容
   */
  void printJson(JSONObject object);

  /**
   * 友好格式化的JSONArray，即带缩进的打印内容
   */
  void printJson(JSONArray object);

  /**
   * 打印当前调用该方法的全部方法栈信息，调试阶段很有用,引入的混淆导致无法正确获取代码信息
   */
  void printTrace();

  /**
   * 打印当前调用该方法的全部方法栈信息，调试阶段很有用,引入的混淆导致无法正确获取代码信息
   *
   * @param msg 附加信息
   */
  void printTrace(String msg);

  /**
   * 获取日志目录下的日志文件，空参没有限定时间，即所有的log日志文件
   *
   * @return 符合限定时间的文件列表, 如果没有配置日志文件则返回空集合
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   */
  List<File> fetchLogList() throws FileNotFoundException;

  /**
   * 获取当前小时的前lastHour小时的log文件列表
   *
   * @param lastHour lastHour 当前时间的前几个小时，如果为0表示当前小时
   * @return 符合限定时间的文件列表, 如果没有配置日志文件则返回空集合
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   */
  List<File> fetchLogList(int lastHour) throws FileNotFoundException;

  /**
   * 获取限定时间节点的log文件列表
   *
   * @param beginTime 限定的日志开始时间 即[beginTime(开始时间)~当前时间]，如果为0表示没有时间限制，返回所有的log文件列表
   * @return 符合限定时间的文件列表, 如果没有配置日志文件则返回空集合
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   */
  List<File> fetchLogList(long beginTime) throws FileNotFoundException;

  /**
   * 压缩打包没有时间限制的log文件，即打包所有log文件
   *
   * @param zipFileName 压缩包文件名
   * @return 压缩的log文件zip, 如果没有配置 会返回null
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   * @throws IOException 压缩文件操作异常
   */
  File makeLogZipFile(String zipFileName) throws IOException;

  /**
   * 压缩打包当前小时的前lastHour小时的log文件
   *
   * @param zipFileName 压缩包文件名
   * @param lastHour 当前时间的前几个小时，如果为0表示当前小时
   * @return 压缩的log文件zip, 如果没有配置 会返回null
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   * @throws IOException 压缩文件操作异常
   */
  File makeLogZipFile(String zipFileName, int lastHour) throws IOException;

  /**
   * 压缩打包限定时间节点的log文件
   *
   * @param zipFileName 压缩包文件名
   * @param beginTime 限定的日志开始时间 即[beginTime(开始时间)~当前时间]，如果为0表示没有时间限制，打包所有的log文件
   * @return 压缩的log文件zip, 如果没有配置 会返回null
   * @throws FileNotFoundException 没有找到符合限定时间节点的log文件列表
   * @throws IOException 压缩文件操作异常
   */
  File makeLogZipFile(String zipFileName, long beginTime) throws IOException;
}

