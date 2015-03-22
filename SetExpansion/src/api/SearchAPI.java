package api;

import org.json.JSONException;

public abstract class SearchAPI {
	
	//The return type needs to be changed
	public abstract void  getTopURLs(String query, int n) throws JSONException;
}
