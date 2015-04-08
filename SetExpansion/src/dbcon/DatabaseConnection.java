package dbcon;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class DatabaseConnection {

	private static MongoClient mongoClient;
	private static DB db;
	private static DBCollection word2vecCollection;
	private static DBCollection searchCollection;
	private static DBCollection webCollection;
	
	static {
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongoClient.getDB("web");
		webCollection = db.getCollection("urlCollection");
		searchCollection = db.getCollection("searchCollection");
		word2vecCollection = db.getCollection("word2vec");
	}
	
	public static void insertWordVectors(String word, double[] vectors){
		
		BasicDBObject document = new BasicDBObject();
		document.put("word", word);
		document.put("vectors", vectors);
        WriteResult result = word2vecCollection.insert(document);
	}
}
