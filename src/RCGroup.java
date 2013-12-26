import java.util.ArrayList;

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
	//------------------- getter, setter end -------------------//

	public RCGroup(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int size() {
		return feedList.size();
	}

	public void Feed() {
	}
}

