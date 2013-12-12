import java.awt.FlowLayout;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import lib.Item;

import org.w3c.dom.Node;

public class RCItem extends Item {

	private int num_recently;
	public RCItem(Node node) {
		super(node);
		this.num_recently = -1;
		// TODO Constracter
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
		if (this.num_recently != -1) return this.num_recently;
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
				(this.getDate().getMinutes()) / (60 / RCConfig.num_split_column_hour),
		};
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

	public JPanel getItemPanel() {
		JPanel pane = new JPanel(new FlowLayout());
		pane.setPreferredSize(RCConfig.underpane_size_dimension);

		JTextField title = new JTextField(this.getTitle());
		title.setPreferredSize(RCConfig.item_info_line);
		title.setBorder(RCConfig.item_info_title_border);
		pane.add(title);

		JTextField date = new JTextField(this.getDateString());
		date.setPreferredSize(RCConfig.item_info_line);
		date.setBorder(RCConfig.item_info_date_border);
		pane.add(date);

		if (this.getDescription() != null) {
			JTextPane description = getHTMLJTextPane(this.getDescription());
			description.setBorder(RCConfig.item_info_description_border);
			pane.add(description);
		}

		String url = this.getLink();
		JTextPane link = getHTMLJTextPane(String.format("<a href=\"%s\">%s</a>", url, url));
		link.setPreferredSize(RCConfig.item_info_line);
		link.setBorder(RCConfig.item_info_url_border);
		pane.add(link);
		return pane;
	}

	private JTextPane getHTMLJTextPane(String html) {
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		HTMLDocument doc = (HTMLDocument) textPane.getDocument();
		HTMLEditorKit editorKit = (HTMLEditorKit) textPane.getEditorKit();
		try {
			editorKit.insertHTML(doc, doc.getLength(), html, 0, 0, null);
		} catch (BadLocationException e) {
			// TODO catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO catch block
			e.printStackTrace();
		}
		return textPane;
	}
}
