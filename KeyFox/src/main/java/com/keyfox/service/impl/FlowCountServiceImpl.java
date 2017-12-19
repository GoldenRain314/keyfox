package com.keyfox.service.impl;

import com.keyfox.entity.LineDiagramEntity;
import com.keyfox.service.FlowCountService;
import com.keyfox.utils.MongoDBUtils;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bson.Document;
import org.springframework.stereotype.Service;

/**
 * 获取流量实现
 * Created by Administrator on 2017/12/19.
 */
@Service("flowCountService")
public class FlowCountServiceImpl implements FlowCountService {

    String dbName = "test";
    String collName = "total";

    @Override
    public LineDiagramEntity selectFlowCount(String date) {
        MongoCollection<Document> coll = MongoDBUtils.instance.getCollection(dbName, collName);
        Document query = new Document();
        query.append("CulTime.TimeStamp", new Document()
            .append("$gte", date + " 00:00:00")
            .append("$lte", date + " 23:59:59")
        );

        List<Object[]> udp = new ArrayList<>();
        List<Object[]> ipv4 = new ArrayList<>();
        List<Object[]> tcp = new ArrayList<>();
        List<Object[]> ipv6 = new ArrayList<>();

        Block<Document> processBlock = document -> {
            Object[] objects = new Object[2];
            Document aa = (Document) document.get("CulTime");
            objects[0] = aa.get("TimeStamp");
            Document bb = (Document) document.get("ConnectInfor");
            objects[1] = bb.getDouble("ConnectNum_UDP");
            udp.add(objects);

            Object[] objects2 = new Object[2];
            objects2[0] = aa.get("TimeStamp");
            objects2[1] = bb.getDouble("PacketNum_IPv4");
            ipv4.add(objects2);

            Object[] objects3 = new Object[2];
            objects3[0] = aa.get("TimeStamp");
            objects3[1] = bb.getDouble("ConnectNum_TCP");
            tcp.add(objects3);

            Object[] objects4 = new Object[2];
            objects4[0] = aa.get("TimeStamp");
            objects4[1] = bb.getDouble("PacketNum_IPv6");
            ipv6.add(objects4);
        };
        coll.find(query).forEach(processBlock);

        LineDiagramEntity lineDiagramEntity = new LineDiagramEntity();
        lineDiagramEntity.setUDP(udp.toArray());
        lineDiagramEntity.setIPV4(ipv4.toArray());
        lineDiagramEntity.setTCP(tcp.toArray());
        lineDiagramEntity.setIPV6(ipv6.toArray());
        return lineDiagramEntity;
    }

    @Override
    public JSONArray selectFlow(String date) {
        MongoCollection<Document> coll = MongoDBUtils.instance.getCollection(dbName, collName);
        Document query = new Document();
        query.append("CulTime.TimeStamp", new Document()
            .append("$gte", date + " 00:00:00")
            .append("$lte", date + " 23:59:59")
        );

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;

        List<Object[]> udp = new ArrayList<>();
        List<Object[]> ipv4 = new ArrayList<>();
        List<Object[]> tcp = new ArrayList<>();
        List<Object[]> ipv6 = new ArrayList<>();

        Block<Document> processBlock = document -> {
            Object[] objects = new Object[2];
            Document aa = (Document) document.get("CulTime");
            objects[0] = dateToStamp(aa.get("TimeStamp").toString());
            Document bb = (Document) document.get("ConnectInfor");
            objects[1] = bb.getDouble("ConnectNum_UDP");
            udp.add(objects);

            Object[] objects2 = new Object[2];
            objects2[0] = dateToStamp(aa.get("TimeStamp").toString());
            objects2[1] = bb.getDouble("PacketNum_IPv4");
            ipv4.add(objects2);

            Object[] objects3 = new Object[2];
            objects3[0] = dateToStamp(aa.get("TimeStamp").toString());
            objects3[1] = bb.getDouble("ConnectNum_TCP");
            tcp.add(objects3);

            Object[] objects4 = new Object[2];
            objects4[0] = dateToStamp(aa.get("TimeStamp").toString());
            objects4[1] = bb.getDouble("PacketNum_IPv6");
            ipv6.add(objects4);
        };
        coll.find(query).forEach(processBlock);

        jsonObject= new JSONObject();
        jsonObject.put("name","UDP");
        jsonObject.put("data",udp.toArray());
        jsonArray.add(jsonObject);

        jsonObject= new JSONObject();
        jsonObject.put("name","IPV4");
        jsonObject.put("data",ipv4.toArray());
        jsonArray.add(jsonObject);

        jsonObject= new JSONObject();
        jsonObject.put("name","TCP");
        jsonObject.put("data",tcp.toArray());
        jsonArray.add(jsonObject);

        jsonObject= new JSONObject();
        jsonObject.put("name","IPV6");
        jsonObject.put("data",ipv6.toArray());
        jsonArray.add(jsonObject);

        return jsonArray;
    }
    private Long dateToStamp(String s){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
