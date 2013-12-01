package sample;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import lib.Feed;
import lib.Item;

public class GoogleBlogSearcher {
	public static void main(String[] args) {
		Feed feed = new Feed();
		String query = args[0];
		try {
			query = URLEncoder.encode(query, "utf-8");  // 全角文字をURLエンコード
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		feed.setURL("https://www.google.co.jp/search?hl=ja&tbm=blg&output=rss&q=" + query);
		feed.run();
		ArrayList<Item> itemList = feed.getItemList();
		// 表示
		for(Item item : itemList) {
			System.out.println("タイトル: " + item.getTitle());
			System.out.println("日時: " + item.getDate());
			System.out.println("スニペット: " + item.getDescription());
			System.out.println("ページURL: " + item.getLink());
			System.out.println();
		}
	}
}
