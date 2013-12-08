import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class RollCakeRSS extends JFrame {
	RCManager fm;
	JPanel detailPane, tablePane, rightPane;
	int group;

	public static void main(String... args) {
		System.out.println("main start");

		RollCakeRSS cake = new RollCakeRSS();

		cake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cake.setBounds(10, 10, RCConfig.window_size_width, RCConfig.window_size_height);
		cake.setTitle(RCConfig.title_frame);
		cake.setVisible(true);

		System.out.println("main end");
	}

	RollCakeRSS() {

		this.setupWindowConfig();
		this.setupToolBar();
		this.setupMenuBar();
		//		Container cp = this.getContentPane();
		this.fm = new RCManager();

		//------------------- debug initialize -------------------//
		for (String[] feed : Debug.DEBUG_URLS) {
			this.fm.addFeed(feed[0], feed[1], null);
		}
		//------------------- debug end -------------------//
		this.group = 0;

		this.updateTable();
		//		this.fm._consoleOutput();
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
		;
		file.add(itemExit);
	}

	public void updateTable() {
		JPanel pane = (JPanel) this.getContentPane();
		pane.setLayout(new BorderLayout());
		pane.setBackground(RCConfig.window_background_color);

		JPanel leftPane = new JPanel(new BorderLayout());
		leftPane.setPreferredSize(new Dimension(RCConfig.tablepane_size_width, RCConfig.window_size_height));

		pane.add(leftPane, BorderLayout.WEST);

		detailPane = new JPanel(new BorderLayout());
		detailPane.setPreferredSize(RCConfig.underpane_size_dimension);
		detailPane.setBackground(RCConfig.underpane_background_color);
		fm.setContentPane(pane);


		leftPane.add(detailPane, BorderLayout.SOUTH);

		tablePane = fm.setTablePane();
//		leftPane.add(tablePane, BorderLayout.NORTH);


		rightPane = getRightPane();
		pane.add(rightPane, BorderLayout.EAST);
		rightPane.setPreferredSize(RCConfig.rightpane_size_dimension);
		rightPane.setBackground(RCConfig.rightpane_background_color);

//		(JPanel)((JPanel)contentPane.getComponent(0)).getComponent(1);
	}

	public JPanel getRightPane() {
		JPanel pane = new JPanel(new GridLayout(3, 1));
		JButton button = new JButton(new AddFeedAction("RSSを登録する"));
		pane.add(button);
		return pane;
	}

	class AddFeedAction extends AbstractAction {
		AddFeedAction(String text) {
			super(text);
		}

		public void actionPerformed(ActionEvent e) {
			Object[] msg = { "登録したいURLを貼り付けて下さい" };
			String ans = JOptionPane.showInputDialog(rightPane, msg, "RSSの登録",
					JOptionPane.INFORMATION_MESSAGE);
			System.out.println(ans);
			if (ans == null)
				return;

			RCFeed feed = fm.createFeed(ans, null);
			if (feed == null) {
				String[] msg2 =  {
						"Feedを取得できませんでした",
						"URLが間違っていないか確認して下さい",
						"["+ ans + "]",
				};
				JOptionPane.showMessageDialog(rightPane, msg2, "失敗",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			feed.run();
			feed.setGroupId(group);
			fm.addFeed(feed);
			fm.setTablePane();

			RCFiler.outputFeedList(fm.getFeedList());

			System.out.println(ans);
		}
	}

	class AddFeedDialog extends JOptionPane {

	}
}
