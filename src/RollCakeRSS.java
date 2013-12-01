import java.util.ArrayList;
import java.util.Iterator;

import lib.Feed;
import lib.Item;

import com.sun.istack.internal.Nullable;

public class RollCakeRSS {


	public static String DEBUG_URL = "http://www.nicovideo.jp/tag/minecraft技術部?sort=f&rss=2.0";
	public static void main(String... args) {
		System.out.println("run start");
		FeedManager fm = new FeedManager();
		fm.addFeed(DEBUG_URL, null);
		fm._consoleOutput();

		System.out.println("run end");
	}

}

class FeedManager {

	ArrayList<Feed> feedList;

	public FeedManager() {
		this.feedList = new ArrayList<Feed>();
	}

	public void addFeed(String url, @Nullable String encode) {
		this.feedList.add(this.createFeed(url, encode));
	}

	private Feed createFeed(String url, @Nullable String encode) {
		Feed feed = new Feed();
		feed.setURL(url);
		if (encode != null) // 引数で指示があったら文字コードを指定
			feed.setEncoding(encode);
		feed.run();
		return feed;
	}

	public void _consoleOutput() {
		for (Feed feed : this.feedList) {
			ArrayList<Item> itemList = feed.getItemList();
			// 表示
			Iterator<Item> iterator = itemList.iterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next().toString());
				System.out.println();
			}
		}

	}
}
