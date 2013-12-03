import lib.Feed;
import lib.Item;

import org.w3c.dom.Node;

public class RCFeed extends Feed {
	private String name;
	private boolean isSimple = false;

	public RCFeed() {
		// TODO Constracter
		super();
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

	@Override protected void findItems(Node node) {
		// node の子ノードについて繰り返す
		for (Node current = node.getFirstChild(); current != null; current = current.getNextSibling()) {
			// 着目している子ノード current は要素か
			if (current.getNodeType() == Node.ELEMENT_NODE) {
				// 要素なら要素名をチェック
				String nodeName = current.getNodeName();
				if (nodeName.equals("item") || nodeName.equals("entry")) // item または entry 要素を発見
					// item 要素または entry 要素から Item オブジェクトを生成しリストに追加
					itemList.add(new Item(current));
				else
					// item 要素または entry 要素でなければ、さらにその要素の子ノードから探す
					// (channel, items など)
					findItems(current); // 再帰呼び出し
			}
		}
	}
}
