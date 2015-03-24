package api;

import java.util.List;

import api.BingSearchAPI.results;

public abstract class SearchAPI {
	
	//The return type needs to be changed
	public abstract List<results>  getTopURLs(String query, int n);
}
