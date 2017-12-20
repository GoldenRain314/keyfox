package com.keyfox.controller;

import com.keyfox.entity.LineDiagramEntity;
import com.keyfox.listener.ZJPFileListener;
import com.keyfox.listener.ZJPFileMonitor;
import com.keyfox.service.FlowCountService;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/hello")
public class TestController {

    @Resource
    private FlowCountService flowCountService;

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

    @RequestMapping(value = "/dataJson")
    @ResponseBody
    public JSONObject dataJson() {
        LineDiagramEntity lineDiagramEntity = flowCountService.selectFlowCount("2017-12-10");
        JSONObject jsonObject = JSONObject.fromObject(lineDiagramEntity);
       //System.out.println(jsonObject);
        return jsonObject;
    }

    @RequestMapping(value = "/dataJsonLine")
    @ResponseBody
    public JSONArray dataJsonLine() {
        JSONArray lineDiagramEntity = flowCountService.selectFlow("2017-12-10");
        //System.out.println(jsonObject);
        return lineDiagramEntity;
    }

    @RequestMapping(value = "/dataJsonThemeRiver")
    @ResponseBody
    public JSONArray dataJsonThemeRiver() {
        JSONArray lineDiagramEntity = flowCountService.themeRiver("2017-12-10");
        System.out.println(lineDiagramEntity);
        return lineDiagramEntity;
    }

    @RequestMapping(value = "/list2", method = RequestMethod.GET)
    public ModelAndView list2() {
        ModelAndView mav = new ModelAndView("/list2");
        return mav;
    }
}
