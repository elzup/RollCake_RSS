import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lib.Item;

import com.sun.istack.internal.Nullable;


public class FeedManager {

	ArrayList<RCFeed> feedList;
	public ArrayList<Tile> tileList;

	public ArrayList<Tile> getTiles() {
		return this.tileList;
	}


	public FeedManager() {
		this.feedList = new ArrayList<RCFeed>();
	}

	public void addFeed(String url, boolean compact, @Nullable String encode) {
		this.feedList.add(this.createFeed(url, encode));
	}

	public void addFeed(String url,@Nullable String encode) {
		this.addFeed(url, false, encode);
	}

	private RCFeed createFeed(String url, @Nullable String encode) {
		RCFeed feed = new RCFeed();
		feed.setURL(url);
		if (encode != null) // 引数で指示があったら文字コードを指定
			feed.setEncoding(encode);
		feed.run();
		return feed;
	}

	public JPanel getPanel() {
		JPanel p = new JPanel();
		for (RCFeed feed : this.feedList) {
			for (Item item : feed.getItemList()) {
				JLabel label = new JLabel(item.getTitle());
				String url = item.getLink();
				System.out.println(url);
				p.add(label);
			}
		}
		return p;
	}

	public void setupTile() {
		if (this.feedList.size() == 0) {
			System.out.println("feedListが空です");
			return ;
		}

		for (RCFeed feed: this.feedList) {
			for (Item item : feed.getItemList()) {
				System.out.println(item.getDateString());	///
				System.out.println(item.getDateTime());	///
				System.out.println();
			}
		}

	}

	public void _consoleOutput() {
		for (RCFeed feed : this.feedList) {
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

