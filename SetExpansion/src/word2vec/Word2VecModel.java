
package word2vec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.jsoup.Jsoup;

import parser.ListFinderHTML;
import parser.WebPage;
import util.IRUtil;
import util.ListUtil;
import util.SearchProvider;
import webdb.Web;
import dbcon.DatabaseConnection;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

/**
 * @author mrugani
 * 
 */
public class Word2VecModel {

	static ArrayList<WebPage> seedPages = new ArrayList<WebPage>();
	static HashMap<String, List<Double>> seedVectors = new HashMap<>();
	
	/**
	 * @param seedList
	 * @param noOfResults
	 */
	public static void expandSet(ArrayList<String> seedList, int noOfResults) {

		long startTime = System.currentTimeMillis();
		getSeedVectors(seedList);
		ArrayList<WebPage> results = SearchProvider.getURLs(seedList);
		HashMap<String, Double> distance = new HashMap<>();
		double d = 0.0;
		ArrayList<String> listTokens = new ArrayList<>();
		ListFinderHTML listFinder = new ListFinderHTML();
		for (WebPage page : results) {

			String html = Web.getPageHtml(page.getUrl());
			String text = "";
			text = Jsoup.parse(html).text().toLowerCase();

			HashSet<String> tokens = IRUtil.split(text);
			listFinder.setMyHTML(html.toLowerCase());
			ArrayList<String> webList = new ArrayList<String>();
			while ((webList = listFinder.getNextList()) != null) {
				if (webList.size() > 0) {
					if (ListUtil.getOverLap(webList, seedList) >= 3) {
						listTokens.addAll(webList);
					}
				}
			}
			for (String word : tokens) {
				d = 0;
				if (IRUtil.isValidWord(word) && !seedList.contains(word)
						&& !distance.containsKey(word)) {
					for (String seedWord : seedList) {
						d += cosineDistance(word, seedWord);
					}
				}
				if (d > 0.0) {
					if (listTokens.contains(word)) {
						d = d + 5; // Add an additional weight
					}
					distance.put(word, d);
				}
			}

			listTokens.clear();

		}
		
		//Sort the output distance array
		Set<Entry<String, Double>> set = distance.entrySet();
		List<Entry<String, Double>> list = new ArrayList<>(set);
		Collections.sort(list, new Comparator<Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> a, Entry<String, Double> b) {
				if (a.getValue() > b.getValue()) {
					return -1;
				} else
					return 1;
			}
		});

		for (int i = 0; i < noOfResults && i < list.size(); i++) {
			System.out.println(list.get(i));
		}

		long endTime = System.currentTimeMillis();

		System.out.println("Total time taken: " + (endTime - startTime) / 1000);

	}

	private static void getSeedVectors(ArrayList<String> seedList) {

		int i = 0, len = seedList.size();
		for (i = 0; i < len; i++) {
			List<Double> seedVector = DatabaseConnection.getVectors(seedList
					.get(i));
			seedVectors.put(seedList.get(i), seedVector);
		}

	}

	public static double cosineDistance(String s1, String s2) {

		List<Double> v1 = DatabaseConnection.getVectors(s1);
		List<Double> v2 = DatabaseConnection.getVectors(s2);
		if (v1 == null || v2 == null) {
			return 0.0;
		}
		return calculateDistance(v1, v2);
	}

	private static double calculateDistance(List<Double> otherVec,
			List<Double> vec) {
		double d = 0;
		for (int a = 0; a < 200; a++)
			d += vec.get(a) * otherVec.get(a);
		return d;
	}

}
