import java.util.ArrayList;

import lib.Feed;

import org.w3c.dom.Node;

public class RCFeed extends Feed {
	private String name;
	private boolean isSimple = false;

	private ArrayList<RCItem> itemList;

	public RCFeed() {
		// TODO Constracter
		super();
		this.itemList = new ArrayList<RCItem>();
	}

	public RCFeed(String url) {
		// TODO Constracter
		super();
		this.setURL(url);
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
