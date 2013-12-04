import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Tile {
	private ArrayList<RCItem> itemList;

	public Tile() {
		this.itemList = new ArrayList<RCItem>();
	}
	public Tile(RCItem item) {
		this.itemList = new ArrayList<RCItem>();
		this.itemList.add(item);
	}

	public ArrayList<RCItem> getItems () {
		return this.itemList;
	}

	public void addItem(RCItem e) {
		this.itemList.add(e);
	}

	public int size() {
		return this.itemList.size();
	}

	public JPanel getItemPane() {
		JPanel pane = new JPanel(new GridLayout(this.size(), 1));
		System.out.println(this.size() + "]");
		for (RCItem item: this.itemList) {
			System.out.println(item.getTitle());
			pane.add(item.getItemPanel());
		}
		return pane;
	}
}

