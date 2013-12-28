import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class HomePanel extends JPanel {
	private ArrayList<RCItem> itemList;

	public HomePanel() {
		super();
		GridLayout gl = new GridLayout(0, RCConfig.num_panel_layout_column);
		gl.setVgap(5);
		gl.setHgap(5);
		this.setLayout(gl);
		this.setBorder(RCConfig.margin_border);
		this.itemList = new ArrayList<>();
	}

	public HomePanel(RCGroup group) {
		this();
		setGroup(group);
	}

	public void setGroup(RCGroup group) {
		this.removeAll();
		this.itemList.clear();
		for (RCFeed feed : group.getFeedList()) {
			System.out.println("f: " + feed.getName() + " [" + feed.getItemList().size());
			for (RCItem item : feed.getRCItemList()) {
				System.out.println("i: " + item.getTitle());
				itemList.add(item);
			}
		}
		for (RCItem item : itemList) {
			ItemPanel itemPane = new ItemPanel(item);
			this.add(itemPane);
		}
		this.setVisible(false);
		this.setVisible(true);
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	class ItemPanel extends JPanel {
		public ItemPanel(RCItem item) {
			this.setPreferredSize(RCConfig.itempane_size);
			this.setBackground(Color.orange);
			BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
			this.setLayout(bl);

			JPanel wrapPane = new JPanel(new BorderLayout());
			JPanel centerPane = new JPanel();
			GridPanel underPane = new GridPanel();
			underPane.setGridBagLayout(new GridBagLayout());

			wrapPane.add(centerPane, BorderLayout.CENTER);
			wrapPane.add(underPane, BorderLayout.SOUTH);

			JTextPane imgPane = new JTextPane();
			imgPane.setContentType("text/html");
			imgPane.setText(RCConfig.toImgTag(item.getImageUrl()));
			imgPane.setMaximumSize(RCConfig.item_imagepane);
			imgPane.setMinimumSize(RCConfig.item_imagepane);
			imgPane.setEditable(false);
			centerPane.add(imgPane);

			underPane.setGridBagLayout(new GridBagLayout());
			JTextPane titleLabel = new JTextPane();
			titleLabel.setText(item.getTitle());
			titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			JLabel dateLabel = new JLabel(item.getDateString());
			dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			OpenButton openButton = new OpenButton(item.getLink());
			underPane.addGridBag(titleLabel, 0, 0, 2, 1);
			underPane.addGridBag(dateLabel, 0, 1, 1, 1);
			underPane.addGridBag(openButton, 1, 1, 1, 1);

			this.add(wrapPane);
			this.setVisible(false);
			this.setVisible(true);
		}
	}

	class OpenButton extends JButton {
		public OpenButton(final String url) {
			try {
				final URI uri = new URI(url);
				this.setText("Open");
				this.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Desktop desktop = Desktop.getDesktop();
						try {
							desktop.browse(uri);
						} catch (IOException e1) {
							// TODO 自動生成された catch ブロック
							e1.printStackTrace();
						}
					}
				});
			} catch (URISyntaxException e2) {
				// TODO 自動生成された catch ブロック
				e2.printStackTrace();
			}
		}
	}

	class GridPanel extends JPanel {
		public GridBagLayout gbl;

		public void setGridBagLayout(GridBagLayout gbl) {
			this.gbl = gbl;
			this.setLayout(this.gbl);
		}

		public final void addGridBag(Component com, int x, int y, int w, int h) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = x;
			gbc.gridy = y;
			gbc.gridwidth = w;
			gbc.gridheight = h;
			gbl.setConstraints(com, gbc);
			add(com);
		}
	}
}
