package concept;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Jsoup;

import util.IRUtil;
import util.SearchProvider;
import webdb.Web;
import Parser.WebPage;
import dbcon.DatabaseConnection;

public class Runner {

	static int fileCount = 1;
	final int seedThrehold = 10;
	static ArrayList<WebPage> seedPages = new ArrayList<WebPage>();
	static HashMap<String, Double> finalSeedScores = new HashMap<String, Double>();
	static HashMap<String, List<Double>> seedVectors = new HashMap<>();

	/**
	 * @param args
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws IOException {
		int noOfResults = Integer.parseInt("10");

		BufferedReader reader = null;
		FileWriter writer = null;
		String line;
		ArrayList<String> seedList = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(args[0]));
			writer = new FileWriter(args[1]);
			while ((line = reader.readLine()) != null && !line.equals("")) {
				seedList.addAll(Arrays.asList(line.toLowerCase().split(" ")));
				expandSet(seedList, noOfResults);
			}
			reader.close();
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void expandSet(ArrayList<String> seedList, int noOfResults) {
		
		ArrayList<WebPage> results = SearchProvider.getURLs(seedList);
		HashMap<String, Double> distance = new HashMap<>();
		getSeedVectors(seedList);
		double d = 0.0;
		for(WebPage page : results){
			
			String text = Jsoup.parse(Web.getPageHtml(page.getUrl())).text().toLowerCase();
			
			HashSet<String> tokens = IRUtil.split(text);
			
			for(String word : tokens){
				d=0;
				if(!IRUtil.isValidWord(word) && !seedList.contains(word) && !distance.containsKey(word)){
					
					for(String seedWord: seedList){
						
						d += cosineDistance(word, seedWord);
					}
				}
				distance.put(word, d);
			}
			
			
		}
		
		Set<Entry<String, Double>> set = distance.entrySet();
		List<Entry<String, Double>> list = new ArrayList<>(set);
		Collections.sort(list, new Comparator<Entry<String, Double>>() {
	        @Override
	        public int compare(Entry<String, Double> a,
	                Entry<String, Double> b) {
	            return (int) (b.getValue() - a.getValue());
	        }
	    });
		
		for (int i = 0; i < 10 && i < list.size(); i++) {
		    System.out.println(list.get(i));
		}
	}
	
	private static void getSeedVectors(
			ArrayList<String> seedList) {
		
		int i=0, len = seedList.size();
		for(i=0; i<len; i++){
			
			List<Double> seedVector = DatabaseConnection.getVectors(seedList.get(i));
			seedVectors.put(seedList.get(i), seedVector);
		}
		
	}

	public static double cosineDistance(String s1, String s2){
		
		List<Double> v1 = DatabaseConnection.getVectors(s1);
		List<Double> v2 = seedVectors.get(s2);
		if(v1==null || v2== null){
			return 0;
		}
		return calculateDistance(v1, v2);
	}
	
	private static double calculateDistance(List<Double> otherVec, List<Double> vec) {
		double d = 0;
		for (int a = 0; a < 300; a++)
			d += vec.get(a) * otherVec.get(a);
		return d;
	}
}
