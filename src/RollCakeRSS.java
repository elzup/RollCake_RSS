import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class RollCakeRSS extends JFrame {
	RCManager manager;
	NaviPanel naviPane;
	RightPanel rightPane;
	TabPanel mainPane;
	HomePanel homePane;
	DefaultListModel<String> feedListModel;
	JList<String> feedList;
	JComboBox<String> groupBox;

	public static void main(String... args) {
		System.out.println("main start");

		RollCakeRSS cake = new RollCakeRSS();

		cake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cake.setBounds(10, 10, RCConfig.window_size_width, RCConfig.window_size_height);
		cake.setTitle(RCConfig.title_frame);
		cake.setVisible(true);

		System.out.println("main end");

		// ------------------- play -------------------//
		// ------------------- play end -------------------//

	}

	RollCakeRSS() {
		// ------------------- construct model -------------------//
		this.manager = new RCManager();
		this.manager.load();

		// ------------------- gui putting -------------------//
		JPanel pane = (JPanel) this.getContentPane();
		pane.setLayout(new BorderLayout());
		mainPane = new TabPanel();
		naviPane = new NaviPanel();
		rightPane = new RightPanel();
		pane.add(naviPane, BorderLayout.PAGE_START);
		pane.add(mainPane, BorderLayout.CENTER);
		pane.add(rightPane, BorderLayout.EAST);

		try {
			this.setupWindowConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setupMenuBar();

		// ------------------- naviPane -------------------//

		// ------------------- mainPane -------------------//
		// ------------------- rightPane -------------------//

		// ------------------- debug initialize -------------------//
		// for (String[] feed : Debug.DEBUG_URLS) {
		// this.fm.addFeed(feed[0], feed[1], null);
		// }
		// ------------------- debug end -------------------//
	}

	private void setupWindowConfig() throws Exception {
		UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[RCConfig.window_id_lookandfeel].getClassName());
	}

	private void setupToolBar() {
		// JToolBar tb = new JToolBar();
	}

	private void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu file = new JMenu("File");
		menuBar.add(file);
		JMenuItem itemExit = new JMenuItem("Exit");
		file.add(itemExit);
	}

	/*
	 * --------------------------------------------------------- * NavPanel
	 * ---------------------------------------------------------
	 */

	class NaviPanel extends JPanel {
		private final String startPage = "http://www.google.co.jp/";
		private JTextField addressBar;

		public NaviPanel() {
			super();
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			addressBar = new JTextField();
			addressBar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mainPane.openWebPage(addressBar.getText());
				}
			});
			// mainPane.openWebPage(startPage);
			mainPane.setupHome();
			this.add(addressBar);
		}

		public void setUrl(String url) {
			addressBar.setText(url);
		}
	}

	/*
	 * --------------------------------------------------------- * TabPanel
	 * ---------------------------------------------------------
	 */
	class TabPanel extends JTabbedPane {
		public TabPanel() {
			super();
		}

		public void addTabPanel(String tabname, final JComponent component) {
			this.addTab(null, component);
			JPanel tab = new JPanel(new FlowLayout());
			JButton closeButton = new JButton("×");
			closeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					removeTabAt(indexOfComponent(component));
				}
			});
			tab.add(new JLabel(tabname));
			tab.add(closeButton);
			this.setTabComponentAt(this.getTabCount() - 1, tab);
		}

		public void addTabPanelDeletable(String tabname, final JComponent component) {
			this.addTabPanel(tabname, component);
			JPanel tab = new JPanel(new FlowLayout());
			tab.add(new JLabel(tabname));
			this.setTabComponentAt(this.getTabCount() - 1, tab);
		}

		public void setupHome() {
			homePane = new HomePanel(manager.getActiveGroup());
			this.addTabPanelDeletable("Home", homePane);
		}

		public void openWebPage(String url) {
			try {
				final JEditorPane pane = new JEditorPane();
				pane.setPage(url);
				pane.addHyperlinkListener(new HyperlinkListener() {
					@Override
					public void hyperlinkUpdate(HyperlinkEvent e) {
						if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
							return;
						String url = e.getURL().toString();
						try {
							pane.setPage(url);
						} catch (IOException e1) {
							addTabPanelDeletable(url, new WebErrorPnael(e1));
						}
						naviPane.setUrl(url);
					}
				});
				pane.setEditable(false);
				this.addTabPanelDeletable(url, new JScrollPane(pane));
			} catch (IOException e) {
				this.addTabPanelDeletable(url, new WebErrorPnael(e));
			}

		}
	}

	/*
	 * --------------------------------------------------------- * RightPanel
	 * ---------------------------------------------------------
	 */
	class RightPanel extends JPanel {
		public JPanel feedListPane;
		public JPanel layoutButtons;
		public JPanel configButtons;

		public RightPanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			// ------------------- layoutButtons -------------------//
			layoutButtons = new JPanel(new GridLayout(1, 3));
			JButton panelsLayout = new JButton("Panel");
			JButton tabelsLayout = new JButton("Table");
			JButton imagesLayout = new JButton("Image");
			layoutButtons.add(panelsLayout);
			layoutButtons.add(tabelsLayout);
			layoutButtons.add(imagesLayout);
			this.add(layoutButtons);

			// ------------------- groupBox -------------------//
			String[] nameList = new String[manager.groupNameList().size()];
			for (RCGroup group : manager.getGroupList())
				nameList[group.getId()] = group.getName();
			groupBox = new JComboBox<String>(nameList);
			groupBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					changeGroup(groupBox.getSelectedIndex());
				}
			});
			this.add(groupBox);

			// ------------------- feedList -------------------//
			feedListPane = new JPanel();
			this.add(feedListPane);
			this.changeGroup(0);

			configButtons = new JPanel(new GridLayout(1, 2));
			JButton editBtn = new JButton("Edit");
			JButton deleteBtn = new JButton("Delete");
			configButtons.add(editBtn);
			configButtons.add(deleteBtn);
			this.add(configButtons);

			this.setupFeel();

		}

		public void setupFeel() {
			this.setPreferredSize(RCConfig.rightpane_size_dimension);
			this.setBackground(RCConfig.rightpane_background_color);
			layoutButtons.setMaximumSize(RCConfig.layout_button_box);
			layoutButtons.setMinimumSize(RCConfig.layout_button_box);
			layoutButtons.setBorder(RCConfig.margin_border);
			layoutButtons.setBackground(Color.red);
			((GridLayout) layoutButtons.getLayout()).setHgap(5);
			((GridLayout) layoutButtons.getLayout()).setVgap(5);
			for (Component button : layoutButtons.getComponents()) {
				if (button instanceof JButton) {
					button.setBackground(RCConfig.button_back_color);
				}
			}

			groupBox.setMaximumSize(RCConfig.group_comb);
			groupBox.setMinimumSize(RCConfig.group_comb);
			groupBox.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

			feedListPane.setBackground(Color.blue);
			feedListPane.setBorder(RCConfig.margin_border);
			for (Component button : feedListPane.getComponents()) {
				if (button instanceof JToggleButton) {
					button.setBackground(RCConfig.button_back_color);
					button.setPreferredSize(RCConfig.feedlist_button_size);
				}
			}

			configButtons.setMaximumSize(RCConfig.layout_button_box);
			configButtons.setMinimumSize(RCConfig.layout_button_box);
			configButtons.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
			configButtons.setBackground(Color.red);
			((GridLayout) configButtons.getLayout()).setHgap(5);
			((GridLayout) configButtons.getLayout()).setVgap(5);
			for (Component button : configButtons.getComponents()) {
				if (button instanceof JButton) {
					button.setBackground(RCConfig.button_back_color);
				}
			}
		}

		public void changeGroup(int groupId) {
			if (groupId == -1)
				return;
			ArrayList<RCGroup> groupList = manager.getGroupList();
			RCGroup group = groupList.get(groupId);
			feedListPane.removeAll();
			for (RCFeed feed : group.getFeedList()) {
				System.out.println(":" + feed.getName());
				JToggleButton tb = new JToggleButton(feed.getName());
				feedListPane.add(tb);
			}
			feedListPane.setVisible(false);
			feedListPane.setVisible(true);
		}
	}
}
