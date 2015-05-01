package concept;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import word2vec.Word2VecModel;
import word2vecindex.IndexFileReader;

public class Runner {

	/**
	 * @param args
	 * @throws IOException
	 * @throws MalformedURLException
	 * args[0] --- no of results
	 * args[1] --- Input file containing seed words
	 */
	public static void main(String[] args) throws IOException {
		
		int noOfResults = Integer.parseInt(args[0]);
		IndexFileReader.mergeIndexFiles();
		BufferedReader reader = null;
		String line;
		IndexFileReader.initPrimaryIndex();
		ArrayList<String> seedList = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(args[1]));
			while ((line = reader.readLine()) != null && !line.equals("")) {
				seedList.addAll(Arrays.asList(line.toLowerCase().split(" ")));
				Word2VecModel.expandSet(seedList, noOfResults);
			}
			reader.close();
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
	
	public static ArrayList<String> getResults(ArrayList<String> seedList, int nResults){
		
		IndexFileReader.initPrimaryIndex();
		return Word2VecModel.expandSet(seedList, nResults);
	}

	
}
