package com.keyfox.listener;

import com.keyfox.utils.FileUtils;
import com.keyfox.utils.MongoDBUtils;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import java.io.File;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.bson.Document;

/**
 * 文件监控
 * Created by Administrator on 2017/12/15.
 */
public class ZJPFileListener implements FileAlterationListener {


    public void onStart(FileAlterationObserver fileAlterationObserver) {
        System.out.println("onStart");
        String dbName = "test";
        String collName = "table";
        MongoCollection<Document> coll = MongoDBUtils.instance.createColl(dbName, collName);

        File files[] = FileUtils.getFileName("C:\\Users\\Administrator\\Desktop\\probe\\Total\\");
        if (files == null) {
            return;
        }
        for (File file : files) {
            MongoCursor<Document> cursor = coll.find(Filters.eq("tableName", file.getName()))
                .iterator();
            if (cursor.hasNext()) {
                continue;
            }
            Document doc = new Document();
            doc.append("tableName", file.getName());
            //数据状态 0未开始导入 1正在导入 2导入完成
            doc.append("state", 0);
            //导入起始行数
            doc.append("line", 1);
            coll.insertOne(doc);
        }
        foreachTable();
    }

    public void onDirectoryCreate(File file) {
        System.out.println(file.getAbsoluteFile());
    }

    public void onDirectoryChange(File file) {
        System.out.println(file.getAbsoluteFile());
    }

    public void onDirectoryDelete(File file) {
        System.out.println(file.getAbsoluteFile());
    }

    public void onFileCreate(File file) {
        System.out.println(file.getAbsoluteFile());
    }

    public void onFileChange(File file) {
        System.out.println("chage    " + file.getAbsoluteFile());

        String dbName = "test";
        String collName = "table";
        MongoCollection<Document> coll = MongoDBUtils.instance.createColl(dbName, collName);
        MongoCursor<Document> cursor = coll.find(Filters.eq("tableName", file.getName()))
            .iterator();
        if (!cursor.hasNext()) {
            return;
        }
        Document document = cursor.next();
        if (document.getInteger("state") != 2) {
            return;
        }
        //继续导入
        importData(coll, document);
    }

    public void onFileDelete(File file) {
        System.out.println(file.getAbsoluteFile());
    }

    public void onStop(FileAlterationObserver fileAlterationObserver) {
        System.out.println("stop");
    }

    private void foreachTable() {
        String dbName = "test";
        String collName = "table";
        MongoCollection<Document> coll = MongoDBUtils.instance.getCollection(dbName, collName);
        MongoCursor<Document> cursor = coll.find(Filters.eq("state", 1)).iterator();
        if (cursor.hasNext()) {
            importData(coll, cursor.next());
            return;
        }

        MongoCursor<Document> cursorExist = coll.find(Filters.eq("state", 0)).iterator();
        if (cursorExist.hasNext()) {
            Document newdoc = cursorExist.next();
            newdoc.put("state", 1);
            MongoDBUtils.instance.updateById(coll, newdoc.getObjectId("_id").toString(), newdoc);
            return;
        }

        //有文件变更后使用
        System.out.println("变更文件数据后的导入");
        MongoCursor<Document> cursorUpdate = coll.find().iterator();
        while (cursorUpdate.hasNext()){
            importData(coll, cursorUpdate.next());
            cursorUpdate.hasNext();
        }

    }

    private void importData(MongoCollection<Document> collTable, Document doc) {
        String dbName = "test";
        String collName = "total";
        MongoCollection<DBObject> coll = MongoDBUtils.instance.createCollObjeck(dbName, collName);

        System.out.println("===========================================正在导入  " + doc.getString("tableName") + "   数据");

        File file = new File(
            "C:\\Users\\Administrator\\Desktop\\probe\\Total\\" + doc.getString("tableName"));
        boolean flag = true;
        while (flag) {
            String data = FileUtils.readAppointedLineNumber(file, doc.getInteger("line"));
            if (data == null) {
                flag = false;
                continue;
            }
            coll.insertOne((DBObject) JSON.parse(data));
            System.out.println(data);
            //更新计数
            doc.append("line", doc.getInteger("line") + 1);
            MongoDBUtils.instance.updateById(collTable, doc.getObjectId("_id").toString(), doc);

        }
        doc.append("state", 2);
        MongoDBUtils.instance.updateById(collTable, doc.getObjectId("_id").toString(), doc);
    }

}
