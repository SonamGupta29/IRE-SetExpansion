package dbcon;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

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
		db = mongoClient.getDB("web2");
		webCollection = db.getCollection("urlCollection");
		searchCollection = db.getCollection("searchCollection");
		word2vecCollection = db.getCollection("word2vec");
	}
	
	public static void insertWordVectors(String word, double[] vectors){
		
		BasicDBObject document = new BasicDBObject();
		document.put("word", word);
		document.put("vectors", vectors);
        word2vecCollection.insert(document);
	}
	
	public static List<Double> getVectors(String word){
		
		BasicDBObject query = new BasicDBObject();
		query.put("word", word);
		DBCursor cur = word2vecCollection.find(query);
		if(cur.count() > 0){
			@SuppressWarnings("unchecked")
			List<Double> obj = (List<Double>) cur.next().get("vectors");
			
			return obj;
		}
		
		return null;
	}
}
