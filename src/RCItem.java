import java.util.Calendar;
import java.util.Date;

import lib.Item;

import org.w3c.dom.Node;


public class RCItem extends Item{
	public RCItem(Node node) {
		super(node);
		// TODO Constracter
	}

	public void compact() {
		this.description = null;
	}

	public int getRecentryNum() {
		long diff = this.getDiffToday(this.getDate());
		if (diff > 60 * 60 * 24 * 3 * 1000) {
			return -1;
		}
		return (int) (diff / (60 * 60 * 24 * 1000));
	}

	public long getDiffToday(Date d) {
		return Calendar.getInstance().getTime().getTime() - d.getTime();
	}
}

