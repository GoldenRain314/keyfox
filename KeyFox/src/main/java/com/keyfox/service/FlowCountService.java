package com.keyfox.service;

import com.keyfox.entity.LineDiagramEntity;
import net.sf.json.JSONArray;

/**
 * 获取流量
 * Created by Administrator on 2017/12/19.
 */
public interface  FlowCountService {

    /**
     * 获取总流量
     */
    LineDiagramEntity selectFlowCount(String date);

    JSONArray selectFlow(String date);
}
