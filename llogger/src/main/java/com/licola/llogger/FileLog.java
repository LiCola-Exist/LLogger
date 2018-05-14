package com.licola.llogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by LiCola on 2018/5/14.
 */
public class FileLog {


  public static void printFile(File logFile, String time, String headString, String msg)
      throws IOException {

    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(logFile, true);
      fileWriter.write(time + ": " + headString + " " + msg + "\n");
      fileWriter.flush();

    } finally {
      if (fileWriter != null) {
        fileWriter.close();
      }
    }

  }
}
