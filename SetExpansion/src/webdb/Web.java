package webdb;

import java.net.UnknownHostException;
import java.util.ArrayList;

import util.LogUtil;
import util.SearchProvider;

import Parser.WebPage;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class Web {
	private static MongoClient mongoClient;
	private static DB db;
	private static DBCollection webCollection;
	private static DBCollection searchCollection;

	static {
		try {
			mongoClient = new MongoClient();	//Driver for accessing mongo- database
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongoClient.getDB("web");			//Accessing database named "web"
		webCollection = db.getCollection("urlCollection");	//Accessing table named urlCollection
		searchCollection = db.getCollection("searchCollection");	////Accessing table named searchCollection
	}

	private static void insert(String url, String html) {
		try {
			BasicDBObject obj = new BasicDBObject("url", url).append("html",
					html);
			webCollection.insert(obj);		//Inseting an object into urlCollection table
		} catch (Exception e) {
			
			System.out.println("Error during insertion of url " + url);
			System.out.println("MongoDB: error occured during insertion");
			//e.printStackTrace();

		}
	}

	public static ArrayList<WebPage> getSearchResults(
			ArrayList<String> seedList, String concept, int noOfResults,
			double overlapTolerance) {
		BasicDBObject dbquery = new BasicDBObject();
		String query = SearchProvider.constructQuery(seedList, concept);
		System.out.println("Searching for query " + query);
		dbquery.put("query", query);
		DBCursor cur = searchCollection.find(dbquery);	//Finding records in table searchCollection. cur is pointer to db record
		Gson gson = new Gson();

		if (cur.count() > 0) {						//Check if record is present in table.
			JsonParser p = new JsonParser();		//Mongo db stores data in the form of "json". Use jsonparser to access the data
			SearchResult result = gson.fromJson(p.parse(cur.next().toString()),
					SearchResult.class);	//Check documentation of gson jar. You will understand
			return result.getResults();
		} else {							//If there is no record with the query, fetch URLs from using APIs
			ArrayList<WebPage> result = SearchProvider.bingSearch(seedList,
					concept, noOfResults, overlapTolerance);
			LogUtil.log.fine("Going to Bing for " + query + " got "
					+ result.size() + " results");
			SearchResult sr = new SearchResult(query, result);
			DBObject obj = (DBObject) JSON.parse(gson.toJson(sr));	
			searchCollection.save(obj);			//Write back the fetched URLs into DB.. Again in the form of json
			return result;
		}
	}

	public static String getPageHtml(String url) {
		BasicDBObject query = new BasicDBObject();		//Check if the html page for given url is present in DB..
		query.put("url", url);
		DBCursor cur = webCollection.find(query);

		if (cur.count() > 0) {
			return (String) cur.next().get("html");
		} else {
			String html = SearchProvider.getHtml(url);
			insert(url, html);
			return html;
		}
	}
}
