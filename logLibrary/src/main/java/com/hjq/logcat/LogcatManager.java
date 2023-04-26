package com.hjq.logcat;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/Logcat
 * time   : 2020/01/24
 * desc   : 日志管理类
 * doc    : https://developer.android.google.cn/studio/command-line/logcat
 */
@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
final class LogcatManager {

    /**
     * 日志捕捉监听对象
     */
    private static volatile Callback sCallback;
    /**
     * 日志捕捉标记
     */
    private static volatile boolean FLAG_WORK;
    /**
     * 备用存放集合
     */
    private static final List<LogcatInfo> LOG_BACKUP = new ArrayList<>();

    /**
     * 开始捕捉
     */
    static void start(Callback callback) {
        FLAG_WORK = true;
        new Thread(new LogRunnable()).start();
        sCallback = callback;
    }

    /**
     * 继续捕捉
     */
    static void resume() {
        FLAG_WORK = true;
        final Callback callback = sCallback;
        if (callback != null && !LOG_BACKUP.isEmpty()) {
            for (LogcatInfo info : LOG_BACKUP) {
                if (info == null) {
                    continue;
                }
                callback.onReceiveLog(info);
            }
        }
        LOG_BACKUP.clear();
    }

    /**
     * 暂停捕捉
     */
    static void pause() {
        FLAG_WORK = false;
    }

    /**
     * 停止捕捉
     */
    static void destroy() {
        FLAG_WORK = false;
        // 把监听对象置空，不然会导致内存泄漏
        sCallback = null;
    }

    /**
     * 清空日志
     */
    static void clear() {
        try {
            new ProcessBuilder("logcat", "-c").start();
        } catch (IOException ignored) {
        }
    }

    /**
     * 创建 Logcat 日志缓冲区
     */
    private static BufferedReader createLogcatBufferedReader() throws IOException {
        // Process process = Runtime.getRuntime().exec("/system/bin/logcat -b " + "main -P '\"/" + android.os.Process.myPid() + " 10708\"'");
        // Process process = Runtime.getRuntime().exec("/system/bin/logcat -b all -v uid");
        // Process process = Runtime.getRuntime().exec("logcat -b all -v uid");
        //只筛选当前APP的日志
        int pid = android.os.Process.myPid();
        Process process = new ProcessBuilder("logcat", "-v", "threadtime","--pid="+pid).start();
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    /**
     * 关闭流
     */
    private static void closeStream(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class LogRunnable implements Runnable {

        @Override
        public void run() {
            BufferedReader reader = null;

            String line;
            while (true) {
                synchronized (LogcatManager.class) {
                    if (reader == null) {
                        try {
                            reader = createLogcatBufferedReader();
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    try {
                        line = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeStream(reader);
                        break;
                    }
                    if (line == null) {
                        // 正常情况讲，line 是不会为空的，因为没有新的日志前提下 reader.readLine() 会阻塞读取
                        // 但是在某些特殊机型（vivo iQOO 9 Pro Android 12）上面会出现，在没有新的日志前提下，会返回 null
                        // 并且等待一会儿再读取还不行，无论循环等待多次，因为原先的流里面已经没有东西了，要读取新的日志必须创建新的流
                        try {
                            closeStream(reader);
                            reader = null;
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    if (LogcatInfo.IGNORED_LOG.contains(line)) {
                        continue;
                    }
                    LogcatInfo info = LogcatInfo.create(line);
                    if (info == null) {
                        continue;
                    }
                    if (!FLAG_WORK) {
                        // 这里可能会出现下标异常
                        LOG_BACKUP.add(info);
                        continue;
                    }

                    final Callback callback = sCallback;
                    if (callback != null) {
                        callback.onReceiveLog(info);
                    }
                }
            }
        }
    }

    public interface Callback {

        /**
         * 收到日志
         */
        void onReceiveLog(LogcatInfo info);
    }
}