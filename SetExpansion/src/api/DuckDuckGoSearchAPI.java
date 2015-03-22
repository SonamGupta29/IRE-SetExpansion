package api;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.*;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import api.GoogleResults.ResponseData;
import api.GoogleResults.Result;

import com.google.gson.Gson;

public class DuckDuckGoSearchAPI extends SearchAPI{

	
	HttpClient httpClient;
	String url = "http://api.duckduckgo.com/?q=";
	
	public DuckDuckGoSearchAPI() {
		httpClient = new DefaultHttpClient();
		HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);
	}
	
	@Override
	public void getTopURLs(String query, int n) throws JSONException {
		try {
			query = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		HttpGet httpget = new HttpGet(url + query + "&format=json");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			
			String responseBody = httpClient.execute(httpget,
					responseHandler);
			InputStream is = new ByteArrayInputStream(
					responseBody.getBytes());
			/*System.out.println(is);*/
			Reader reader = new InputStreamReader(is);
			//System.out.println(reader);
			BufferedReader br=new BufferedReader(reader);
			String line="";
			String jsonData="";
			try{
				
				while((line=br.readLine())!=null)
				{
					jsonData+=line+"\n";
				}
				
			}catch(IOException e){
				e.printStackTrace();
			}finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			JSONObject obj=new JSONObject(jsonData);
			obj = new JSONObject(jsonData);
			//System.out.println(obj);
			/*for(int i = 0; i<obj.names().length(); i++){
			    System.out.println( "key = " + obj.names().getString(i) + " value = " + obj.get(obj.names().getString(i)));
			}*/
			/*System.out.println(obj);*/
			for(int i = 0 ; i < obj.length() ; i++){
				System.out.println(obj.getString("AbstractURL"));
			    //list.add(array.getJSONObject(i).getString("interestKey"));
			}
			JSONObject obj111 = new JSONObject(obj);
			System.out.println(obj);
			/*List<String> list = new ArrayList<String>();
			JSONArray array = obj111.getJSONArray("1");
			for(int i = 0 ; i < array.length() ; i++){
			    list.add(array.getJSONObject(i).getString("AbstractURL"));
			}
			System.out.println(list);*/
			
			/*DuckResults results = new Gson().fromJson(reader,
					DuckResults.class);
			System.out.println("Size of results: "
					+ results.getResponseData().getResults().size());
			for (int j = 0; j <= 3; j++) {
				System.out.println("Title: "
						+ results.getResponseData().getResults().get(j)
								.getTitle());
				System.out.println("URL: "
						+ results.getResponseData().getResults().get(j)
								.getUrl() + "\n");
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

/*class DuckResults {
	private ResponseData responseData;
	public ResponseData getResponseData() {
		return responseData;
	}
	public void setResponseData(ResponseData responseData) {
		this.responseData = responseData;
	}
	public String toString() {
		return "ResponseData[" + responseData + "]";
	}
	static class ResponseData {
		private List<Result> results;
		public List<Result> getResults() {
			return results;
		}
		public void setResults(List<Result> results) {
			this.results = results;
		}
		public String toString() {
			return "Results[" + results + "]";
		}
	}
	static class Result {
		private String AbstractURL;
		private String Abstract;

		public String getUrl() {
			return AbstractURL;
		}

		public String getTitle() {
			return Abstract;
		}

		public void setUrl(String url) {
			this.AbstractURL = url;
		}

		public void setTitle(String title) {
			this.Abstract = title;
		}

		public String toString() {
			return "Result[url:" + AbstractURL + ",title:" + Abstract + "]";
		}
	}
}
*/