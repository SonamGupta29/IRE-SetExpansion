package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;

import concept.Stemmer;

public class IRUtil {
	public static final String Token = " |\\?|\\.|/|:|\\+|%|=|&|\n|\\$|,|_|;|\\(|\\)|\\{|\\}|\\[|\\]|&|%";
	static HashSet<String> stopWords = new HashSet<String>();
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




	public static boolean isValidWord(String smallWord) {
		//Matcher matcher = nonWordPattern.matcher(smallWord);
		//if(matcher.find() || stopWords.contains(smallWord) || smallWord.length()<2 ){
		int i=0;
		if(smallWord.length()<=2 || stopWords.contains(smallWord)){
			//System.out.println("This is Invalid Concept (stopword or not-word) : " + word);
			return false;
		}
		
		for(i=0; i<smallWord.length(); i++){
			if(!Character.isAlphabetic(smallWord.charAt(i))){
				return false;
			}
		}
		
		return true;
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
