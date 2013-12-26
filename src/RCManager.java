import java.util.ArrayList;

import com.sun.istack.internal.Nullable;

public class RCManager {
	private ArrayList<RCGroup> groupList;

	private int groupPointer;

	public static int table_mode_recentry = 0;

	// ------------------- getter, setter, start -------------------//
	public ArrayList<RCGroup> getGroupList() {
		return this.groupList;
	}

	public int getGroupPointer() {
		return this.groupPointer;
	}

	public void setGroupPointer(int index) {
		this.groupPointer = index;
	}

	// ------------------- getter, setter, end -------------------//
	public RCManager() {
		this.groupList = new ArrayList<RCGroup>();
		this.groupPointer = 0;
	}

	public void addGroup(int id, String name) {
		this.groupList.add(new RCGroup(id, name));
	}

	public void addGroup(RCGroup group) {
		this.groupList.add(group);
	}

	public void addFeed(String name, String url) {
		this.groupList.get(groupPointer).add(this.createFeed(name, url, null));
	}

	public void addFeed(String url) {
		this.addFeed(null, url);
	}

	public RCFeed findFeed(String name) {
		for (RCGroup group : this.groupList) {
			RCFeed feed = group.findFeed(name);
			if (feed != null)
				return feed;
		}
		return null;
	}

	public RCFeed createFeed(@Nullable String name, String url,
			@Nullable String encode) {
		RCFeed feed = new RCFeed();
		if (!feed.setURL(url)) {
			return null;
		}
		feed.setName(name);
		if (encode != null) // 引数で指示があったら文字コードを指定
			feed.setEncoding(encode);
		// feed.run();
		return feed;
	}

	public RCFeed createFeed(@Nullable String name, String url) {
		return this.createFeed(name, url, null);
	}

}
