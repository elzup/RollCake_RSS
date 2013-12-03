import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lib.Item;

import org.w3c.dom.Node;


public class RCItem extends Item{

	private int id_recently;
	public RCItem(Node node) {
		super(node);
		// TODO Constracter
	}

	public void compact() {
		this.description = null;
	}

	public int getRecentryNum() {
		long diff = this.getDiffToday(this.getDate());
		if (diff > 60 * 60 * 24 * RCConfig.num_day_recentry * 1000) {
			return -1;
		}
		return (int) (diff / (60 * 60 * 24 * 1000));
	}

	public long getDiffToday(Date d) {
		return Calendar.getInstance().getTime().getTime() - d.getTime();
	}

	public void setIdRecently(int id) {
		this.id_recently = id;
	}

	public JPanel getItemPanel() {
		JPanel pane = new JPanel(new GridLayout(4, 0));
		JLabel title = new JLabel(this.getDescription());
		JLabel date = new JLabel(this.getDateString());
		JLabel description = new JLabel(this.getDescription());
		String url = this.getLink();
		JLabel link = new JLabel(String.format("<a href=\"%s\">%s</a>", url, url));

		pane.add(title);
		pane.add(date);
		pane.add(description);
		pane.add(link);
		return pane;
	}
}


