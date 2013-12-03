import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
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

	public void addFeed(String url, @Nullable String encode) {
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

	@SuppressWarnings("deprecation")
	public JPanel getPanel() {
		JPanel p = new JPanel();
		for (RCFeed feed : this.feedList) {
			for (RCItem item : feed.getRCItemList()) {
				Date d = item.getDate();
				JLabel label = new JLabel(item.getTitle());
//				p.add(label);
			}
		}
		return p;
	}

	public void setupTile() {
		if (this.feedList.size() == 0) {
			System.out.println("feedListが空です");
			return;
		}

		for (RCFeed feed : this.feedList) {
			for (RCItem item : feed.getRCItemList()) {
				System.out.println(item.getDateString()); ///
				System.out.println(item.getDateTime()); ///
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

	public JPanel getTable() {
		JPanel table = new JPanel();
		table.setLayout(new GridLayout(1, 3));
		JPanel[] colPanes = new JPanel[3];
		for (JPanel p : colPanes) {
			p = new JPanel();
			p.setLayout(new GridLayout(24, 4));
		}

		this.putRecently(colPanes);
		return table;
	}

	private void putRecently(JPanel[] pane) {
		if (pane.length != 3) {
			System.out.println("Argumentが不正:" + "長さが3でない");
			return;
		}

		for (RCFeed feed : this.feedList) {
			for (RCItem item : feed.getRCItemList()) {
				int res = item.getRecentryNum();

				System.out.printf("%s\n%s\n%s\n%d\n\n", item.getTitle(), item.getLink(), item.getDate(), res);

				if (res == -1) continue;
			}
		}
	}
}
