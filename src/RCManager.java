import java.awt.Color;
import java.util.ArrayList;

import com.sun.istack.internal.NotNull;

public class RCManager {
	private ArrayList<RCGroup> groupList;

	private int groupPointer;
	private RCFiler filer;
	private ArrayList<String> crawledHistory;

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

	public ArrayList<String> getCrawledHistory() {
		return this.crawledHistory;
	}

	// ------------------- getter, setter, end -------------------//
	public RCManager(String filename) {
		this.groupList = new ArrayList<RCGroup>();
		this.groupPointer = 0;
		this.filer = new RCFiler(filename, this);
		this.crawledHistory = new ArrayList<>();
	}

	public RCManager() {
		this(RCConfig.savefile_name);
	}

	public void save() {
		this.filer.saveFeedList();
	}

	public void load() {
		this.filer.loadFeedList();
	}

	public void addGroup(int id, String name) {
		this.groupList.add(new RCGroup(id, name));
	}

	public void addGroup(RCGroup group) {
		this.groupList.add(group);
	}

	public void addFeed(String name, String url, int index) {
		this.groupList.get(index).add(this.createFeed(name, url));
	}

	public void addFeed(String name, String url, int index, Color color) {
		this.groupList.get(index).add(this.createFeed(name, url, color));

	}

	public void addFeed(String name, String url) {
		this.addFeed(name, url, groupPointer);
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

	public void runAll() {
		for (RCGroup group : this.getGroupList())
			group.runAll();
	}

	public RCFeed createFeed(String name, String url, String encode, Color color) {
		RCFeed feed = new RCFeed();
		if (!feed.setURL(url)) {
			return null;
		}
		feed.setName(name);
		feed.setColor(color);
		if (encode != null)
			feed.setEncoding(encode);
		return feed;
	}

	public RCFeed createFeed(String name, String url, @NotNull Color color) {
		return this.createFeed(name, url, null, color);
	}

	public RCFeed createFeed(String name, String url, String encode) {
		return this.createFeed(name, url, encode, RCConfig.no_color);
	}

	public RCFeed createFeed(String name, String url) {
		return this.createFeed(name, url, null, RCConfig.no_color);
	}

	public ArrayList<String> groupNameList() {
		ArrayList<String> nameList = new ArrayList<>();
		for (RCGroup group : this.getGroupList())
			nameList.add(group.getName());
		return nameList;
	}

	public RCGroup getActiveGroup() {
		return this.groupList.get(this.groupPointer);
	}

	public Object getContent(int superIndex) {
		int i = 0;
		for (RCGroup group : this.getGroupList()) {
			if (i++ == superIndex)
				return group;
			for (RCFeed feed : group.getFeedList())
				if (i++ == superIndex)
					return feed;
		}
		return null;
	}

	public boolean removeFeed(RCFeed feed, int groupId) {
		return this.groupList.get(groupId).remove(feed);
	}

	public boolean removeFeed(RCFeed feed) {
		for (RCGroup group : this.groupList)
			if ( group.remove(feed))return true;
		return false;
	}

	public void moveFeedTo(RCFeed feed, int groupId) {
		for (RCGroup group : this.groupList)
			if ( group.remove(feed))break;
		this.groupList.get(groupId).add(feed);
	}

	public void addCrawledHistory(String e) {
		this.crawledHistory.add(e);
	}

	/* --------------------------------------------------------- *
	 *     debug
	 * --------------------------------------------------------- */
	@Deprecated
	void print() {
		System.out.println("RCManager----");
		for (RCGroup group : this.getGroupList()) {
			System.out.println(" group" + group.getId() + ":" + group.getName());
			for (RCFeed feed : group.getFeedList()) {
				System.out.println("  feed:" + feed.getName() + "\n   (" + feed.getUrl().toString() + ")");
				for (RCItem item : feed.getRCItemList()) {
					System.out.println("   item:" + item.getTitle());
				}
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		RCManager manager = new RCManager();
		manager.load();
		manager.print();
		manager.save();
	}
}
