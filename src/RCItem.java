import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import lib.Item;

import org.w3c.dom.Node;

public class RCItem extends Item {

	private Color color;
	private int num_recently;

	public void setColor(Color col) {
		this.color = col;
	}

	public Color getColor() {
		return this.color;
	}

	public RCItem(Node node) {
		this(node, Color.white);
	}

	public RCItem(Node node, Color color) {
		super(node);
		this.num_recently = -1;
		this.color = color;
		// TODO Constracter
	}

	public void run(Node node){
		title = null;
		link = null;
		description = null;
		dcDate = null;
		pubDate = null;
		dcCreator = null;
		dcSubject = null;
		date = null;
		dateString = null;
		// 引数 node (= item要素) の子ノードを走査
		for(Node current = node.getFirstChild();
				current != null;
				current = current.getNextSibling()) { // 子ノードを先頭から
			if(current.getNodeType() == Node.ELEMENT_NODE) { // 要素だったら
				String nodeName = current.getNodeName();
				if(nodeName.equals("title"))
					title = getContent(current);
				else if(nodeName.equals("link"))
					if(current.hasChildNodes())  // RSS
						link = getContent(current);
					else  // Atom
						link = current.getAttributes().getNamedItem("href").getNodeValue();
				else if(nodeName.equals("description") || nodeName.equals("summary"))
					description = getContent(current);
				else if(nodeName.equals("dc:date") || nodeName.equals("updated"))
					dcDate = getContent(current);
				else if(nodeName.equals("pubDate"))
					pubDate = getContent(current);
				else if(nodeName.equals("dc:creator"))
					dcCreator = getContent(current);
				else if(nodeName.equals("dc:subject")) {
					if(dcSubject == null) {
						dcSubject = new LinkedList<String>();
					}
					dcSubject.add(getContent(current));
				}
				else if(nodeName.equals("guid") ||
						nodeName.equals("category") ||
						nodeName.startsWith("dc:") ||
						nodeName.startsWith("rdf:") ||
						nodeName.startsWith("dcq:")){
					; // 処理しない
				}
				else {
					; // 処理しない
				}
			}
			// 要素 (Node.ELEMENT_NODE) でなかったら何もしない (改行など)
		}
	}
	public void compact() {
		this.description = null;
	}

	public int getRecentryNum() {
		int diffNum = this.getDiffTodayNum();
		return (diffNum > RCConfig.num_day_recentry) ? -1 : diffNum;
	}

	public static long getDiffToday(Date d) {
		Calendar cal = Calendar.getInstance();
		return cal.getTime().getTime() - d.getTime();
	}

	public int getDiffTodayNum() {
		if (this.num_recently != -1)
			return this.num_recently;
		long diff = getDiffToday(this.getDate());
		return this.num_recently = (int) (diff / (60 * 60 * 24 * 1000));
	}

	public void setNumRecently(int id) {
		this.num_recently = id;
	}

	public String getKey() {
		@SuppressWarnings("deprecation")
		int[] vals = {
				this.getDiffTodayNum(),
				this.getDate().getHours(),
				(this.getDate().getMinutes())
						/ (60 / RCConfig.num_split_column_hour), };
		return valsToKey(vals);
	}

	public static int[] keyToVals(String key) {
		int[] vals = new int[3];
		String[] strs = key.split(RCConfig.key_delimiter);
		for (int i = 0; i < 3; i++)
			vals[i] = Integer.parseInt(strs[i]);
		return vals;
	}

	public static String valsToKey(int[] vals) {
		return vals[0] + RCConfig.key_delimiter + vals[1]
				+ RCConfig.key_delimiter + vals[2];
	}
}
