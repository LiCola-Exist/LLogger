package com.licola.llogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by LiCola on 2018/5/14.
 */
class FileLog {

  private static final char SPACING = ' ';
  private static final String FILE_FORMAT = ".log";
  private static final String ZIP_SUFFIX = ".zip";
  private static final String DEFAULT_FILE_PREFIX = "_";

  private static final int BUFFER_SIZE = 4096;

  private static final ThreadLocal<SimpleDateFormat> FORMAT_FILE = new ThreadLocal<SimpleDateFormat>() {
    @Override
    protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat("yyyy-MM-dd_HH", Locale.CHINA);
    }
  };

  private static final ThreadLocal<SimpleDateFormat> FORMAT_INFO = new ThreadLocal<SimpleDateFormat>() {
    @Override
    protected SimpleDateFormat initialValue() {
      return new SimpleDateFormat("HH:mm:ss.SSS", Locale.CHINA);
    }
  };


  private final ReadWriteLock readWriteLock;
  private final String logTag;
  private final File logFileDir;

  FileLog(String logTag, File logFileDir) {
    checkDirFile(logFileDir);
    this.logFileDir = logFileDir;
    this.logTag = logTag;
    this.readWriteLock = new ReentrantReadWriteLock(false);
  }

  String printLog(int type, String msg) throws IOException {
    long timeMillis = System.currentTimeMillis();

    String timeFileInfo = FORMAT_FILE.get().format(timeMillis);
    String logFileName = logTag + DEFAULT_FILE_PREFIX + timeFileInfo + FILE_FORMAT;

    String timeInfo = FORMAT_INFO.get().format(timeMillis);

    File logFile = new File(logFileDir, logFileName);

    //写锁 获取锁
    Lock writeLock = readWriteLock.writeLock();
    writeLock.lock();
    boolean createFileFlag;
    try {
      //检查目标日志文件
      createFileFlag = createFile(logFile);
      //写入日志
      writeInfo(logFile, timeInfo, LLogger.mapperType(type), logTag, msg);
    } catch (IOException e) {
      throw new IOException("log info write fail", e);
    } finally {
      //写锁 释放锁
      writeLock.unlock();
    }

    if (createFileFlag) {
      return logFile.getAbsolutePath();
    } else {
      return null;
    }
  }

  File makeZipFile(String zipFileName, long beginTime)
      throws IOException {

    File zipFile;

    if (zipFileName.endsWith(ZIP_SUFFIX)) {
      zipFile = new File(logFileDir, zipFileName);
    } else {
      zipFile = new File(logFileDir, zipFileName + ZIP_SUFFIX);
    }

    //读锁 获取锁
    Lock readLock = readWriteLock.readLock();
    readLock.lock();

    //重入
    List<File> files = fetchLogFiles(beginTime);

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
    } finally {
      readLock.unlock();
      try {
        zipOutputStream.close();
      } catch (IOException e) {
      }
      try {
        zipFileOutputStream.close();
      } catch (IOException e) {
      }
    }

    return zipFile;
  }

  List<File> fetchLogFiles(long beginTime)
      throws FileNotFoundException {

    //读锁 获取锁
    Lock readLock = readWriteLock.readLock();
    readLock.lock();

    File[] files = logFileDir.listFiles();

    if (files == null || files.length == 0) {
      //释放锁
      readLock.unlock();
      throw new FileNotFoundException(logFileDir.getAbsolutePath() + " is empty dir");
    }

    ArrayList<File> logFiles = new ArrayList<>();
    for (File file : files) {
      String fileName = file.getName();
      if (!fileName.startsWith(logTag) || !fileName.endsWith(FILE_FORMAT)) {
        //去除非目标日志 即非固定前缀和固定后缀的文件名
        continue;
      }

      if (beginTime == 0) {
        //开始时间为0 表示没有限制条件
        logFiles.add(file);
      } else {
        String timeFileInfo = fileName
            .substring(logTag.length() + DEFAULT_FILE_PREFIX.length(),
                fileName.length() - FILE_FORMAT.length());
        long fileTime = getFileTime(timeFileInfo);
        if (fileTime >= beginTime) {
          //log文件保存时间 >= 限定开始时间 即在限定时间之后的日志
          logFiles.add(file);
        }
      }
    }

    //释放锁
    readLock.unlock();
    if (logFiles.isEmpty()) {
      throw new FileNotFoundException("No files meet the conditions of time");
    } else {
      return logFiles;
    }
  }

  private static long getFileTime(String timeFileInfo) {

    Date date;
    try {
      date = FORMAT_FILE.get().parse(timeFileInfo);
    } catch (ParseException e) {
      date = null;
    }

    return date != null ? date.getTime() : 0;
  }

  private static void writeInfo(File logFile, String timePrefix, String type, String tag,
      String msg)
      throws IOException {

    String threadName = Thread.currentThread().getName();

    String output = timePrefix
        + SPACING
        + threadName
        + SPACING
        + type
        + '/'
        + tag
        + ':'
        + msg
        + "\n";

    mappedByteBufferWrite(logFile, output);
  }

  private static void checkDirFile(File logFileDir) {
    if (logFileDir == null) {
      throw new NullPointerException("logFileDir == null");
    }

    if (logFileDir.exists() && !logFileDir.isDirectory()) {
      throw new IllegalArgumentException("logFileDir must be directory");
    }

    boolean mkdirs = logFileDir.mkdirs();
    if (!mkdirs && !logFileDir.exists()) {
      throw new RuntimeException("logFileDir mkdirs failed:" + logFileDir.getAbsolutePath());
    }
  }

  private static boolean createFile(File file) throws IOException {

    if (file.exists()) {
      if (!file.isFile()) {
        throw new IOException(
            "file " + file.getAbsolutePath() + " is not file cannot input log");
      }
      return false;
    } else {
      try {
        return file.createNewFile();
      } catch (IOException e) {
        throw new IOException("file" + file.getAbsolutePath() + " createNewFile fail", e);
      }
    }
  }

  private static void writeZipFile(ZipOutputStream zipOutputStream, File file) throws IOException {
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(file);
      ZipEntry zipEntry = new ZipEntry(file.getName());
      zipOutputStream.putNextEntry(zipEntry);

      byte[] bytes = new byte[BUFFER_SIZE];
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

  private static void mappedByteBufferWrite(File logFile, String output)
      throws IOException {
    byte[] srcByte = output.getBytes();
    int length = srcByte.length;
    RandomAccessFile randomAccessFile = null;

    try {
      randomAccessFile = new RandomAccessFile(logFile, "rw");
      MappedByteBuffer byteBuffer = randomAccessFile
          .getChannel()
          .map(MapMode.READ_WRITE, logFile.length(), length);
      for (byte aSrcByte : srcByte) {
        byteBuffer.put(aSrcByte);
      }
    } finally {
      if (randomAccessFile != null) {
        randomAccessFile.close();
      }
    }
  }


}
