package com.keyfox.controller;

import com.keyfox.listener.ZJPFileListener;
import com.keyfox.listener.ZJPFileMonitor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 * Created by Administrator on 2017/12/15.
 */
@RestController
@EnableAutoConfiguration
public class TestController {

    @Value("${source.dir}")
    private String dir;

    @RequestMapping("/")
    String home() {
        ZJPFileMonitor m;
        try {
            m = new ZJPFileMonitor(5000);
            m.monitor(dir, new ZJPFileListener());
            m.start();
            System.out.println("kaishi");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @RequestMapping("/hello/{myName}")
    String index(@PathVariable String myName) {
        return "Hello " + myName + "!!!";
    }
}
