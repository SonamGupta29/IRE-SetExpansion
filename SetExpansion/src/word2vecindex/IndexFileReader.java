package word2vecindex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexFileReader {
	
	
	private static final String primaryIndexPath = "/home/jarvis/Desktop/major project_ire/primaryindex";
	private static final String largeIndexPath = "/home/jarvis/Desktop/major project_ire/largeindex_sorted";

	private static HashMap<String, Long> primaryIndexOffset = new HashMap<>();
	
	public static void initPrimaryIndex(){
		
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(primaryIndexPath));
			String line = null;
			while((line=bReader.readLine())!=null){
				
				int offset = line.indexOf(" ");
				String word = line.substring(0,offset);
				String loc = line.substring(offset+1);
				
				long wordOffset = Long.parseLong(loc);
				
				primaryIndexOffset.put(word, wordOffset);
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		finally{
			
			if(bReader!=null){
				try {
					bReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//bReader = new BufferedReader(new );		
	}
	public static List<Double> getVectors(String word){
		
		Long location = primaryIndexOffset.get(word);
		List<Double> vectors = null;
		if(location!=null){
			
			vectors = readLargeIndex(location);
		}
		
		return vectors;
		
	}
	
	
	private static List<Double> readLargeIndex(Long location){
		
		RandomAccessFile raf;
		String line;
		List<Double> resultVectors = new ArrayList<>();
		try{
			raf = new RandomAccessFile(largeIndexPath, "r");
			raf.seek(location);
			line = raf.readLine();
			int index = line.indexOf(":[");
			String vector = line.substring(index+2);
			vector = vector.substring(0, vector.length()-1);
			String vals[] = vector.split(",");
			System.out.println(vals.length);
			int i=0, len = vals.length;
			for(i=0; i<len; i++){
				
				resultVectors.add(Double.parseDouble(vals[i]));
			}
			raf.close();
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		return resultVectors;
	}
}
