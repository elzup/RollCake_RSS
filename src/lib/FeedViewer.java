
package lib;


import java.util.ArrayList;
import java.util.Iterator;

public class FeedViewer {
	public static void main(String[] args) {
		Feed feed = new Feed();
		feed.setURL(args[0]);
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
