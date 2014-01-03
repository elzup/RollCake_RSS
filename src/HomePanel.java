import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

@SuppressWarnings("serial")
public class HomePanel extends JPanel {
	private ArrayList<ItemPanel> itemPaneList;
	private String searchRegex;

	public HomePanel() {
		super();
		GridLayout gl = new GridLayout(0, RCConfig.num_panel_layout_column);
		gl.setVgap(5);
		gl.setHgap(5);
		this.setLayout(gl);
		//		FlowLayout fl = new FlowLayout();
		//		fl.setVgap(5);
		//		fl.setHgap(5);
		//		this.setLayout(fl);		//まだ諦めるような時間じゃない
		this.setBackground(RCConfig.home_back_color);
		this.setBorder(RCConfig.margin_border);
		this.itemPaneList = new ArrayList<>();
	}

	public HomePanel(RCGroup group) {
		this();
		setGroup(group);
	}

	public void setGroup(RCGroup group) {
		this.setGroup(group, null);
	}

	public void setGroup(RCGroup group, JViewport view) {
		this.removeAll();
		this.itemPaneList.clear();
		for (RCFeed feed : group.getFeedList()) {
			System.out.println("-" + feed.getName());
			for (RCItem item : feed.getRCItemList()) {
				//				System.out.println(item);
				System.out.println(" -" + item.getTitle());
				item.setColor(feed.getColor());
				item.setFeedId(group.getFeedList().indexOf(feed));
				ItemPanel itemPane = new ItemPanel(item);
				itemPaneList.add(itemPane);
			}
		}
		this.timeSort();
		this.reloadSafe(view);
		if (view != null)
			view.setViewPosition(new Point(0, 0));
	}

	public void reloadInit() {

	}

