
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
			searchAPI = new BingSearchAPI();
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
		case DUCKDUCKGO:
			searchAPI = new DuckDuckGoSearchAPI();
		case STACKOVERFLOW:
			searchAPI = new StackOverflowSearchAPI();
		default:
			break;
		
		
		}
		
		return searchAPI;
	}
}
