package api;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.util.List;

public class TwitterSearchAPI {

	public static void main(String[] args) throws IOException, TwitterException {
		
		System.getProperties().put("http.proxyHost", "proxy.iiit.ac.in");
		System.getProperties().put("http.proxyPort", "8080");
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("ZvyHY7wciiK7WMHSvCLow8755")
		  .setOAuthConsumerSecret("rirtzaACfiINS6nOaQajckqelWnDrqEMIQ3Hz4XTSaiFd8K5xD")
		  .setOAuthAccessToken("3066359394-v3ExCcaKVxksplTxk3EmANIylScClNdZrjVsnq3")
		  .setOAuthAccessTokenSecret("6dMcJElEZyQmq1JfvDm4Vw2DxI9Q4mdQhQPXUmFsOqW0P");
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		
//		if (args.length < 1) {
//			System.out.println("java twitter4j.examples.search.SearchTweets [query]");
//			System.exit(-1);
//		}
		
		Twitter twitter = tf.getInstance();
		
		try {
		Query query = new Query("inheritance abstraction encapsulation");
		QueryResult result;
		int count=0;
		
		do {
		result = twitter.search(query);
		List<Status> tweets = result.getTweets();
		
		String keyword ="inheritance abstraction encapsulation";
		System.out.println(keyword);

		for (Status tweet : tweets) {
			count++;
			String s = tweet.getUser().getScreenName();
			if (!s.toLowerCase().contains(keyword.toLowerCase()))
				System.out.println("@" + s + " - " + tweet.getText());
		}
		} while ((query = result.nextQuery()) != null && count <= 10);
		
		System.exit(0);
		} catch (TwitterException te) {
		te.printStackTrace();
		System.out.println("Failed to search tweets: " + te.getMessage());
		System.exit(-1);
		}
	}
}