	public void crawl(String regex) {
		this.searchRegex = regex;

		long start = System.currentTimeMillis();
		ExecutorService threadPool = Executors.newFixedThreadPool(8);
		Collection<Callable<Void>> processes = new LinkedList<Callable<Void>>();
		for (ItemPanel ip : this.itemPaneList) {
			final ItemPanel ip0 = ip;
			processes.add(new Callable<Void>() {
				@Override
				public Void call() {
					ip0.updateFind();
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
		System.out.println("RunProcesseTime: " + (System.currentTimeMillis() - start));
		this.findSort();

	}

	public void closeFind() {
		for (ItemPanel ip : this.itemPaneList) {
			ip.closeFind();
		}
	}

	public void reloadSafe(JViewport view) {
		this.removeAll();
		for (ItemPanel pane : itemPaneList) {
			if (!pane.isDisplay())
				continue;
			this.add(pane);
			if (view != null)
				view.setViewPosition(new Point(0, 0));
		}
		this.setVisible(false);
		this.setVisible(true);
	}

	public void reload() {
		this.reloadSafe(null);
	}

	public void offItem(int id) {
		this.changeItem(id, false);
	}

	public void onItem(int id) {
		this.changeItem(id, true);
	}

	static final int ALL_ITEM = -1;

	public void offItemAll() {
		this.changeItem(ALL_ITEM, false);
	}

	public void onItemAll() {
		this.changeItem(ALL_ITEM, true);
	}

	// all -> -1
	void changeItem(int id, boolean state) {
		for (ItemPanel pane : this.itemPaneList) {
			if (id == ALL_ITEM || pane.getId() == id) {
				pane.setDisplay(state);
			}
		}
		this.reload();
	}

	public void findSort() {
		Collections.sort(itemPaneList, new Comparator<ItemPanel>() {
			@Override
			public int compare(ItemPanel o1, ItemPanel o2) {
				String s1 = o1.getFoundValue();
				String s2 = o2.getFoundValue();
				if (RCConfig.no_match_text.equals(s1)) return -1;

				int r = 0;
				try {
					s1 = s1.replace(",", "");
					s2 = s2.replace(",", "");
					Integer i1 = new Integer(s1);
					Integer i2 = new Integer(s2);
					r =i2.compareTo(i1);
				} catch (NumberFormatException e) {
					r = s2.compareTo(s1);
				}

				return r;
			}
		});
		this.reload();
	}

	public void timeSort() {
		Collections.sort(itemPaneList, new Comparator<ItemPanel>() {
			@Override
			public int compare(ItemPanel o1, ItemPanel o2) {
				Date d = o2.getItem().getDate();
				return (d == null) ? -1 : d.compareTo(o1.getItem().getDate());
			}
		});
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

	class ItemPanel extends JPanel {
		private int feedId;
		private JTextField foundBox;
		private boolean display;
		private RCItem item;

		// ------------------- getter, setter -------------------//
		public int getId() {
			return this.feedId;
		}

		public void setDisplay(boolean b) {
			this.display = b;
		}

		public boolean isDisplay() {
			return this.display;
		}

		public RCItem getItem() {
			return this.item;
		}

		public void setItem(RCItem item) {
			this.item = item;
		}

		public String getFoundValue() {
			return this.foundBox.getText();
		}

		// ------------------- getter, setter end -------------------//
		public ItemPanel(RCItem items) {
			this.item = items;
			this.feedId = item.getFeedId();
			this.display = true;

			this.setPreferredSize(RCConfig.itempane_size);
			this.setMaximumSize(RCConfig.itempane_size);
			this.setMinimumSize(RCConfig.itempane_size);
			this.setBackground(Color.white);
			//			BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);

			this.setBorder(new MatteBorder(new Insets(0, 0, 12, 0), item.getColor()));

			JPanel wrapPane = new JPanel();
			wrapPane.setMaximumSize(RCConfig.itempane_size);
			wrapPane.setMinimumSize(RCConfig.itempane_size);
			this.add(wrapPane);
			JPanel centerPane = new JPanel();
			centerPane.setBackground(RCConfig.itempane_back_color);
			centerPane.setBorder(null);
			GridPanel underPane = new GridPanel();
			wrapPane.setLayout(new BoxLayout(wrapPane, BoxLayout.Y_AXIS));
			underPane.setGridBagLayout(new GridBagLayout());
			wrapPane.setBackground(RCConfig.itempane_back_color);

			this.foundBox = new JTextField();
			wrapPane.add(centerPane);
			wrapPane.add(underPane);

			JTextPane imgPane = new JTextPane();
			imgPane.setMargin(new Insets(0, 0, 0, 0));
			imgPane.setBackground(Color.white);
			imgPane.setContentType("text/html");
			imgPane.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					JOptionPane.showMessageDialog((Component) e.getSource(), new DetailPanel(item));
				}
			});
			String imgUrl = item.getImageUrl();
			System.out.println("," + imgUrl);
			imgPane.setText(RCConfig.toImgTag(imgUrl));
			//			if (imgUrl != null) {
			//				imgPane.setText(RCConfig.toImgTag(imgUrl));
			//			} else {
			//				imgPane.setText(RCConfig.no_image);
			//			}
			imgPane.setEditable(false);
			centerPane.setMaximumSize(RCConfig.item_imagepane);
			centerPane.setMinimumSize(RCConfig.item_imagepane);
			centerPane.setMinimumSize(RCConfig.item_imagepane);
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
			dateLabel.setBorder(new EmptyBorder(RCConfig.button_insets));
			OpenButton openButton = new OpenButton(item.getLink());
			//			underPane.addGridBag(titleLabel, 0, 0, 2, 1);
			//			underPane.addGridBag(dateLabel, 0, 1, 1, 1);
			//			underPane.addGridBag(openButton, 1, 1, 1, 1);
			openButton.setMaximumSize(RCConfig.item_brows_button);
			openButton.setBackground(RCConfig.button_back_color);
			underPane.addGridBag(titleLabel, 0, 0, 3, 2);
			underPane.addGridBag(this.foundBox, 0, 2, 3, 1);
			underPane.addGridBag(dateLabel, 0, 3, 2, 1);
			underPane.addGridBag(openButton, 2, 3, 1, 1);

			this.foundBox.setVisible(false);
			this.setVisible(false);
			this.setVisible(true);
		}

		public void updateFind() {
			String find = this.item.crowl(searchRegex);
			if (find == null)
				find = RCConfig.no_match_text;
			this.foundBox.setText(find);
			this.foundBox.setVisible(true);
		}

		public void closeFind() {
			this.foundBox.setVisible(false);
		}
	}
}
