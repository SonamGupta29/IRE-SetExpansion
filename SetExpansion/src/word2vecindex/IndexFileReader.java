package word2vecindex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;

public class IndexFileReader {
	
	
	private static final String primaryIndexPath = "";
	private static final String largeIndexPath = "";

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
				
				long wordOffset = Integer.parseInt(loc);
				
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
		try{
			raf = new RandomAccessFile(largeIndexPath, "r");
			raf.seek(location);
			line = raf.readLine();
			System.out.println(line);
			raf.close();
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}
}
