package com.keyfox.controller;

import com.keyfox.listener.ZJPFileListener;
import com.keyfox.listener.ZJPFileMonitor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/hello")
public class TestController {


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("/list");
        return mav;
    }

    @RequestMapping("/dd")
    String home() {
        ZJPFileMonitor m;
        try {
            m = new ZJPFileMonitor(5000);
            m.monitor("", new ZJPFileListener());
            m.start();
            System.out.println("kaishi");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
