import java.awt.Container;

import javax.swing.JFrame;

public class RollCakeRSS extends JFrame {
	FeedManager fm;

	public static void main(String... args) {
		System.out.println("main start");

		RollCakeRSS cake = new RollCakeRSS();

		cake.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cake.setBounds(10, 10, 300, 200);
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
		fm.getTable();
		//		this.fm._consoleOutput();
	}

	public void updateTable() {
		Container cp = this.getContentPane();
		cp.add(this.fm.getPanel());
	}
}

