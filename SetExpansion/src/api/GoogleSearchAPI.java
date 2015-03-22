package api;
import java.io.ByteArrayInputStream;
public class GoogleSearchAPI extends SearchAPI {


    public static void main(String[] args) {
		GoogleSearchAPI ap = new GoogleSearchAPI();
		Scanner scan = new Scanner(System.in);
		String query;
		query = scan.nextLine();
		int total;
		total = scan.nextInt();
		ap.getData(query,total);
	}
	@Override
	public void getData(String query, int total) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		for (int i = 0; i < 20; i = i + 4) {
			HttpGet httpget = new HttpGet("http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start="+i+"&q="+query);
	        System.out.println("executing request " + httpget.getURI());
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        try {
				String responseBody = httpclient.execute(httpget, responseHandler);
				InputStream is = new ByteArrayInputStream(responseBody.getBytes());
				Reader reader = new InputStreamReader(is);
				GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);
				for(int j=0; j<=3; j++){
					System.out.println("Title: " + results.getResponseData().getResults().get(j).getTitle());
					System.out.println("URL: " + results.getResponseData().getResults().get(j).getUrl() + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}


class GoogleResults{
    private ResponseData responseData;
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    public String toString() { return "ResponseData[" + responseData + "]"; }

    static class ResponseData {
        private List<Result> results;
        public List<Result> getResults() { return results; }
        public void setResults(List<Result> results) { this.results = results; }
        public String toString() { return "Results[" + results + "]"; }
    }

    static class Result {
        private String url;
        private String title;
        public String getUrl() { return url; }
        public String getTitle() { return title; }
        public void setUrl(String url) { this.url = url; }
        public void setTitle(String title) { this.title = title; }
        public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
    }
}
