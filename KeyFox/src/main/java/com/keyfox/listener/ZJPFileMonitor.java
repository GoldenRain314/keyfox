package com.keyfox.listener;

import java.io.File;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * 文件监控开关
 * Created by Administrator on 2017/12/15.
 */
public class ZJPFileMonitor {
    private FileAlterationMonitor monitor = null;
    public ZJPFileMonitor(long interval) throws Exception {
        monitor = new FileAlterationMonitor(interval);
    }

    public void monitor(String path, FileAlterationListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);
        observer.addListener(listener);
    }
    public void stop() throws Exception{
        monitor.stop();
    }
    public void start() throws Exception {
        monitor.start();
    }
}
