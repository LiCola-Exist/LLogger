package com.licola.llogger;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LiCola on 2018/5/14.
 */
public class JsonLog {

  private static final int JSON_INDENT = 4;


  public static void printJson(String tag, String headString,Object object) {

    String message;
    String type = "Unknown";

    try {
      if (object instanceof JSONObject){
        message =((JSONObject) object).toString(JSON_INDENT);
        type = "JSONObject";
      }else if (object instanceof JSONArray){
        message =((JSONArray) object).toString(JSON_INDENT);
        type = "JSONArray";
      }else {
        message= object.toString();
      }
    }catch (JSONException e){
      message= object.toString();
    }

    message = headString + type + LLogger.LINE_SEPARATOR + message;
    Log.d(tag, message);

  }
}
