import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lib.Item;

import org.w3c.dom.Node;

public class RCItem extends Item {

	private Color color;
	private String imageUrl;
	private int feedId;

	// ------------------- getter, setter -------------------//
	public void setColor(Color col) {
		this.color = col;
	}

	public Color getColor() {
		return this.color;
	}

	public void setFeedId (int feedId) {
		this.feedId  = feedId;
	}
	public int getFeedId () {
		return this.feedId;
	}

	public void setImgUrl(String url) {
		this.imageUrl = url;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	// ------------------- getter, setter end -------------------//

	public RCItem(Node node) {
		this(node, Color.white);
	}

	public RCItem(Node node, Color color) {
		super(node);
		this.color = color;
	}

	public void run(Node node) {
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
		for (Node current = node.getFirstChild(); current != null; current = current.getNextSibling()) { // 子ノードを先頭から
			if (current.getNodeType() == Node.ELEMENT_NODE) { // 要素だったら
				String nodeName = current.getNodeName();
				if (nodeName.equals("title"))
					title = getContent(current);
				else if (nodeName.equals("link"))
					if (current.hasChildNodes()) // RSS
						link = getContent(current);
					else
						// Atom
						link = current.getAttributes().getNamedItem("href").getNodeValue();
				else if (nodeName.equals("description") || nodeName.equals("summary"))
					description = getContent(current);
				else if (nodeName.equals("dc:date") || nodeName.equals("updated"))
					dcDate = getContent(current);
				else if (nodeName.equals("pubDate"))
					pubDate = getContent(current);
				else if (nodeName.equals("dc:creator"))
					dcCreator = getContent(current);
				else if (nodeName.equals("dc:subject")) {
					if (dcSubject == null) {
						dcSubject = new LinkedList<String>();
					}
					dcSubject.add(getContent(current));
//@formatter:off
				} else if (
						nodeName.equals("guid") 	||
						nodeName.equals("category") ||
						nodeName.startsWith("dc:")  ||
						nodeName.startsWith("rdf:") ||
						nodeName.startsWith("dcq:")
						) {
					; // 処理しない
//@formatter:on
				} else {
					; // 処理しない
				}
			}
			// 要素 (Node.ELEMENT_NODE) でなかったら何もしない (改行など)
		}

		imageUrl = null;
		String regex = "<img.*src=\"(.*)\"";
		Matcher m = Pattern.compile(regex).matcher(description);
		if (m.find()) {
			this.imageUrl = m.group(1);
		}
	}

	public void compact() {
		this.description = null;
	}

	public String getDateString() {
		if (dateString == null) {
			Date d = getDate();
			if (d != null)
				dateString = new SimpleDateFormat("yyyy年 MM/dd HH:mm").format(d);
			//				dateString = String.format("%d年 %2d/%2d %02d:%02d", d.getYear() + 1900, d.getMonth() + 1, d.getDate(), d.getHours(), d.getMinutes());
		}
		return dateString;
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
		long diff = getDiffToday(this.getDate());
		return (int) (diff / (60 * 60 * 24 * 1000));
	}

	public String getKey() {
		@SuppressWarnings("deprecation")
//@formatter:off
		int[] vals = {
				this.getDiffTodayNum(),
				this.getDate().getHours(),
				(this.getDate().getMinutes()) / (60 / RCConfig.num_split_column_hour),
		};
//@formatter:on
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
		return vals[0] + RCConfig.key_delimiter + vals[1] + RCConfig.key_delimiter + vals[2];
	}

}
