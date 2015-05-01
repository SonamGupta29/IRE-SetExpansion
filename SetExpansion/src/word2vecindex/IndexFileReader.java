package word2vecindex;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexFileReader {
	
	
	private static final String primaryIndexPath = "./index/secondaryIndex";
	private static final String largeIndexPath = "./index/primaryIndex";
	private static final String mainIndex = "./mainIndex";
	private static HashMap<String, Long> primaryIndexOffset = new HashMap<>();
	
	private static int indexFileCount = 4;
	
	public static void mergeIndexFiles(){
		
		int i=0;
		try {
			String line = "";
			File file = new File("./mainIndex");
			file.deleteOnExit();
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
			
			for(i=1; i<=indexFileCount; i++){
				
				BufferedReader bReader = new BufferedReader(new FileReader(largeIndexPath+i));
				while((line=bReader.readLine())!=null){
					
					bWriter.write(line+"\n");
				}
				
				bReader.close();
			}
			
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//Load primary index into memory
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
		
	}
	
	//Read vectors for word from file
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
			raf = new RandomAccessFile(mainIndex, "r");
			raf.seek(location);
			line = raf.readLine();
			int index = line.indexOf(":[");
			String vector = line.substring(index+2);
			vector = vector.substring(0, vector.length()-1);
			String vals[] = vector.split(",");
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
