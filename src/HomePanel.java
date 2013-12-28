import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import javax.swing.border.MatteBorder;

@SuppressWarnings("serial")
public class HomePanel extends JPanel {
	private ArrayList<RCItem> itemList;

	public HomePanel() {
		super();
		GridLayout gl = new GridLayout(0, RCConfig.num_panel_layout_column);
		gl.setVgap(5);
		gl.setHgap(5);
		this.setLayout(gl);
		//		FlowLayout fl = new FlowLayout();
		//		fl.setVgap(5);
		//		fl.setHgap(5);
		//		this.setLayout(fl);
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
				item.setColor(feed.getColor());
				item.setFeedId(group.getFeedList().indexOf(feed));
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
		private int feedId;
		public int getId () {
			return this.feedId;
		}

		public ItemPanel(RCItem item) {
			this.setPreferredSize(RCConfig.itempane_size);
			this.setMaximumSize(RCConfig.itempane_size);
			this.setMinimumSize(RCConfig.itempane_size);
			this.setBackground(Color.white);
//			BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);

			this.feedId = item.getFeedId();
			this.setBorder(new MatteBorder(new Insets(1, 1, 1, 10), item.getColor()));

			JPanel wrapPane = new JPanel();
			JPanel centerPane = new JPanel();
			GridPanel underPane = new GridPanel();
			wrapPane.setLayout(new BoxLayout(wrapPane, BoxLayout.Y_AXIS));
			underPane.setGridBagLayout(new GridBagLayout());

			wrapPane.add(centerPane);
			wrapPane.add(underPane);

			JTextPane imgPane = new JTextPane();
			imgPane.setContentType("text/html");
			imgPane.setText(RCConfig.toImgTag(item.getImageUrl()));
			imgPane.setMaximumSize(RCConfig.item_imagepane);
			imgPane.setMinimumSize(RCConfig.item_imagepane);
			imgPane.setEditable(false);
			centerPane.add(imgPane);

			underPane.setGridBagLayout(new GridBagLayout());
			underPane.setBackground(Color.white);
			underPane.setMaximumSize(RCConfig.item_underpane);
			underPane.setMinimumSize(RCConfig.item_underpane);
			JTextPane titleLabel = new JTextPane();
			titleLabel.setText(item.getTitle());
			titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			titleLabel.setPreferredSize(RCConfig.item_titlepane);
			titleLabel.setEditable(false);
			//			titleLabel.setMaximumSize(RCConfig.item_titlepane);
			//			titleLabel.setMinimumSize(RCConfig.item_titlepane);
			JLabel dateLabel = new JLabel(item.getDateString());
			dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			OpenButton openButton = new OpenButton(item.getLink());
			//			underPane.addGridBag(titleLabel, 0, 0, 2, 1);
			//			underPane.addGridBag(dateLabel, 0, 1, 1, 1);
			//			underPane.addGridBag(openButton, 1, 1, 1, 1);
			underPane.addGridBag(titleLabel, 0, 0, 3, 2);
			underPane.addGridBag(dateLabel, 0, 2, 2, 1);
			underPane.addGridBag(openButton, 2, 2, 1, 1);

			this.add(wrapPane);
			this.setVisible(false);
			this.setVisible(true);
		}

	}

	public void offItem(int id) {
		this.changeItem(id, false);
	}
	public void onItem(int id) {
		this.changeItem(id, true);
	}

	void changeItem(int id, boolean state) {
		for (Component itemPane : this.getComponents()) {
			if (!(itemPane instanceof ItemPanel)) continue;
			if ( ((ItemPanel) itemPane).getId()  == id) {
				itemPane.setVisible(state);
			}
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
