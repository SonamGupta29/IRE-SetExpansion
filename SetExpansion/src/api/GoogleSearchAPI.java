package api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import api.BingSearchAPI.results;
import api.GoogleResults.googleRes;

import com.google.gson.Gson;

public class GoogleSearchAPI extends SearchAPI {

	HttpClient httpClient;
	// String url =
	// "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=";
	String url = "https://www.googleapis.com/customsearch/v1?key=AIzaSyCjgdkMJWhKWLLkYcphniaR271MoTUO3P8&cx=012132730063510152485:eijpvngnmic&q=";
	List<results> googleResults = new ArrayList<>();

	public GoogleSearchAPI() {
		httpClient = new DefaultHttpClient();
		HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);
	}

	public List<results> getTopURLs_Old(String query, int n) {

		try {
			query = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		System.out.println("Getting google results for query=" + query);
		int len = 0;
		for (int i = 0; i < n; i = i + 4) {

			HttpGet httpget = new HttpGet(url + i + "&q=" + query);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			try {
				Thread.sleep(1000);
				String responseBody = httpClient.execute(httpget,
						responseHandler);
				InputStream is = new ByteArrayInputStream(
						responseBody.getBytes());
				Reader reader = new InputStreamReader(is);
				GoogleResults results = new Gson().fromJson(reader,
						GoogleResults.class);
				if (results != null) {
					len = results.getResponseData().getResults().size();
				} else {
					len = 0;
				}
				for (int j = 0; j < len; j++) {

					results r = new results();
					r.Title = results.getResponseData().getResults().get(j)
							.getTitle();
					r.Url = results.getResponseData().getResults().get(j)
							.getUrl();
					r.Description = null;

					googleResults.add(r);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return googleResults;

	}

	@Override
	public List<results> getTopURLs(String query, int n) {

		try {
			query = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		System.out.println("Getting google results for query=" + query);
		int len = 0;
		int i = 0;
		for (i = 0; i <=n; i++) {
			
			HttpGet httpget = new HttpGet(url + query + "&startIndex=" + i);
			System.out.println(url + query + "&start=" + i);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			try {
				String responseBody = httpClient.execute(httpget,
						responseHandler);
				InputStream is = new ByteArrayInputStream(
						responseBody.getBytes());
				Reader reader = new InputStreamReader(is);
				googleRes results = new Gson()
						.fromJson(reader, googleRes.class);
				if (results != null) {
					if(results.items!=null)
						len = results.items.size();
					else 
						return googleResults;
				} else {
					len = 0;
				}
				for (int j = 0; j < len; j++) {

					results r = new results();
					r.Title = results.items.get(j).title;
					r.Url = results.items.get(j).link;
					r.Description = null;

					googleResults.add(r);
				}
				
				i += len;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return googleResults;

	}
}

class GoogleResults {
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
		private String url;
		private String title;

		public String getUrl() {
			return url;
		}

		public String getTitle() {
			return title;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String toString() {
			return "Result[url:" + url + ",title:" + title + "]";
		}
	}

	static class googleRes {

		List<items> items;

	}

	static class items {

		String title;
		String link;

	}
}
