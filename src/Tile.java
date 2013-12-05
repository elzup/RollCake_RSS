import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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

	private void removeActionListener(JButton b) {
		while(true) {
			ActionListener[] als = b.getActionListeners();
			if (als.length == 0) return;
			b.removeActionListener(als[0]);
		}
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
}