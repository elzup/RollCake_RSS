import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lib.Item;

import com.sun.istack.internal.Nullable;

public class FeedManager {

	private JPanel uPane;
	private ArrayList<RCFeed> feedList;
	private HashMap<String, Tile> tileList;

	public HashMap getTiles() {
		return this.tileList;
	}

	public FeedManager() {
		this.feedList = new ArrayList<RCFeed>();
		this.tileList = new HashMap<String, Tile>();
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

	public void setUnderPane(JPanel p) {
		this.uPane = p;
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
		table.setLayout(new GridLayout(1, RCConfig.num_day_recentry));
		JPanel[] colPanes = new JPanel[RCConfig.num_day_recentry];
		for (int i = 0; i < colPanes.length; i++) {
			colPanes[i] = new JPanel();
			colPanes[i].setLayout(new GridLayout(24, RCConfig.num_split_column_hour));
			table.add(colPanes[i]);
		}
		System.out.println(colPanes.length);
		this.putRecently(colPanes);
		return table;
	}

	@SuppressWarnings("deprecation")
	private void putRecently(JPanel[] pane) {
		if (pane.length != RCConfig.num_day_recentry) {
			System.out.println("Argumentが不正:" + "長さが3でない");
			return;
		}

		JButton[][][] bus = new JButton[RCConfig.num_day_recentry][24][RCConfig.num_split_column_hour];

		for (int l = 0; l < bus.length; l++)
			for (int j = 0; j < bus[0].length; j++)
				for (int i = 0; i < bus[0][0].length; i++) {
					bus[l][j][i] = new JButton();
					bus[l][j][i].setEnabled(false);
					System.out.printf("%d : %d : %d\n", l, j, i);
					if (pane[l] == null) {
						System.out.println("null");
					}
					pane[l].add(bus[l][j][i]);
				}

		for (RCFeed feed : this.feedList) {
			for (RCItem item : feed.getRCItemList()) {
				int d = item.getRecentryNum();
				if (d == -1)
					continue;
				System.out.printf("%s\n%s\n%s\n%d\n\n", item.getTitle(), item.getLink(), item.getDate(), d);
				int h = item.getDate().getHours();
				int m = (item.getDate().getMinutes()) / (60 / RCConfig.num_split_column_hour);
				System.out.printf("%d : %d : %d\n", d, h, m);
				bus[d][h][m].setEnabled(true);
				String key = d + ":" + h + ":" + m;
				Tile tile = new Tile();
				if (tileList.containsKey(key)) {
					tile = tileList.remove(key);
				}
				else
					tile = new Tile();
				tile.addItem(item);
				tileList.put(key, tile);

				bus[d][h][m].removeActionListener(null);
				bus[d][h][m].addActionListener(new ActionOpenDetails(this.uPane, tile));
			}
		}
	}
}

class ActionOpenDetails implements ActionListener {
	private JPanel pane;
	private Tile tile;

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(tile.getItems().get(0).getTitle());
		//		pane.removeAll();
		//		pane.add(tile.getItemPane());
		JTextField l = new JTextField("test");
		pane.add(l);
	}

	public ActionOpenDetails(JPanel pane, Tile tile) {
		// TODO Constracter
		this.pane = pane;
		this.tile = tile;
	}
}
