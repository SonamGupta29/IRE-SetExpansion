
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.util.List;

public class tweetsearch {

	public static void main(String[] args) throws IOException, TwitterException {
		
		System.getProperties().put("http.proxyHost", "proxy.iiit.ac.in");
		System.getProperties().put("http.proxyPort", "8080");
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("********************")
		  .setOAuthConsumerSecret("************************")
		  .setOAuthAccessToken("******************************")
		  .setOAuthAccessTokenSecret("******************************");
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		
		if (args.length < 1) {
			System.out.println("java twitter4j.examples.search.SearchTweets [query]");
			System.exit(-1);
		}
		
		Twitter twitter = tf.getInstance();
		
		try {
		Query query = new Query(args[0]);
		QueryResult result;
		int count=0;
		
		do {
		result = twitter.search(query);
		List<Status> tweets = result.getTweets();
		
		String keyword = args[0];
		System.out.println(keyword);

		for (Status tweet : tweets) {
			count++;
			String s = tweet.getUser().getScreenName();
			if (!s.toLowerCase().contains(keyword.toLowerCase()))
				System.out.println("@" + s + " - " + tweet.getText());
		}
		} while ((query = result.nextQuery()) != null && count <= 8);
		
		System.exit(0);
		} catch (TwitterException te) {
		te.printStackTrace();
		System.out.println("Failed to search tweets: " + te.getMessage());
		System.exit(-1);
		}
	}
}
