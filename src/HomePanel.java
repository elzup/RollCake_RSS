import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HomePanel extends JPanel {
	private ArrayList<RCItem> itemList;

	public HomePanel() {
		GridLayout gl =  new GridLayout(0, RCConfig.num_panel_layout_column);
		gl.setVgap(5);
		gl.setHgap(5);
		this.setLayout(gl);
		this.setBorder(RCConfig.margin_border);
		this.itemList = new ArrayList<>();
	}

	public HomePanel(RCGroup group) {
		this();
		setGroup(group);
	}

	public void setGroup(RCGroup group) {
		this.removeAll();
		this.itemList.clear();
		for (RCFeed feed : group.getFeedList()) {
			for (RCItem item : feed.getRCItemList()) {
				itemList.add(item);
			}
		}
		for (RCItem item : itemList) {
			ItemPanel itemPane = new ItemPanel(item);
			this.add(itemPane);
		}
		this.setVisible(false);
		this.setVisible(true);
	}

	class ItemPanel extends JPanel {
		public ItemPanel(RCItem item) {
			this.setPreferredSize(RCConfig.itempane_size);
			this.setBackground(Color.cyan);
		}
	}
}
