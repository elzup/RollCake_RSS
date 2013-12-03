import java.util.ArrayList;

import lib.Item;


public class Tile {
	private ArrayList<Item> listItem;

	public Tile() {
		this.listItem = new ArrayList<Item>();
	}

	public ArrayList<Item> getItems () {
		return this.listItem;
	}
	public void addItem(Item e) {
		this.listItem.add(e);
	}

	public int size() {
		return this.listItem.size();
	}
}
