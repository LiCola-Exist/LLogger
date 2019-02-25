
# [LLogger](https://github.com/LiCola/LLogger)

[ ![Download](https://api.bintray.com/packages/licola/maven/LLogger/images/download.svg) ](https://bintray.com/licola/maven/LLogger/_latestVersion)

# 作用
日志工具，支持更多信息的打印
 - 支持打印行号、方法、内部类名，支持在Logcat中的点击行号跳转代码
 - 支持空参，单一参数，多参数打印
 - 支持log日志信息写入本地文件,以时间为节点，避免日志内容过长，且支持获取和压缩打包log文件
 - 支持Java环境log打印，打印内容近似Android-Logcat风格
 - 支持JSON字符串、JSON对象、JSON数组友好格式化打印
 - 支持代码追踪调试，trace()方法打印方法调用栈（看源码效率工具）
 - 支持超长4000+字符串长度打印
 - ~~支持UI主线程耗时任务检测(Android环境)，打印耗时任务相关代码行~~
 - 支持运行环境中多个LLogger日志实例，以外观模式简化调用（主要使用），并可以创建多个实例，以细分管理日志。

# 引用

```java
  implementation "com.licola:llogger:1.4.8"
```

# 更新日志
 - 1.4.3:优化log文件写入的IO操作
 - 1.4.4:加入Json信息写入日志操作
 - 1.4.5:修复在特殊Android运行环境(如Xposed)的情况下某些log信息无效情况
 - 1.4.6:修改部分对象使用方式，优化内存。
 - 1.4.8:加入日志对象的创建，并暴露给外部使用，以实现运行环境多个LLogger日志。
# 使用
外观模式，静态方法，一行代码调用
```java
    LLogger.d();
    LLogger.d("debug");
    LLogger.d("debug", "more info");
    LLogger.json(jsonObject);
    LLogger.trace();//打印方法调用栈
    
    List<File> logFileAll = LLogger.logList();//获取全部日志文件
    File logZipFile = LLogger.logZipFile("log.zip", 24);//获取前24小时的日志文件并打包压缩到zip包中，如果为0表示当前小时，
```

# 配置
LLogger默认不打印log，默认不写入log文件。
默认tag为```LLogger```，提供静态方法一行参数控制日志。

在实际项目需要配置参数，建议在```Application```类中初始化配置，下面示例代码
```java
/**
 * Created by LiCola on 2018/5/14.
 */
public class MyApplication extends Application {

    public static final String LOG_FILE_DIR = "log-files";
  
    public static final String TAG = "Demo";
  
    @Override
    public void onCreate() {
      super.onCreate();
  
      /**
       * 空参初始化：使用默认tag，默认打印行号，不写本地日志文件。
       */
  //    LLogger.init();
  
      /**
       * 初始化：显示行号，配置tag，不写本地日志文件
       */
  //    LLogger.init(true,TAG);
  
      /**
       * 1：建议log文件存放在项目内部存储中，避免读写外部存储的权限处理
       * 2：建议在cache下指定二级目录 存放log文件 避免cache中文件杂乱
       */
      File logDir = new File(getCacheDir(), LOG_FILE_DIR);
  
      /**
       * 初始化：显示行号，配置tag，log信息写入本地目录
       */
      LLogger.init(true, TAG, logDir);
  
    }
}
```
# 效果图

![log信息](https://github.com/LiCola/LLogger/blob/master/image/android-log.png)

# 其他使用
创建LLogger日志实例，在某些模块使用（不同的日志实例拥有不同的日志目录）
```java
      LLogger lLogger = LLogger.create(false, "Other", new File(getCacheDir(), "other"));
      lLogger.printLog(LLogger.V);
      lLogger.printLog(LLogger.D);
      lLogger.printJson(new JSONObject().put("key", "value"));
      lLogger.printTrace();
      
      List<File> files = lLogger.fetchLogList(2);
```

# 关于log本地文件
可以看到上图的```LLogger_2018-06-04_18.log```日志行，表示创建以小时为节点的log文件。

**文件名格式：文件名前缀_日期信息_小时信息**

采用小时为节点，写入到本地文件，是为了避免信息太过冗长。
效果如下:

![log文件信息](https://github.com/LiCola/LLogger/blob/master/image/log-file.png)

基本仿照Logcat格式：也是支持点击行号跳转代码
```log
log：18:26:04.068 main Verbose/Demo: [ (MainActivity.java:25)#onClickLogV ] verbose
格式：时间 线程名 log类型/Tag：[ (类名:行号)#方法名 ] 参数
```

# 获取log文件
因为log文件以时间为节点分开，会存在大量的日志文件，所以提供静态方法获取日志文件列表或压缩日志文件
```java
    List<File> files = LLogger.fetchLogList();//获取所有的日志文件列表
    List<File> files = LLogger.fetchLogList(24);//获取前24小时内的日志文件列表

    File logZipFile = LLogger.makeLogZipFile("log.zip");//打包所有的日志文件
    File logZipFile = LLogger.makeLogZipFile("log.zip", 0);//打包当前时间的日志文件 ，如果为0表示当前小时

```

# 关于Java环境
LLogger内部初始化时判定运行环境，如果是Java环境，也可以打印出log信息
![java环境-log信息](https://github.com/LiCola/LLogger/blob/master/image/java-log.png)

基本仿照Logcat格式：也是支持点击行号跳转代码
```log
log：06-04 19:02:18.971 main Debug/Java: [ (JavaMain.java:17)#main ] execute
格式：日期 时间 线程名 log类型/Tag：[ (类名:行号)#方法名 ] 参数
```

具体比如在IDEA的纯Java环境也可以在Run中打印出丰富的信息

# 混淆
因为日志库中代码有线程方法栈的使用（Thread.currentThread().getStackTrace()），对代码结构敏感，如果开启混淆代码结构变化，导致方法内联等就会让日志库打印信息错误。
所以需要在依赖该库的项目里的"proguard-rules.pro"文件添加
```proguard
#llogger的混淆配置
-keep class com.licola.llogger.**{*;}
```

开启混淆的同时可能会改变类和方法的名称，这样就会导致日志行打印的部分信息无效。
我正在考虑在混淆版本中修改日志输出结构，然后用proguard的retrace.jar库加载mapping.txt映射信息恢复原有代码信息

# 优化
日志文件的写入肯定是IO操作，这在移动设备是一大性能优化方向。
考虑日志的使用场景，随时可能发生写日志数据。且需要保证日志的完整性，当程序发生Crash或进程被杀死后需要保证日志完整写入。
这就限制了在写日志的优化方向，不能使用内存Buffer缓存日志避免IO，因为内存在Crash时就无法保证数据正常。
所以就提出了高性能方案：mmap，内存映射文件。具体使用就是`MappedByteBuffer`类映射本地文件进行读写。

## 结果
`MappedByteBuffer`-内存映射方案 `FileWriter`-普通IO，比对。
目前在PC设备（i7/16G）上能够达到优化0.05ms效果（20W次log写入测试）。
在移动设备上因为的内存GC的影响太大，无法具体确认优化效果，只能简单说能够达到1ms级别的优化效果。


# 参考
- [KLog](https://github.com/ZhaoKaiQiang/KLog),感谢提供基础思路。 
- [Android 高性能日志写入方案](https://juejin.im/post/5b6d26016fb9a04f86065cdf)