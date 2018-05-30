package com.licola.llogger;

import static com.licola.llogger.LLogger.DEFAULT_TAG;
import static com.licola.llogger.LLogger.E;
import static com.licola.llogger.LLogger.FILE_PREFIX;
import static com.licola.llogger.LLogger.I;
import static com.licola.llogger.LLogger.LINE_SEPARATOR;
import static com.licola.llogger.LLogger.mapperType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by LiCola on 2018/5/14.
 */
public class FileLog {

  private static final String FILE_FORMAT = ".log";

  private static final String DATE_FORMAT_LOG_FILE = "yyyy-MM-dd_HH";
  private static final String DATE_FORMAT_LOG_INFO = "HH:mm:ss.SSS";

  public static void printFile(File logFileDir, long timeMillis, Logger logger, int type,
      String tag, String msg) {

    if (!checkLogDir(logFileDir, logger)) {
      return;
    }

    String timeFileInfo = new SimpleDateFormat(DATE_FORMAT_LOG_FILE,
        Locale.CHINA).format(timeMillis);
    String logFileName = FILE_PREFIX + timeFileInfo + FILE_FORMAT;

    File logFile = checkLogFile(new File(logFileDir, logFileName), logger);
    if (logFile == null) {
      return;
    }

    try {
      String timeInfo = new SimpleDateFormat(DATE_FORMAT_LOG_INFO,
          Locale.CHINA).format(timeMillis);
      writeInfo(logFile, timeInfo, type, tag, msg);
    } catch (IOException e) {
      logger.log(E, DEFAULT_TAG,
          "log info write fail :" + LINE_SEPARATOR + StackTraceUtils.getStackTraceString(e));
    }
  }

  public static File makeZipFile(File logFileDir, String zipFileName, long beginTime)
      throws IOException {

    List<File> files = fetchLogFiles(logFileDir, beginTime);

    File zipFile = new File(logFileDir, zipFileName);

    if (zipFile.exists()) {
      //防止已经存在的文件影响
      zipFile.delete();
    }

    FileOutputStream zipFileOutputStream = new FileOutputStream(zipFile);

    ZipOutputStream zipOutputStream = new ZipOutputStream(zipFileOutputStream);

    try {
      for (File file : files) {
        writeZipFile(zipOutputStream, file);
      }
    } catch (IOException e) {
      throw new IOException("压缩日志文件异常", e);
    }

    try {
      zipOutputStream.close();
    } catch (IOException e) {
    }

    try {
      zipFileOutputStream.close();
    } catch (IOException e) {
    }

    return zipFile;
  }

  private static void writeZipFile(ZipOutputStream zipOutputStream, File file) throws IOException {
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(file);
      ZipEntry zipEntry = new ZipEntry(file.getName());
      zipOutputStream.putNextEntry(zipEntry);

      byte[] bytes = new byte[1024];
      int length;
      while ((length = fileInputStream.read(bytes)) >= 0) {
        zipOutputStream.write(bytes, 0, length);
      }
    } finally {
      if (fileInputStream != null) {
        fileInputStream.close();
      }
    }

  }


  public static List<File> fetchLogFiles(File logFileDir, long beginTime)
      throws FileNotFoundException {
    if (logFileDir == null || !logFileDir.exists()) {
      throw new FileNotFoundException("logFileDir == null or not exists");
    }

    File[] files = logFileDir.listFiles();

    if (files == null || files.length == 0) {
      throw new FileNotFoundException(logFileDir.getAbsolutePath() + " is empty dir");
    }

    ArrayList<File> logFiles = new ArrayList<>();
    for (File file : files) {
      String fileName = file.getName();
      if (!fileName.contains(FILE_PREFIX) || !fileName.contains(FILE_FORMAT)) {
        //去除非目标日志 即非固定前缀和固定后缀的文件名
        continue;
      }

      if (beginTime == 0) {
        //开始时间为0 表示没有限制条件
        logFiles.add(file);
      } else {
        String timeFileInfo = fileName
            .substring(FILE_PREFIX.length(), fileName.length() - FILE_FORMAT.length());
        long fileTime = getFileTime(DATE_FORMAT_LOG_FILE, timeFileInfo);
        if (fileTime >= beginTime) {
          //log文件保存时间 >= 限定开始时间 即在限定时间之后的日志
          logFiles.add(file);
        }
      }
    }

    if (logFiles.isEmpty()) {
      throw new FileNotFoundException("No files meet the conditions of time");
    }

    return logFiles;
  }

  private static long getFileTime(String dataFormatStr, String timeFileInfo) {
    Date dateFile = null;
    try {
      dateFile = new SimpleDateFormat(dataFormatStr,
          Locale.CHINA).parse(timeFileInfo);
    } catch (ParseException e) {

    }

    return dateFile != null ? dateFile.getTime() : 0;
  }

  private static void writeInfo(File logFile, String timePrefix, int type, String tag,
      String msg)
      throws IOException {

    String threadName = Thread.currentThread().getName();

    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(logFile, true);
      fileWriter.write(
          timePrefix + " " + threadName + " " + mapperType(type) + "/" + tag + ": " + msg
              + "\n");
      fileWriter.flush();
    } finally {
      if (fileWriter != null) {
        fileWriter.close();
      }
    }
  }

  private static File checkLogFile(File logFile, Logger logger) {

    if (logFile.exists()) {
      if (!logFile.isFile()) {
        logger.log(E, DEFAULT_TAG,
            "file " + logFile.getAbsolutePath() + " is not file cannot input log");
        return null;
      }
      return logFile;
    } else {
      try {
        logFile.createNewFile();
        logger.log(I, DEFAULT_TAG, "create log file local:" + logFile.getAbsolutePath());
        return logFile;
      } catch (IOException e) {
        e.printStackTrace();
        logger.log(E, DEFAULT_TAG,
            "log create file failed :" + LINE_SEPARATOR + StackTraceUtils.getStackTraceString(e));
      }
    }
    return null;
  }

  private static boolean checkLogDir(File logFileDir, Logger logger) {
    if (logFileDir == null) {
      logger.log(E, DEFAULT_TAG, "logFileDir == nul");
      return false;
    }

    if (!logFileDir.exists()) {
      boolean mkdirs = logFileDir.mkdirs();
      if (!mkdirs) {
        logger.log(E, DEFAULT_TAG, "logFileDir mkdirs failed");
        return false;
      }
    }
    return true;
  }

}
