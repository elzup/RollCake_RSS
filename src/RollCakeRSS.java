import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
		JTextField details_tf = new JTextField();
		details_tf.setEnabled(false);
		underPane.add(details_tf, BorderLayout.CENTER);

		fm.setUnderPane(underPane);

		JPanel rightPane = new JPanel();

		pane.add(fm.getTable(), BorderLayout.CENTER);
		pane.add(rightPane, BorderLayout.EAST);
		pane.add(underPane, BorderLayout.SOUTH);
	}
}
