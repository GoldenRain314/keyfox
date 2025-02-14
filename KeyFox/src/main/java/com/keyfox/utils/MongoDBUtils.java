package com.keyfox.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了<br>
 * 注意Mongo已经实现了连接池，并且是线程安全的。 <br>
 * 设计为单例模式， 因 MongoDB的Java驱动是线程安全的，对于一般的应用，只要一个Mongo实例即可，<br>
 * Mongo有个内置的连接池（默认为10个） 对于有大量写和读的环境中，为了确保在一个Session中使用同一个DB时，<br>
 * DB和DBCollection是绝对线程安全的<br>
 */
public enum MongoDBUtils {

    /**
     * 定义一个枚举的元素，它代表此类的一个实例
     */
    instance;

    private MongoClient mongoClient;

    static {
        // 从配置文件中获取属性值
        String ip = "localhost";
        int port = 27017;
        instance.mongoClient = new MongoClient(ip, port);

        // or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
        // List<ServerAddress> listHost = Arrays.asList(new ServerAddress("localhost", 27017),new ServerAddress("localhost", 27018));
        // instance.mongoClient = new MongoClient(listHost);

        // 大部分用户使用mongodb都在安全内网下，但如果将mongodb设为安全验证模式，就需要在客户端提供用户名和密码：
        // boolean auth = db.authenticate(myUserName, myPassword);
        Builder options = new Builder();
        // options.autoConnectRetry(true);// 自动重连true
        // options.maxAutoConnectRetryTime(10); // the maximum auto connect retry time
        options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
        options.connectTimeout(15000);// 连接超时，推荐>3000毫秒
        options.maxWaitTime(5000); //
        options.socketTimeout(0);// 套接字超时时间，0无限制
        options.threadsAllowedToBlockForConnectionMultiplier(
            5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
        options.writeConcern(WriteConcern.SAFE);//
        options.build();
    }

    // ------------------------------------共用方法---------------------------------------------------

    /**
     * 获取DB实例 - 指定DB
     */
    public MongoDatabase getDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            return mongoClient.getDatabase(dbName);
        }
        return null;
    }

    /**
     * 获取collection对象 - 指定Collection
     */
    public MongoCollection<Document> getCollection(String dbName, String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        return mongoClient.getDatabase(dbName).getCollection(collName);
    }

    /**
     * 查询DB下的所有表名
     */
    public List<String> getAllCollections(String dbName) {
        MongoIterable<String> colls = getDB(dbName).listCollectionNames();
        List<String> _list = new ArrayList<>();
        for (String s : colls) {
            _list.add(s);
        }
        return _list;
    }

    /**
     * 获取所有数据库名称列表
     */
    public MongoIterable<String> getAllDBNames() {
        return mongoClient.listDatabaseNames();
    }

    /**
     * 删除一个数据库
     */
    public void dropDB(String dbName) {
        getDB(dbName).drop();
    }

    /**
     * 查找对象 - 根据主键_id
     */
    public Document findById(MongoCollection<Document> coll, String id) {
        ObjectId _idobj;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        return coll.find(Filters.eq("_id", _idobj)).first();
    }

    /**
     * 统计数
     */
    public int getCount(MongoCollection<Document> coll) {
        return (int) coll.count();
    }

    /**
     * 条件查询
     */
    public MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
        return coll.find(filter).iterator();
    }

    /**
     * 分页查询
     */
    public MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo,
        int pageSize) {
        Bson orderBy = new BasicDBObject("_id", 1);
        return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize)
            .iterator();
    }

    /**
     * 通过ID删除
     */
    public int deleteById(MongoCollection<Document> coll, String id) {
        int count;
        ObjectId _id;
        try {
            _id = new ObjectId(id);
        } catch (Exception e) {
            return 0;
        }
        Bson filter = Filters.eq("_id", _id);
        DeleteResult deleteResult = coll.deleteOne(filter);
        count = (int) deleteResult.getDeletedCount();
        return count;
    }

    /**
     * FIXME
     */
    public Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
        ObjectId _idobj;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Bson filter = Filters.eq("_id", _idobj);
        // coll.replaceOne(filter, newdoc); // 完全替代
        coll.updateOne(filter, new Document("$set", newdoc));
        return newdoc;
    }

    public void dropCollection(String dbName, String collName) {
        getDB(dbName).getCollection(collName).drop();
    }


    /**
     * 关闭Mongodb
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }

    public MongoCollection<Document> createColl(String dbName,String collName){
        MongoDatabase db = mongoClient.getDatabase(dbName);
        return db.getCollection(collName);
    }
    public MongoCollection<DBObject> createCollObjeck(String dbName, String collName){
        MongoDatabase db = mongoClient.getDatabase(dbName);
        return db.getCollection(collName, DBObject.class);
    }

    /**
     * 测试入口
     */
    public static void main(String[] args) {

        String dbName = "test";
        String collName = "table";
        MongoCollection<Document> coll = MongoDBUtils.instance.getCollection(dbName, collName);


        // 插入多条
        // for (int i = 1; i <= 4; i++) {
        // Document doc = new Document();
        // doc.put("name", "zhoulf");
        // doc.put("school", "NEFU" + i);
        // Document interests = new Document();
        // interests.put("game", "game" + i);
        // interests.put("ball", "ball" + i);
        // doc.put("interests", interests);
        // coll.insertOne(doc);
        // }

        /// // 根据ID查询
//         String id = "5a33bdd41b5a5288aa77f0d1";
//         Document doc = MongoDBUtil.instance.findById(coll, id);
//         System.out.println(doc);

        // 查询多个
//         MongoCursor<Document> cursor1 = coll.find(Filters.eq("x", 10.0)).iterator();
//         while (cursor1.hasNext()) {
//         org.bson.Document _doc = cursor1.next();
//         System.out.println(_doc.toString());
//         }
//         cursor1.close();

        // 查询多个
        // MongoCursor<Person> cursor2 = coll.find(Person.class).iterator();

        // 删除数据库
        // MongoDBUtil2.instance.dropDB("testdb");

        // 删除表
        // MongoDBUtil2.instance.dropCollection(dbName, collName);

        // 修改数据
        // String id = "556949504711371c60601b5a";
        // Document newdoc = new Document();
        // newdoc.put("name", "时候");
        // MongoDBUtil.instance.updateById(coll, id, newdoc);

        // 统计表
        //System.out.println(MongoDBUtil.instance.getCount(coll));

        // 查询所有
        //Bson filter = Filters.eq("count", 0);
        //MongoDBUtil.instance.find(coll, filter);

    }

}
