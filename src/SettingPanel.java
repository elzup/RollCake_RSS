import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SettingPanel extends JPanel {
	RCManager manager;

	JList<String> list;
	DefaultListModel<String> model;
	FeedConfigPanel feedConfigPane;

	public SettingPanel(RCManager mana) {
		this.manager = mana;
		this.setMaximumSize(RCConfig.settingpanel_size);
		this.setMinimumSize(RCConfig.settingpanel_size);

		this.setLayout(new BorderLayout());

		model = new DefaultListModel<>();
		list = new JList<>(model);
		RCFeed firstFeed = setList();
		this.feedConfigPane = new FeedConfigPanel(firstFeed);
		this.add(new JScrollPane(list), BorderLayout.WEST);
		this.add(feedConfigPane, BorderLayout.CENTER);

		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) return;
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
		int select = list.getSelectedIndex();
		model.clear();
		list.removeAll();
		RCFeed firstFeed = null;
		for (RCGroup group : manager.getGroupList()) {
			model.addElement("+" + group.getName());
			for (RCFeed feed : group.getFeedList()) {
				if (firstFeed == null)
					firstFeed = feed;
				model.addElement("  -" + feed.getName());
			}
		}
		list.setSelectedIndex(select);
		return firstFeed;
	}

	static final JPanel wrapByJPanel(Component c) {
		JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pane.add(c);
		return pane;
	}

	class FeedConfigPanel extends JPanel {
		private RCFeed feed;
		JTextField nameField;
		JTextField urlField;
		JButton colorButton, resetButton;
		Color color;

		public FeedConfigPanel() {
			this.setLayout(new GridLayout(6, 1));
			this.nameField = new JTextField();
			this.urlField = new JTextField();
			this.colorButton = new JButton(" ");
			this.color = null;

			this.add(wrapByJPanel(new JLabel("フィード名")));
			this.add(wrapByJPanel(this.nameField));
			this.add(wrapByJPanel(new JLabel("URL")));
			this.add(wrapByJPanel(this.urlField));
			JPanel colorPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
			colorPane.add(new JLabel("色"));
			colorPane.add(this.colorButton);
			this.add(colorPane);

			JPanel btnPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JButton updateButton = new JButton("OK");
			JButton resetButton = new JButton("リセット");
			updateButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					feed.setName(nameField.getText());
					feed.setURL(urlField.getText());
					feed.setColor(color);
					setList();
				}
			});
			resetButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setFeedInfo();
				}
			});
			btnPane.add(updateButton);
			btnPane.add(resetButton);
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

		public void setFeedInfo () {
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
