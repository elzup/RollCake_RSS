import java.awt.Container;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lib.Feed;
import lib.Item;

import com.sun.istack.internal.Nullable;

public class RollCakeRSS extends JFrame {

	public static String DEBUG_URL = "http://www.nicovideo.jp/tag/minecraft技術部?sort=f&rss=2.0";
	public static String FRAME_TITLE = "RollCakeRSS";

	FeedManager fm;
	public static void main(String... args) {
		System.out.println("run start");

		RollCakeRSS rcake = new RollCakeRSS();

		rcake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rcake.setBounds(10, 10, 300, 200);
		rcake.setTitle(FRAME_TITLE);
		rcake.setVisible(true);




		System.out.println("run end");
	}

	RollCakeRSS() {
//		Container cp = this.getContentPane();

		this.fm = new FeedManager();
		this.fm.addFeed(DEBUG_URL, null);
		this.updateTable();
//		this.fm._consoleOutput();
	}

	public void updateTable() {
		Container cp = this.getContentPane();
		cp.add(this.fm.getPanel());
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

	public JPanel getPanel() {
		JPanel p = new JPanel();
		for (Feed feed: this.feedList) {
			for (Item item : feed.getItemList()) {
				JLabel label = new JLabel(item.getTitle());
				p.add(label);
			}
		}
		return p;
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
