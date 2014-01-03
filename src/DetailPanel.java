import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class DetailPanel extends JPanel {
	JTable table;
	DefaultTableModel model;

	public DetailPanel(RCItem item) {
		String[] columnNames = "要素名,値".split(",");
		model = new DefaultTableModel(columnNames, 0);
		table = new JTable(model);

		//@formatter:off
		String[] titleRow       = {"title"       , item.getTitle()};
		String[] dateRow        = {"date"        , item.getDateString()};
		String[] descriptionRow = {"description" , item.getDescription()};
		String[] linkRow        = {"link"        , item.getLink()};
		//@formatter:on
		model.addRow(titleRow);
		model.addRow(dateRow);
		model.addRow(descriptionRow);
		model.addRow(linkRow);

		HashMap<String, String> tagmap = item.getOtherTag();
		for (Entry<String, String> e : tagmap.entrySet()) {
			String[] row = {
					e.getKey(), e.getValue()
			};
			model.addRow(row);
		}
		table.getColumnModel().getColumn(1).setPreferredWidth(600);
		table.setEnabled(false);

		this.add(table);
	}
}
