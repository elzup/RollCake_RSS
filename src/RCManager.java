import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lib.Item;

import com.sun.istack.internal.Nullable;

public class RCManager {

	private JPanel contentPane;
	private ArrayList<RCFeed> feedList;
	private Table table;

	private int mode;

	public static int table_mode_recentry = 0;

	public RCManager() {
		this.feedList = new ArrayList<RCFeed>();
	}

	public ArrayList<RCFeed> getFeedList() {
		return this.feedList;
	}

	public void addFeed(RCFeed feed) {
		this.feedList.add(feed);
	}

	public void addFeed(String name, String url, boolean compact, @Nullable String encode) {
		this.addFeed(this.createFeed(name, url, encode));
	}

	public void addFeed(String name, String url, @Nullable String encode) {
		this.addFeed(name, url, false, encode);
	}

	public void addFeed(String url, @Nullable String encode) {
		this.addFeed(null, url, false, encode);
	}

	public RCFeed createFeed(String name, String url, @Nullable String encode) {
		RCFeed feed = new RCFeed();
		if (!feed.setURL(url)) {
			return null;
		}
		feed.setName(name);
		if (encode != null) // 引数で指示があったら文字コードを指定
			feed.setEncoding(encode);
		feed.run();
		return feed;
	}

	public RCFeed createFeed(String url, @Nullable String encode) {
		return this.createFeed(null, url, encode);
	}

	public void setContentPane(JPanel cPane) {
		this.contentPane = cPane;
		this.table = new Table(contentPane, this.feedList);
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

	public JPanel setTablePane() {
		JPanel table = new JPanel();
		table.setLayout(new GridLayout(1, RCConfig.num_day_recentry));
		JPanel[] colPanes = this.getRecentlyTable();

		for (int i = 0; i < colPanes.length; i++)
			table.add(colPanes[i]);
		//		System.out.println(contentPane.getComponents().length + "<<<");
		JPanel leftPane = (JPanel) contentPane.getComponent(0);

		if (leftPane.getComponents().length > 1)
			leftPane.remove(leftPane.getComponent(1));
		((JPanel) contentPane.getComponent(0)).add(table, BorderLayout.NORTH);
		table.setPreferredSize(RCConfig.tablepane_size_dimension);
		table.setBackground(RCConfig.tablepane_background_color);
		table.setVisible(false);
		table.setVisible(true);
		return table;
	}

	public void update() {

	}

	@SuppressWarnings("deprecation")
	private JPanel[] getRecentlyTable() {
		JPanel[] pane = new JPanel[RCConfig.num_day_recentry];

		for (int i = 0; i < RCConfig.num_day_recentry; i++)
			pane[i] = this.table.getDatePane(i);
		return pane;
	}
}
