package api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class GoogleSearchAPI extends SearchAPI {

	HttpClient httpClient;
	String url = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=";

	public GoogleSearchAPI() {
		httpClient = new DefaultHttpClient();
		HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);
	}

	@Override
	public void getTopURLs(String query, int n) {

		try {
			query = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < n; i = i + 4) {
			
			HttpGet httpget = new HttpGet(url + i + "&q=" + query);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			try {
				String responseBody = httpClient.execute(httpget,
						responseHandler);
				InputStream is = new ByteArrayInputStream(
						responseBody.getBytes());
				Reader reader = new InputStreamReader(is);
				GoogleResults results = new Gson().fromJson(reader,
						GoogleResults.class);
				System.out.println("Size of results: "
						+ results.getResponseData().getResults().size());
				for (int j = 0; j <= 3; j++) {
					System.out.println("Title: "
							+ results.getResponseData().getResults().get(j)
									.getTitle());
					System.out.println("URL: "
							+ results.getResponseData().getResults().get(j)
									.getUrl() + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

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
}
