package com.mongo.sofsit;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class Util {
    public static boolean checkIfCollectionExists(MongoDatabase database, String collectionName) {
        database.listCollectionNames().forEach(System.out::println);
//        for (String collection : collectionNames) {
//            System.out.println(collection);
//            if (collection.equalsIgnoreCase(collectionName)) {
//                return true;
//            }
//        }
        return false;
    }
}
