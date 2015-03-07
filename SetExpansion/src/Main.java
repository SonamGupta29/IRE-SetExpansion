import constants.SearchAPIConstants.APIType;
import api.SearchAPI;
import api.SearchAPIFactory;



/**
 * @author mrugani
 *
 */
public class Main {

	public static void main(String[] args) {
		
		//Uncomment for testing wikisearch API
		
		/*SearchAPI searchAPI = SearchAPIFactory.getSearchAPI(APIType.WIKI);
		searchAPI.getTopURLs("c++ java", 100);*/
		
		//Uncomment for testing google API
		
		/*SearchAPI searchAPI = SearchAPIFactory.getSearchAPI(APIType.GOOGLE);
		searchAPI.getTopURLs("c++ java", 100);*/
	}
}
