import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RCGroup {
	private int id;
	private String name;

	private ArrayList<RCFeed> feedList;

	//------------------- getter, setter start -------------------//
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<RCFeed> getFeedList() {
		return this.feedList;
	}

	//------------------- getter, setter end -------------------//

	public RCGroup(int id, String name) {
		this.id = id;
		this.name = name;
		this.feedList = new ArrayList<RCFeed>();
	}

	public int size() {
		return feedList.size();
	}

	public void add(RCFeed feed) {
		feed.setGroupId(this.id);
		this.feedList.add(feed);
	}

	public RCFeed findFeed(String name) {
		for (RCFeed feed : this.feedList) {
			if (name.equals(feed.getName()))
				return feed;
		}
		return null;
	}

	public ArrayList<String> feedNameList() {
		ArrayList<String> nameList = new ArrayList<>();
		for (RCFeed feed : this.getFeedList())
			nameList.add(feed.getName());
		return nameList;
	}

	public void runAll() {
//		long start = System.currentTimeMillis();
		ExecutorService threadPool = Executors.newFixedThreadPool(8);
		Collection<Callable<Void>> processes = new LinkedList<Callable<Void>>();
		for (RCFeed feed : this.feedList) {
			//			System.out.println("RunNow :" + feed.getName());
			final RCFeed feed0 = feed;
			processes.add(new Callable<Void>() {
				@Override
				public Void call() {
					feed0.run();
					return null;
				}
			});
		}
		try {
			threadPool.invokeAll(processes);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			threadPool.shutdown();
		}
		//		System.out.println("RunProcesseTime: " + (System.currentTimeMillis() - start));
	}

	public boolean remove(RCFeed feed) {
		return this.feedList.remove(feed);
	}

	@Deprecated
	public void reRunAll() {
		for (RCFeed feed : this.feedList) {
			feed.reSet();
		}
		this.runAll();
	}

}
