import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import lib.Item;

import com.sun.istack.internal.Nullable;

public class RCManager {

	private JPanel contentPane;
	private ArrayList<RCFeed> feedList;
	private ArrayList<RCGroup> groupList;
	private Table table;

	private int mode;

	public static int table_mode_recentry = 0;

	public RCManager() {
		this.feedList = new ArrayList<RCFeed>();
		this.groupList = new ArrayList<RCGroup>();
	}

	public ArrayList<RCFeed> getFeedList() {
		return this.feedList;
	}

	public ArrayList<RCGroup> getGroupList() {
		return this.groupList;
	}

	public void addGroup(int id, String name) {
		this.groupList.add(new RCGroup(id, name));
	}

	public void addFeed(RCFeed feed) {
		this.feedList.add(feed);
	}

	public void addFeed(String name, String url, boolean compact, @Nullable String encode) {
		this.addFeed(this.createFeed(name, url, encode));
	}

	public void addFeed(String name, String url, @Nullable String encode) {
		this.addFeed(name, url, false, encode);
	}

	public void addFeed(String url, @Nullable String encode) {
		this.addFeed(null, url, false, encode);
	}

	public RCFeed createFeed(String name, String url, @Nullable String encode) {
		RCFeed feed = new RCFeed();
		if (!feed.setURL(url)) {
			return null;
		}
		feed.setName(name);
		if (encode != null) // 引数で指示があったら文字コードを指定
			feed.setEncoding(encode);
		//		feed.run();
		return feed;
	}

	public RCFeed createFeed(String url, @Nullable String encode) {
		return this.createFeed(null, url, encode);
	}

	public void setContentPane(JPanel cPane) {
		this.contentPane = cPane;
		this.table = new Table(contentPane, this.feedList);
	}

	//	public JPanel getPanel() {
	//		JPanel p = new JPanel();
	//		for (RCFeed feed : this.feedList) {
	//			for (RCItem item : feed.getRCItemList()) {
	//				Date d = item.getDate();
	//				JLabel label = new JLabel(item.getTitle());
	//				//				p.add(label);
	//			}
	//		}
	//		return p;
	//	}

	public void setupTile() {
		if (this.feedList.size() == 0) {
			System.out.println("feedListが空です");
			return;
		}

		for (RCFeed feed : this.feedList) {
			for (RCItem item : feed.getRCItemList()) {
				System.out.println(item.getDateString()); ///
				System.out.println(item.getDateTime()); ///
				System.out.println();
			}
		}
	}

	public void runAll() {

		long start = System.currentTimeMillis();
		ExecutorService threadPool = Executors.newFixedThreadPool(8);
		Collection<Callable<Void>> processes = new LinkedList<Callable<Void>>();
		for (RCFeed feed : this.feedList) {
			final RCFeed feed0 = feed;
			processes.add(new Callable<Void>() {
				@Override public Void call() {
					feed0.run();
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
	}

//	@SuppressWarnings("hiding")
//	public class RunCallable<Void> implements Callable<Void> {
//		RCFeed feed;
//		public RunCallable(RCFeed feed) {
//			this.feed = feed;
//		}
//		@Override
//		public Void call() throws Exception {
//			// TODO method
//			this.runCall();
//			return null;
//		}
//		public void runCall() {
//			feed.run();
//			System.out.println("run now");
//		}
//	}

	public void update(RCFeed feed) {
		this.table.tileUpdate(feed, RCConfig.num_day_recentry);
	}

	public void _consoleOutput() {
		for (RCFeed feed : this.feedList) {
			ArrayList<Item> itemList = feed.getItemList();

			Iterator<Item> iterator = itemList.iterator();
			while (iterator.hasNext()) {
				System.out.print(iterator.next().toString());
				System.out.println();
			}
		}
	}

	public JPanel setTablePane() {
		JPanel table = new JPanel();
		table.setLayout(new GridLayout(1, RCConfig.num_day_recentry));
		JPanel[] colPanes = this.getRecentlyTable();

		for (int i = 0; i < colPanes.length; i++)
			table.add(colPanes[i]);
		JPanel leftPane = (JPanel) contentPane.getComponent(0);

		if (leftPane.getComponents().length > 1)
			leftPane.remove(leftPane.getComponent(1));
		((JPanel) contentPane.getComponent(0)).add(table, BorderLayout.NORTH);
		table.setBackground(RCConfig.tablepane_background_color);
		table.setVisible(false);
		table.setVisible(true);
		return table;
	}

	private JPanel[] getRecentlyTable() {
		int ndr = RCConfig.num_day_recentry;
		JPanel[] pane = new JPanel[ndr];
		for (int i = 0; i < ndr; i++) {
			JPanel wrapPane = new JPanel();
			wrapPane.setLayout(new BoxLayout(wrapPane, BoxLayout.Y_AXIS));
			wrapPane.add(new JTextField(RCConfig.DateToString(new Date(new Date().getTime() - (i * 24 * 60 * 60)))));
			JPanel inPane = this.table.getDatePane(i);
			inPane.setPreferredSize(RCConfig.tablepane_size_dimension);
			wrapPane.add(inPane);
			pane[ndr - i - 1] = wrapPane;
		}
		return pane;
	}

	//------------------- RightPane -------------------//
	public void setFeedListPaneSetAt(JPanel pane) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		JList<String> list = new JList<String>(model);
		for (RCFeed feed : this.feedList)
			model.addElement(feed.getName());
		pane.add(new JScrollPane(list));
	}

	public void updateFeedList() {

	}

	public class ShowFeedDetailActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}
}
