import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class Table {

	public HashMap<String, Tile> tileMap;
	public JPanel contentPane;
	public ArrayList<RCFeed> feedList;

	public Table(JPanel pane, ArrayList<RCFeed> feedLsit) {
		// TODO Constracter
		this.contentPane = pane;
		this.feedList = feedLsit;
	}

	public JPanel getDatePane(int diff) {
		assert 0 <= diff && diff < 3;
		this.tileMap = new HashMap<String, Tile>();
		if (feedList.size() == 0) {
			System.out.println("feedLsitが空です");
			return null;
		}

		for (RCFeed feed : this.feedList) {
			this.tileUpdate(feed, diff);
		}
		return this.getTilePain();
	}

	public void tileUpdate(RCFeed feed, int diff) {
//		System.out.println(feed.getName());
		for (RCItem item : feed.getRCItemList()) {
			int d = item.getDate().getDate();
			int num_diff = item.getDiffTodayNum();
			if (Math.abs(num_diff - diff) > 2 || d != RCConfig.getDateDif(diff))
				continue;
//			System.out.println("~~~");
			String key = item.getKey();
			Tile tile = new Tile();
			if (tileMap.containsKey(key)) {
				tile = tileMap.get(key);
				tile.addItem(item);
			}
			else
				this.tileMap.put(key, new Tile(item));
		}
	}

	public JPanel getTilePain() {
		JPanel pane = new JPanel(new GridLayout(24, RCConfig.num_split_column_hour));
		pane.setPreferredSize(RCConfig.tablepane_size_dimension);
		pane.setBackground(RCConfig.tablepane_background_color);
		for (int j = 0; j < 24; j++) {
			JLabel label = new JLabel(String.valueOf(j));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setPreferredSize(RCConfig.label_table_hour);
			pane.add(label);
			for (int i = 0; i < RCConfig.num_split_column_hour; i++) {
				JButton b = new JButton();
				b.setEnabled(false);
				b.setForeground(RCConfig.button_font_color);
				b.setBackground(RCConfig.button_back_color);
				b.setBorder(RCConfig.button_border);
				pane.add(b);
			}
		}
		for (Map.Entry<String, Tile> e : this.tileMap.entrySet()) {
			Tile tile = e.getValue();
			int[] vals = RCItem.keyToVals(e.getKey());
			System.out.println(e.getKey());
			int n = vals[1] * (RCConfig.num_split_column_hour + 1) + (vals[2] + 1);
			JButton b = (JButton) pane.getComponent(n);
			b.setText(String.valueOf(tile.size()));
			b.setEnabled(true);
			JPanel uPane = (JPanel) ((JPanel) contentPane.getComponent(0)).getComponent(0);
			b.addActionListener(new ActionOpenDetails(uPane, tile));
		}
		return pane;
	}

	public void put(String key, Tile value) {
		this.tileMap.put(key, value);
	}
	public void addAll(ArrayList<RCItem> itemList) {
	}
	public void removeAll(ArrayList<RCItem> itemList) {
	}
}

class ActionOpenDetails implements ActionListener {
	private JPanel pane;
	private Tile tile;

	@Override
	public void actionPerformed(ActionEvent e) {
		pane.removeAll();
		JScrollPane spane = new JScrollPane(tile.getItemPane());

		pane.add(spane, BorderLayout.CENTER);
		pane.setVisible(false);
		pane.setVisible(true);
		//		pane.repaint();
	}

	public ActionOpenDetails(JPanel pane, Tile tile) {
		// TODO Constracter
		this.pane = pane;
		this.tile = tile;
	}

	public static void removeActionListener(JButton b) {
		while (true) {
			ActionListener[] als = b.getActionListeners();
			if (als.length == 0)
				return;
			b.removeActionListener(als[0]);
		}
	}
}