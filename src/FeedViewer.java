
import java.util.ArrayList;
import java.util.Iterator;

import lib.Feed;
import lib.Item;
public class FeedViewer {

	private static String DEBUG_URL = "http://www.nicovideo.jp/tag/minecraft技術部?sort=f&rss=2.0";
	public static void main(String[] args) {
		String url = DEBUG_URL;
		if (args.length > 0) url = args[0];
		Feed feed = new Feed();
		feed.setURL(url);
		if(args.length > 1)  // 引数で指示があったら文字コードを指定
			feed.setEncoding(args[1]);
		feed.run();
		ArrayList<Item> itemList = feed.getItemList();
		// 表示
		Iterator<Item> iterator = itemList.iterator();
		while(iterator.hasNext()) {
			System.out.print(iterator.next().toString());
			System.out.println();
		}
	}

}
