import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lib.Item;

import com.sun.istack.internal.Nullable;

public class RCManager {

	private JPanel uPane;
	private ArrayList<RCFeed> feedList;
	private Table table;

	public RCManager() {
		this.feedList = new ArrayList<RCFeed>();
	}

	public void addFeed(String name, String url, boolean compact, @Nullable String encode) {
		this.feedList.add(this.createFeed(name, url, encode));
	}

	public void addFeed(String name, String url, @Nullable String encode) {
		this.addFeed(name, url, false, encode);
	}

	private RCFeed createFeed(String name, String url, @Nullable String encode) {
		RCFeed feed = new RCFeed();
		feed.setName(name);
		feed.setURL(url);
		if (encode != null) // 引数で指示があったら文字コードを指定
			feed.setEncoding(encode);
		feed.run();
		return feed;
	}

	public void setUnderPane(JPanel p) {
		this.uPane = p;
		this.table = new Table(p, this.feedList);
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

	public JPanel getTablePane() {
		JPanel table = new JPanel();
		table.setLayout(new GridLayout(1, RCConfig.num_day_recentry));
		JPanel[] colPanes = this.getRecentlyTable();

		for (int i = 0; i < colPanes.length; i++)
			table.add(colPanes[i]);
		return table;
	}



	@SuppressWarnings("deprecation")
	private JPanel[] getRecentlyTable() {
		JPanel[] pane = new JPanel[RCConfig.num_day_recentry];
		//		if (pane.length != RCConfig.num_day_recentry) {
		//			System.out.println("Argumentが不正:" + "長さが3でない");
		//			return null;
		//		}

		for (int i = 0; i < RCConfig.num_day_recentry; i++)
			pane[i] = this.table.getDatePane(i);
		return pane;
	}
}
