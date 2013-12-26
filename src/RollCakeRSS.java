import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class RollCakeRSS extends JFrame {
	RCManager fm;
	JPanel detailPane, tablePane, rightPane;
	DefaultListModel<String> feedListModel;
	JList<String> feedList;
	int group;

	public static void main(String... args) {
		System.out.println("main start");

		RollCakeRSS cake = new RollCakeRSS();

		cake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cake.setBounds(10, 10, RCConfig.window_size_width, RCConfig.window_size_height);
		cake.setTitle(RCConfig.title_frame);
		cake.setVisible(true);

		System.out.println("main end");

		//------------------- play -------------------//
		//------------------- play end -------------------//

	}

	RollCakeRSS() {

		this.setupWindowConfig();
		this.setupToolBar();
		this.setupMenuBar();
		//		Container cp = this.getContentPane();
		this.fm = new RCManager();

		RCFiler.loadFeedList(fm);
		//------------------- debug initialize -------------------//
		//		for (String[] feed : Debug.DEBUG_URLS) {
		//			this.fm.addFeed(feed[0], feed[1], null);
		//		}
		//------------------- debug end -------------------//
		this.group = 0;
		this.updateTable();
	}

	private void setupWindowConfig() {
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[RCConfig.window_id_lookandfeel]
					.getClassName());
		} catch (ClassNotFoundException e) {
			// TODO catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO catch block
			e.printStackTrace();
		}
	}

	private void setupToolBar() {
		//		JToolBar tb = new JToolBar();
	}

	private void setupMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu file = new JMenu("File");
		menuBar.add(file);
		JMenuItem itemExit = new JMenuItem("Exit");
		file.add(itemExit);
	}

	public void updateTable() {
		JPanel pane = (JPanel) this.getContentPane();
		pane.setLayout(new BorderLayout());
		pane.setBackground(RCConfig.window_background_color);

		JPanel leftPane = new JPanel(new BorderLayout());
		leftPane.setPreferredSize(new Dimension(RCConfig.tablepane_size_width, RCConfig.window_size_height));

		//		pane.add(this.getTopPane(), BorderLayout.PAGE_START);
		pane.add(leftPane, BorderLayout.CENTER);

		detailPane = new JPanel(new BorderLayout());
		detailPane.setPreferredSize(RCConfig.underpane_size_dimension);
		detailPane.setBackground(RCConfig.underpane_background_color);
		fm.setContentPane(pane);

		leftPane.add(detailPane, BorderLayout.SOUTH);

		tablePane = fm.setTablePane();

		rightPane = getRightPane();
		pane.add(rightPane, BorderLayout.EAST);
		rightPane.setPreferredSize(RCConfig.rightpane_size_dimension);
		rightPane.setBackground(RCConfig.rightpane_background_color);
	}

	public JPanel getTopPane() {
		JPanel pane = new JPanel(new GridLayout(1, 5));
		pane.setBackground(RCConfig.rightpane_background_color);
		return pane;
	}

	public JPanel getRightPane() {
		JPanel pane = new JPanel(new BorderLayout());
		pane.setBackground(RCConfig.rightpane_background_color);
		JPanel manaPane = this.getManagerPane();
		JPanel listPane = new JPanel();
		listPane.setBackground(RCConfig.rightpane_background_color);
		this.feedList = fm.getFeedJList();
		JScrollPane sp = new JScrollPane(this.feedList);
		sp.setPreferredSize(RCConfig.rightpane_list_size_dimension);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		listPane.add(sp);
		this.feedListModel = (DefaultListModel<String>) feedList.getModel();

		pane.add(manaPane, BorderLayout.NORTH);
		pane.add(listPane, BorderLayout.CENTER);
		return pane;
	}

	public JPanel getManagerPane() {
		JPanel pane = new JPanel(new GridLayout(2, 1));
		JButton addButton = new JButton(new AddFeedAction("Feed繧堤匳骭ｲ縺吶ｋ"));
		JButton updButton = new JButton(new UpdateFeedAction("Feed繧堤ｷｨ髮�☆繧�));
		//		addButton.setPreferredSize(RCConfig.rightbutton_dimension);
		//		updButton.setPreferredSize(RCConfig.rightbutton_dimension);
		addButton.setBackground(RCConfig.rightbutton_back_color);
		updButton.setBackground(RCConfig.rightbutton_back_color);
		addButton.setForeground(RCConfig.rightbutton_font_color);
		updButton.setForeground(RCConfig.rightbutton_font_color);
		addButton.setBorder(RCConfig.rightbutton_border);
		updButton.setBorder(RCConfig.rightbutton_border);
		pane.add(addButton);
		pane.add(updButton);
		return pane;
	}

	class AddFeedAction extends AbstractAction {
		AddFeedAction(String text) {
			super(text);
		}

		public void actionPerformed(ActionEvent e) {
			Object[] msg = { "逋ｻ骭ｲ縺励◆縺ФRL繧定ｲｼ繧贋ｻ倥￠縺ｦ荳九＆縺� };
			String ans = JOptionPane.showInputDialog(rightPane, msg, "RSS縺ｮ逋ｻ骭ｲ",
					JOptionPane.INFORMATION_MESSAGE);
			System.out.println(ans);
			if (ans == null)
				return;
			RCFeed feed = fm.createFeed(ans, null);
			if (feed == null) {
				String[] msg2 = {
						"Feed繧貞叙蠕励〒縺阪∪縺帙ｓ縺ｧ縺励◆",
						"URL縺碁俣驕輔▲縺ｦ縺�↑縺�°遒ｺ隱阪＠縺ｦ荳九＆縺�,
						"[" + ans + "]",
				};
				JOptionPane.showMessageDialog(rightPane, msg2, "螟ｱ謨�,
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			feed.run();
			Object[] msg2 = { "逋ｻ骭ｲ縺吶ｋRSS縺ｮ蜷榊燕繧偵▽縺代※荳九＆縺�, "[" + ans + "]" };
			String ans_name = JOptionPane.showInputDialog(rightPane, msg2, feed.getTempName());
			System.out.println(ans);
			if (ans_name == null)
				return;
			feed.setName(ans_name);
			feed.setGroupId(group);
			feedListModel.addElement(feed.getName());
			fm.addFeed(feed);
			fm.setTablePane();
			RCFiler.saveFeedList(fm);
			System.out.println(ans);
		}
	}

	class UpdateFeedAction extends AbstractAction {
		Color col;
		UpdateFeedAction(String text) {
			super(text);
		}

		public void actionPerformed(ActionEvent e) {
			String name = feedList.getSelectedValue();
			RCFeed feed = fm.getFeed(name);
			JPanel feedSettingPane = new JPanel();
			feedSettingPane.setLayout(new BoxLayout(feedSettingPane, BoxLayout.Y_AXIS));
			JTextField title = new JTextField(name);
			col = feed.getColor();
			JButton ccButton = new JButton();
			ccButton.setBackground(col);
			ccButton.addActionListener(new ColorChooseAction());

			feedSettingPane.add(title);
			feedSettingPane.add(ccButton);

			Object[] msg = {
					"逋ｻ骭ｲ縺輔ｌ縺ｦ縺�ｋFeed繧堤ｷｨ髮�＠縺ｾ縺�,
					feedSettingPane,
			};
			JOptionPane.showMessageDialog(rightPane, msg, "Feed",
					JOptionPane.INFORMATION_MESSAGE);
			feed.setColor(col);
		}

		class ColorChooseAction extends AbstractAction {
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser cc = new JColorChooser();
				col = cc.showDialog(rightPane, "繧ｫ繝ｩ繝ｼ螟画峩", col);
			}
		}
	}
}
