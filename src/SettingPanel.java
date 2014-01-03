import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class SettingPanel extends JTabbedPane {
	RCManager manager;

	JList<String> list;
	DefaultListModel<String> model;
	FeedConfigPanel feedConfigPane;

	public SettingPanel(RCManager mana) {
		this.manager = mana;
		this.setMaximumSize(RCConfig.settingpanel_size);
		this.setMinimumSize(RCConfig.settingpanel_size);

		JPanel addfeedPane = new AddPanel();
		JPanel configPane = new JPanel();
		this.addTab("AddFeed", addfeedPane);
		this.addTab("Config", configPane);

		//addfeedPane
		addfeedPane.setLayout(new BoxLayout(addfeedPane, BoxLayout.Y_AXIS));

		//configPane
		configPane.setLayout(new BorderLayout());
		model = new DefaultListModel<>();
		list = new JList<>(model);
		RCFeed firstFeed = setList();
		this.feedConfigPane = new FeedConfigPanel(firstFeed);

		configPane.add(new JScrollPane(list), BorderLayout.WEST);
		configPane.add(feedConfigPane, BorderLayout.CENTER);

		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting())
					return;
				Object rc = manager.getContent(list.getSelectedIndex());
				if (rc instanceof RCFeed) {
					feedConfigPane.setFeed((RCFeed) rc);
				} else if (rc instanceof RCGroup) {
					System.out.println(((RCGroup) rc).getId());///
					feedConfigPane.callGroupSetting((RCGroup) rc);
				}
			}
		});
	}

	public RCFeed setList() {
		model.clear();
		list.removeAll();
		list.setModel(model);
		RCFeed firstFeed = null;
		for (RCGroup group : manager.getGroupList()) {
			model.addElement("+" + group.getName());
			for (RCFeed feed : group.getFeedList()) {
				model.addElement("  -" + feed.getName());
				if (firstFeed == null)
					firstFeed = feed;
			}
		}
		list.setVisible(false);
		list.setVisible(true);
		//		list.setSelectedIndex(select);
		return firstFeed;
	}

	static final JPanel wrapByJPanel(Component c) {
		return wrapByJPanel(c, null);
	}

	static final JPanel wrapByJPanel(Component c, Dimension d) {
		JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pane.add(c);
		if (d != null) {
			pane.setMaximumSize(d);
			pane.setMinimumSize(d);
		}
		return pane;
	}

	class AddPanel extends JPanel {
		JTextField nameField;
		JTextField urlField;
		JComboBox<String> groupList;

		public AddPanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.nameField = new JTextField();
			this.urlField = new JTextField();

			nameField.setText("");
			urlField.setText("");
			model = new DefaultListModel<String>();

			nameField.setPreferredSize(RCConfig.setting_child2_size);
			urlField.setPreferredSize(RCConfig.setting_child2_size);

			this.add(wrapByJPanel(new JLabel("フィード名")));
			this.add(wrapByJPanel(this.nameField));
			this.add(wrapByJPanel(new JLabel("URL")));
			this.add(wrapByJPanel(this.urlField));

			this.setGroupList();
			this.add(wrapByJPanel(this.groupList));

			JPanel btnPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JButton addButton = new JButton("OK");
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int select = groupList.getSelectedIndex();
					manager.addFeed(nameField.getText(), urlField.getText(), select);
					nameField.setText("");
					urlField.setText("");
					setList();
				}
			});
			btnPane.add(addButton);
			btnPane.setAlignmentX(RIGHT_ALIGNMENT);
			this.add(btnPane);

			for (Component c : this.getComponents()) {
				if (c instanceof JPanel) {
					//					c.setBackground(Color.red);
					//					((FlowLayout) ((JPanel) c).getLayout()).setAlignment(FlowLayout.LEFT);
					c.setMaximumSize(RCConfig.setting_child_size);
					c.setMinimumSize(RCConfig.setting_child_size);
				}
			}
		}

		public void setGroupList() {
			String[] nameList = new String[manager.getGroupList().size()];
			for (RCGroup group : manager.getGroupList())
				nameList[group.getId()] = group.getName();
			groupList = new JComboBox<String>(nameList);
		}

	}

	class FeedConfigPanel extends JPanel {
		private RCFeed feed;
		JTextField nameField;
		JTextField urlField;
		JButton colorButton, resetButton, removeButton;
		Color color;
		JComboBox<String> groupList;

		public FeedConfigPanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.nameField = new JTextField();
			this.urlField = new JTextField();
			this.colorButton = new JButton(" ");
			this.color = null;

			model = new DefaultListModel<String>();
			this.setGroupList();
			this.add(wrapByJPanel(new JLabel("フィード名")));
			this.add(wrapByJPanel(this.nameField));
			this.add(wrapByJPanel(new JLabel("URL")));
			this.add(wrapByJPanel(this.urlField));
			JPanel colorPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
			colorPane.add(new JLabel("色"));
			colorPane.add(this.colorButton);
			this.add(colorPane);

			this.add(wrapByJPanel(this.groupList));

			JPanel btnPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JButton updateButton = new JButton("OK");
			JButton resetButton = new JButton("リセット");
			JButton removeButton = new JButton("消去");
			updateButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					feed.setName(nameField.getText());
					feed.setURL(urlField.getText());
					feed.setColor(color);
					int select = groupList.getSelectedIndex();
					feed.setGroupId(select);
					manager.moveFeedTo(feed, select);

					setList();
				}
			});
			resetButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setFeedInfo();
				}
			});
			removeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int ans = JOptionPane.showConfirmDialog(feedConfigPane, "[" + feed.getName() + "]を消去しますか？", "Feedの消去", JOptionPane.YES_NO_OPTION);
					if (ans != JOptionPane.YES_OPTION) return ;
					manager.removeFeed(feed);
					setList();
				}
			});
			btnPane.add(updateButton);
			btnPane.add(resetButton);
			btnPane.add(removeButton);
			btnPane.setAlignmentX(RIGHT_ALIGNMENT);
			this.add(btnPane);

			for (Component c : this.getComponents()) {
				if (c instanceof JPanel) {
					//					c.setBackground(Color.red);
					//					((FlowLayout) ((JPanel) c).getLayout()).setAlignment(FlowLayout.LEFT);
					c.setMaximumSize(RCConfig.setting_child_size);
					c.setMinimumSize(RCConfig.setting_child_size);
				}
			}
		}

		public FeedConfigPanel(RCFeed feed) {
			this();
			this.setFeed(feed);
		}

		public void setGroupList() {
			String[] nameList = new String[manager.getGroupList().size()];
			for (RCGroup group : manager.getGroupList())
				nameList[group.getId()] = group.getName();
			groupList = new JComboBox<String>(nameList);
		}

		public void setFeedInfo() {
			this.nameField.setText(feed.getName());
			this.urlField.setText(feed.getUrl().toString());
			this.color = feed.getColor();
			this.colorButton.setBackground(color);
			this.colorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Color chooser
					System.out.println(feed.getColor());///
					Color choosedColor = JColorChooser.showDialog(feedConfigPane, "色選択", color);
					if (choosedColor == null)
						return;
					color = choosedColor;
					feed.setColor(choosedColor);
					colorButton.setBackground(choosedColor);
				}
			});
			this.groupList.setSelectedIndex(feed.getGroupId());
		}

		public void callGroupSetting(RCGroup group) {
			JTextField nameField = new JTextField(group.getName());
			Object[] o = {
					"グループ名", nameField,
			};
			int ans = JOptionPane.showConfirmDialog(this, o, "グループ名編集", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

			if (ans == JOptionPane.OK_OPTION && nameField.getText() != null) {
				group.setName(nameField.getText());
				setList();
			}
		}

		public void setFeed(RCFeed feedS) {
			this.feed = feedS;
			this.setFeedInfo();
		}
	}
}
