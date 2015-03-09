package http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;


public class HttpQueries {

	public static String sendGetQuery(String query, HttpClient client)
			throws HttpException {
		// Send the query
		HttpGet queryRequest = new HttpGet(query);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody;
		try {
			responseBody = client.execute(queryRequest, responseHandler);
		} catch (ClientProtocolException e) {
			throw new HttpException("An HTTP protocol error occurred.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new HttpException("The connection was aborted.");
		}
		// System.out.println(responseBody);
		return responseBody.toString();
	}
}
