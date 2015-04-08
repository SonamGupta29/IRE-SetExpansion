package webdb;

import util.SearchProvider;

public class Web {

	public static String getPageHtml(String url) {

		String html = SearchProvider.getHtml(url);
		return html;
	}
}
