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
	public JPanel pane;
	public ArrayList<RCFeed> feedList;

	public Table(JPanel pane, ArrayList<RCFeed> feedLsit) {
		// TODO Constracter
		this.pane = pane;
		this.feedList = feedLsit;
		this.tileMap = new HashMap<String, Tile>();
	}

	public JPanel getDatePane(int diff) {
		if (feedList.size() == 0) {
			System.out.println("feedLsitが空です");
			return null;
		}

		for (RCFeed feed : this.feedList) {
			for (RCItem item : feed.getRCItemList()) {
				int d = item.getDiffTodayNum();
				if (d != diff)
					continue;
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

		JPanel pane = new JPanel(new GridLayout(24, RCConfig.num_split_column_hour));
		for (int j = 0; j < 24; j++) {
			JLabel label = new JLabel(String.valueOf(j));
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setPreferredSize(RCConfig.label_table_hour);
			pane.add(label);
			for (int i = 0; i < RCConfig.num_split_column_hour;i++){
				JButton b = new JButton();
				b.setEnabled(false);
				pane.add(b);
			}
		}
		for (Map.Entry<String, Tile> e : this.tileMap.entrySet()) {
			Tile tile = e.getValue();
			int[] vals = RCItem.keyToVals(e.getKey());
			System.out.println(e.getKey());
			int n = vals[1] * (vals[2] + 1);
			JButton b = (JButton) pane.getComponent(n);
			b.setText(String.valueOf(tile.size()));
			b.setEnabled(true);
			b.addActionListener(new ActionOpenDetails(this.pane, tile));
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
		while(true) {
			ActionListener[] als = b.getActionListeners();
			if (als.length == 0) return;
			b.removeActionListener(als[0]);
		}
	}
}