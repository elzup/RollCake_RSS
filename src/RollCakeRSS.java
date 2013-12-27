import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class RollCakeRSS extends JFrame {
	RCManager manager;
	NaviPanel naviPane;
	RightPanel rightPane;
	TabPanel mainPane;
	DefaultListModel<String> feedListModel;
	JList<String> feedList;
	JComboBox<String> groupBox;

	public static void main(String... args) {
		System.out.println("main start");

		RollCakeRSS cake = new RollCakeRSS();

		cake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cake.setBounds(10, 10, RCConfig.window_size_width,
				RCConfig.window_size_height);
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
		UIManager
				.setLookAndFeel(UIManager.getInstalledLookAndFeels()[RCConfig.window_id_lookandfeel]
						.getClassName());
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
			addressBar = new JTextField(startPage);
			mainPane.openWebPage(startPage);
			addressBar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mainPane.openWebPage(addressBar.getText());
				}
			});
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

		public void openWebPage(String url) {
			final JEditorPane pane = new JEditorPane();
			try {
				pane.setPage(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
			pane.addHyperlinkListener(new HyperlinkListener() {
				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
						return;
					String url = e.getURL().toString();
					try {
						pane.setPage(url);
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
					naviPane.setUrl(url);
				}
			});

			pane.setEditable(false);
			this.addTabPanel(url.substring(0, 20), new JScrollPane(pane));
		}
	}

	/*
	 * --------------------------------------------------------- * RightPanel
	 * ---------------------------------------------------------
	 */
	class RightPanel extends JPanel {
		public RightPanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			// ------------------- layoutButtons -------------------//
			JPanel layoutButtons = new JPanel(new GridLayout(1, 3));
			JButton panelsLayout = new JButton("Panel");
			JButton tabelsLayout = new JButton("Table");
			JButton imagesLayout = new JButton("Image");
			layoutButtons.add(panelsLayout);
			layoutButtons.add(tabelsLayout);
			layoutButtons.add(imagesLayout);
			this.add(layoutButtons);

			// ------------------- groupChooser-------------------//
			String[] nameList = manager.groupNameList().toArray(new String[manager.groupNameList().size()]);
			groupBox = new JComboBox<String>(nameList);
			this.add(groupBox);

			// ------------------- feedList -------------------//
			ArrayList<RCGroup> groupList = manager.getGroupList();
			RCGroup group = groupList.get(groupBox.getSelectedIndex());
			nameList = group.feedNameList().toArray(new String[group.feedNameList().size()]);
			JList<String> feedList = new JList<>(nameList);
			this.add(feedList);


		}
	}
}
