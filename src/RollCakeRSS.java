import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class RollCakeRSS extends JFrame {
	RCManager fm;

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
		for (String url : Debug.DEBUG_URLS) {
			this.fm.addFeed(url, null);
		}
		//------------------- debug end -------------------//



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
		JMenuItem itemExit = new JMenuItem("Exit");;
		file.add(itemExit);
	}

	public void updateTable() {
		JPanel pane = (JPanel) this.getContentPane();
		pane.setLayout(new BorderLayout());
		pane.setBackground(RCConfig.window_background_color);

		JPanel leftPane = new JPanel(new BorderLayout());
		leftPane.setPreferredSize(new Dimension(RCConfig.tablepane_size_width, RCConfig.window_size_height));

		JPanel leftUnderPane = new JPanel(new BorderLayout());
		leftUnderPane.setPreferredSize(RCConfig.underpane_size_dimension);
		leftUnderPane.setBackground(RCConfig.underpane_background_color);
		fm.setUnderPane(leftUnderPane);

		JPanel tablePane = fm.getTablePane();
		tablePane.setPreferredSize(RCConfig.tablepane_size_dimension);
		tablePane.setBackground(RCConfig.tablepane_background_color);



		leftPane.add(tablePane, BorderLayout.NORTH);
		leftPane.add(leftUnderPane, BorderLayout.SOUTH);

		JPanel rightPane = new JPanel();
		rightPane.setPreferredSize(RCConfig.rightpane_size_dimension);
		rightPane.setBackground(RCConfig.rightpane_background_color);

		pane.add(leftPane,  BorderLayout.WEST);
		pane.add(rightPane, BorderLayout.EAST);
	}
}
