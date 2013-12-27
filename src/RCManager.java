import java.util.ArrayList;

import com.sun.istack.internal.Nullable;

public class RCManager {
	private ArrayList<RCGroup> groupList;

	private int groupPointer;
	private RCFiler filer;

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
	public RCManager(String filename) {
		this.groupList = new ArrayList<RCGroup>();
		this.groupPointer = 0;
		this.filer = new RCFiler(filename, this);
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
		this.groupList.get(index).add(this.createFeed(name, url, null));
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

	/* --------------------------------------------------------- *
	 *     debug
	 * --------------------------------------------------------- */

	private void print() {
		System.out.println("RCManager----");
		for (RCGroup group : this.getGroupList()) {
			System.out.println(" group" + group.getId() + ":" + group.getName());
			for (RCFeed feed : group.getFeedList()) {
				System.out.println("  feed:" + feed.getName() + "\n   (" + feed.getUrl().toString() + ")");
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
