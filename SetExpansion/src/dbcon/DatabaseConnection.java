package dbcon;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class DatabaseConnection {

	private static MongoClient mongoClient;
	private static DB db;
	private static DBCollection word2vecCollection;
	
	static {
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongoClient.getDB("web2");
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
		DBObject obj = word2vecCollection.findOne(query);
		if(obj != null){
			@SuppressWarnings("unchecked")
			List<Double> obj1 = (List<Double>) obj.get("vectors");
			
			return obj1;
		}
		return null;
	}
}
