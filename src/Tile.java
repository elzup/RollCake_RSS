import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Tile {
	private ArrayList<RCItem> itemList;

	public Tile() {
		this.itemList = new ArrayList<RCItem>();
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
		JPanel pane = new JPanel(new FlowLayout());
		for (RCItem item: this.itemList) {
			pane.add(item.getItemPanel());
		}
		return pane;
	}
}

