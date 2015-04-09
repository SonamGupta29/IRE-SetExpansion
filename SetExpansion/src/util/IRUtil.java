package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;

import webdb.Web;

import concept.Stemmer;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

public class IRUtil {
	private static int totalDocs=0;
	public static final String UrlRegEx = " |\\?|\\.|/|:|-|\\+|%|=|&|\\$|,|_|;|\\(|\\)|\\{|\\}|\\[|\\]|&|%";
	public static final String Token = " |\\?|\\.|/|:|\\+|%|=|&|\n|\\$|,|_|;|\\(|\\)|\\{|\\}|\\[|\\]|&|%";
	static HashSet<String> stopWords = new HashSet<String>();
	static HashMap<String, Set<String>> urlToTokensMapping = new HashMap<>(); 
	static {
		BufferedReader reader=null;
		try{
			reader = new BufferedReader(new FileReader("stopwords"));
			stopWords.addAll( Arrays.asList(reader.readLine().split(",")));
			reader.close();
			LogUtil.log.info("No of StopWords :"+stopWords.size());
		} catch (FileNotFoundException e) {
			System.err.println("stopwords File Not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(reader != null)
				try{
					reader.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public static double getIdf(HashMap<String, Set<String>> docMap, Entry<String, Integer> e) {
		Set<String> uniqDocs = new HashSet<String>();
		if(totalDocs  == 0){
			for(Entry<String, Set<String>> en : docMap.entrySet()){
				uniqDocs.addAll(en.getValue());
			}
			totalDocs = uniqDocs.size();
		}
		double idf = Math.log(totalDocs/docMap.get(e.getKey()).size());

		//return tf*docMap.get(e.getKey()).size();
		return idf;
	}

	public static String clean() {
		return null;
	}

	public static double compareDocs(String url1, String url2) {
		String text1 = null,text2 = null;
		try {
			//text1 = ArticleExtractor.INSTANCE.getText(new URL(url1));
			text1 = ArticleExtractor.INSTANCE.getText(Web.getPageHtml(url1));
			text2 = ArticleExtractor.INSTANCE.getText(Web.getPageHtml(url2));
		}catch (BoilerpipeProcessingException e) {
			LogUtil.log.fine(e.toString());
		}
		
		if(text1==null || text2==null){
			return 0;
		}
		text1=text1.toLowerCase();
		text2=text2.toLowerCase();
		
		Set<String> uniqText1 = split(text1);
		Set<String> uniqText2 = split(text2);

		double intersection=ListUtil.getOverLapWithoutStopWords(uniqText1, uniqText2);
		//return 100.0*intersection/(double)Math.log((uniqText1.size()+uniqText2.size()));
		return intersection;///(double)Math.log((uniqText1.size()+uniqText2.size()));
	}
	
	public static double compareDocsWithSeed(String url1, String url2, ArrayList<String> seedList) {
		String text1 = null,text2 = null;
		/*try {
			//text1 = ArticleExtractor.INSTANCE.getText(new URL(url1));
			text1 = ArticleExtractor.INSTANCE.getText(Web.getPageHtml(url1));
			text2 = ArticleExtractor.INSTANCE.getText(Web.getPageHtml(url2));
		}catch (BoilerpipeProcessingException e) {
			LogUtil.log.fine(e.toString());
		}*/
		text1 = Jsoup.parse(Web.getPageHtml(url1)).text().toLowerCase();
		//System.out.println();
		text2 = Jsoup.parse(Web.getPageHtml(url2)).text().toLowerCase();
		
		if(text1==null || text2==null){
			return 0;
		}
		
		double intersection=0;
		Set<String> uniqText1, uniqText2;
		if(urlToTokensMapping.containsKey(url1)){
			uniqText1 = urlToTokensMapping.get(url1);
		} else {
			//uniqText1 =  new HashSet<String>(Arrays.asList(text1.split(IRUtil.Token)));
			uniqText1 = split(text1);
		}
		
		if(urlToTokensMapping.containsKey(url2)){
			uniqText2 = urlToTokensMapping.get(url2);
		} else {
			//uniqText2 = new HashSet<String>(Arrays.asList(text2.split(IRUtil.Token)));
			uniqText2 = split(text2);
		}
		for(String seed : seedList){
			if(text1.contains(seed) && text2.contains(seed)){  // ANd or OR ?
				intersection=ListUtil.getOverLapWithoutStopWords(uniqText1, uniqText2);
				return intersection;
			}	
		}
		
				//return 100.0*intersection/(double)Math.log((uniqText1.size()+uniqText2.size()));
		return intersection;///(double)Math.log((uniqText1.size()+uniqText2.size()));
	}

	public static boolean isValidWord(String smallWord) {
		//Matcher matcher = nonWordPattern.matcher(smallWord);
		//if(matcher.find() || stopWords.contains(smallWord) || smallWord.length()<2 ){
		int i=0;
		if(smallWord.length()<=2 || stopWords.contains(smallWord)){
			//System.out.println("This is Invalid Concept (stopword or not-word) : " + word);
			return false;
		}
		
		for(i=0; i<smallWord.length(); i++){
			if(!Character.isAlphabetic(smallWord.charAt(i)) && !Character.isDigit(smallWord.charAt(i))){
				return false;
			}
		}
		
		return true;
	}

	public static ArrayList<String> getUrltokens(String urlString){
		ArrayList<String> tokens = new ArrayList<String>();
		URI url = null;
		try {
			url = new URI(urlString);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(url.getPath()!=null){
			tokens.addAll(Arrays.asList(url.getPath().split(IRUtil.UrlRegEx)));
		}
		if(url.getQuery()!=null){
			tokens.addAll(Arrays.asList(url.getQuery().split(IRUtil.UrlRegEx)));
		}
		return tokens;
	}

	public String getStemmedWord(String word){
		Stemmer stemmer = new Stemmer();
		stemmer.add(word.toLowerCase().toCharArray(), word.length());
		stemmer.stem();
		return stemmer.toString();
	}
	
	
	public static HashSet<String> split(String text){
		
		HashSet<String> tokens = new HashSet<>();
		
		StringTokenizer tokenize = new StringTokenizer(text, IRUtil.Token);
		
		while(tokenize.hasMoreTokens()){
			tokens.add(tokenize.nextToken());
		}
		return tokens;
	}
}
