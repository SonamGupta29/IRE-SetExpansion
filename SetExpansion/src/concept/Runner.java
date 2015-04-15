package concept;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import word2vec.Word2VecModel;

public class Runner {

	/**
	 * @param args
	 * @throws IOException
	 * @throws MalformedURLException
	 * args[0] --- no of results
	 * args[1] --- Input file containing seed words
	 * args[2] --- Output file
	 */
	public static void main(String[] args) throws IOException {
		
		int noOfResults = Integer.parseInt(args[0]);

		BufferedReader reader = null;
		FileWriter writer = null;
		String line;
		ArrayList<String> seedList = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(args[1]));
			writer = new FileWriter(args[2]);
			while ((line = reader.readLine()) != null && !line.equals("")) {
				seedList.addAll(Arrays.asList(line.toLowerCase().split(" ")));
				Word2VecModel.expandSet(seedList, noOfResults);
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

	
}
