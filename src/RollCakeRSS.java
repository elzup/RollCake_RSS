import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class RollCakeRSS extends JFrame {
	FeedManager fm;

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

		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[RCConfig.window_id_lookandfeel].getClassName());
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
		//		Container cp = this.getContentPane();
		this.fm = new FeedManager();

		//------------------- debug initialize -------------------//
		for (String url : Debug.DEBUG_URLS) {
			this.fm.addFeed(url, null);
		}
		//------------------- debug end -------------------//


		this.updateTable();
		//		this.fm._consoleOutput();
	}

	public void updateTable() {
		JPanel pane = (JPanel) this.getContentPane();
		pane.setLayout(new BorderLayout());

		JPanel underPane = new JPanel(new BorderLayout());

		fm.setUnderPane(underPane);

		JPanel rightPane = new JPanel();

		pane.add(fm.getTable(), BorderLayout.CENTER);
		pane.add(rightPane, BorderLayout.EAST);
		pane.add(underPane, BorderLayout.SOUTH);
	}
}
