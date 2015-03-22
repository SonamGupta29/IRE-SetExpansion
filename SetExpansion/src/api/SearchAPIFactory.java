
package api;

import constants.SearchAPIConstants.APIType;

/**
 * @author mrugani
 *
 */
public class SearchAPIFactory {

	public static SearchAPI getSearchAPI(APIType type){
		
		SearchAPI searchAPI = null;
		
		switch (type) {
		case BING:
			//TO be implemented
			break;
		case GOOGLE:
			searchAPI = new GoogleSearchAPI();
			break;
		case TWITTER:
			//TO be implemented
			break;
		case WIKI:
			searchAPI = new WikiSearchAPI();
			break;
		case FAROO:
			searchAPI = new FarooSearchAPI();
		case WEBHOSE:
			searchAPI = new WebHoseSearchAPI();
		default:
			break;
		
		
		}
		
		return searchAPI;
	}
}
