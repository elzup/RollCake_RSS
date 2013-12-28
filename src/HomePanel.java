import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HomePanel extends JPanel {
	private ArrayList<ItemPanel> itemPanelList;

	public HomePanel() {
		this.itemPanelList = new ArrayList<>();
		this.setLayout(new FlowLayout());
	}

	public HomePanel(RCGroup group) {
		this();
		setGroup(group);
	}

	public void setGroup(RCGroup group) {
		this.removeAll();
		this.itemPanelList.clear();
		this.itemPanelList = new ArrayList<>();

		ArrayList<RCItem> itemList = new ArrayList<>();
		for (RCFeed feed : group.getFeedList()) {
			for (RCItem item : feed.getRCItemList()) {
				itemList.add(item);
			}
		}
		for (RCItem item : itemList) {
			ItemPanel itemPane = new ItemPanel(item);
			itemPanelList.add(itemPane);
			this.add(itemPane);
		}
	}

	class ItemPanel extends JPanel {
		private RCItem item;
		public ItemPanel(RCItem item) {
			this.item = item;
			this.add(new JLabel(item.getTitle()));
		}
	}
}
