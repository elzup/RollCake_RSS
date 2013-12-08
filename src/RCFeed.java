import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import lib.Feed;

import org.w3c.dom.Node;

public class RCFeed extends Feed {
	private int groupId;
	private String name;
	private boolean isSimple = false;
//	private String url;

	private ArrayList<RCItem> itemList;

	public RCFeed() {
		// TODO Constracter
		super();
		this.itemList = new ArrayList<RCItem>();
	}

	public boolean setURL(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	public URL getUrl() {
		return this.url;
	}
	public void setGroupId(int id) {
		this.groupId = id;
	}
	public int getGroupId() {
		return this.groupId;
	}

	public boolean isSimple() {
		return this.isSimple;
	}
	public void setSimple (boolean b) {
		this.isSimple = b;
	}
	public void setSimple () {
		this.setSimple(true);
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getName () {
 		return this.name;
	}

	public ArrayList<RCItem> getRCItemList() {
		return itemList;
	}

//	public ArrayList<RCItem> getRecentryDate(int diff) {
//		for (RCItem item: this.itemList) {
//			int d = item.getRecentryNum();
//			if (d == -1)
//				continue;
//			System.out.printf("%s\n%s\n%s\n%d\n\n", item.getTitle(), item.getLink(), item.getDate(), d);
//			int h = item.getDate().getHours();
//			int m = (item.getDate().getMinutes()) / (60 / RCConfig.num_split_column_hour);
//			System.out.printf("%d : %d : %d\n", d, h, m);
//			but.setEnabled(true);
//			String key = d + ":" + h + ":" + m;
//		}
//	}

	@Override protected void findItems(Node node) {
		for (Node current = node.getFirstChild(); current != null; current = current.getNextSibling()) {
			if (current.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = current.getNodeName();
				if (nodeName.equals("item") || nodeName.equals("entry")) {
					RCItem item = new RCItem(current);
					if (this.isSimple) item.compact();
					itemList.add(item);
				}
				else
					findItems(current);
			}
		}
	}
}


