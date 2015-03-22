import org.json.JSONException;

import constants.SearchAPIConstants.APIType;
import api.SearchAPI;
import api.SearchAPIFactory;



/**
 * @author mrugani
 *
 */
public class Main {

	public static void main(String[] args) throws JSONException {
		
		//Uncomment for testing wikisearch API
		
		/*SearchAPI searchAPI = SearchAPIFactory.getSearchAPI(APIType.WIKI);
		searchAPI.getTopURLs("mango orange", 50);*/
		/*SearchAPI searchAPI = SearchAPIFactory.getSearchAPI(APIType.FAROO);
		searchAPI.getTopURLs("c++ java",4);*/
		//Uncomment for testing google API
		
		/*SearchAPI searchAPI = SearchAPIFactory.getSearchAPI(APIType.GOOGLE);
		searchAPI.getTopURLs("c++ java", 100);*/
		
		SearchAPI searchAPI = SearchAPIFactory.getSearchAPI(APIType.DUCKDUCKGO);
		searchAPI.getTopURLs("java", 10);
	}
}
