package dbcon;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
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
		db = mongoClient.getDB("web");
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
		System.out.println("Word is : " + word + "\n");
		query.put("word", word);
		DBObject obj = word2vecCollection.findOne(query);
		System.out.println("inside getvectors function\n");
		//System.out.println("query count = " + cur.count());
		if(obj != null){
			System.out.println("inside if block\n");
			@SuppressWarnings("unchecked")
			List<Double> obj1 = (List<Double>) obj.get("vectors");
			
			return obj1;
		}
		System.out.println("out of if condition\n");
		return null;
	}
}
