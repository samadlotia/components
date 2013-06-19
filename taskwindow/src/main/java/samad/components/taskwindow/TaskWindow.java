package samad.components.taskwindow;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Dialog.ModalityType;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TaskWindow {
	protected static final Icon CANCEL_ICON = iconFromURL("/cancel-icon.png");
	protected static final Icon CANCEL_HOVER_ICON = iconFromURL("/cancel-hover-icon.png");
	protected static final Icon CANCEL_PRESSED_ICON = iconFromURL("/cancel-pressed-icon.png");

	protected final JDialog dialog;
	protected final JScrollPane scrollPane;
	protected final JPanel tasksPanel;

	protected int count = 0;

	public TaskWindow() {
		dialog = new JDialog(null, "Tasks", ModalityType.MODELESS);

		dialog.setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();

		final JButton cleanButton = new JButton("Clean");
		cleanButton.setToolTipText("Clean tasks list by removing all finished, cancelled, and failed tasks.");

		tasksPanel = new JPanel(new GridBagLayout());
		scrollPane = new JScrollPane(tasksPanel);
		c.gridx = 0;			c.gridy = 0;
		c.gridwidth = 1;		c.gridheight = 1;
		c.weightx = 1.0;		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		dialog.add(scrollPane, c);

		final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.add(cleanButton);

		c.gridx = 0;			c.gridy = 1;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		dialog.add(buttonsPanel, c);

		dialog.pack();
	}	

	public void show() {
		dialog.setVisible(true);
	}

	private static JLabel newCancelLabel() {
		final JLabel label = new JLabel(CANCEL_ICON);
		label.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				final boolean isPressed = (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0;
				label.setIcon(isPressed ? CANCEL_PRESSED_ICON : CANCEL_HOVER_ICON);
			}

			public void mousePressed(MouseEvent e) {
				label.setIcon(CANCEL_PRESSED_ICON);
			}

			public void mouseReleased(MouseEvent e) {
				final int x = e.getX();
				final int y = e.getY();
				if (0 <= x && x <= label.getWidth() &&
					0 <= y && y <= label.getHeight()) {
					label.setIcon(CANCEL_HOVER_ICON);
				} else {
					label.setIcon(CANCEL_ICON);
				}
			}

			public void mouseExited(MouseEvent e) {
				label.setIcon(CANCEL_ICON);
			}

			public void mouseClicked(MouseEvent e) {
			}
		});
		return label;
	}

	public void addTask(final String name, final String icon, final float progress) {
		final JLabel titleLabel = labelWithFont(new JLabel(name), "bold 16");
		final RoundedProgressBar progressBar = new RoundedProgressBar();
		final JLabel cancelLabel = newCancelLabel();
		cancelLabel.setToolTipText("Cancel this task");

		final JPanel msgsPanel = new JPanel(new GridBagLayout());
		final DiscloseTriangle discloseTriangle = new DiscloseTriangle();
		discloseTriangle.setToolTipText("See details of task");
		discloseTriangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msgsPanel.setVisible(discloseTriangle.isOpen());
			}
		});
		final JLabel statusLabel = new JLabel("omg wtf", iconFromURL(String.format("/%s-icon.png", icon)), JLabel.LEFT);
		labelWithFont(statusLabel, "bold 12");
		final JLabel cancelStatusLabel = new JLabel("Cancelling...", JLabel.RIGHT);
		labelWithFont(cancelStatusLabel, "italic 12");

		progressBar.setValue(progress);

		final GridBagConstraints c = new GridBagConstraints();
		final JPanel statusPanel = new JPanel(new GridBagLayout());
		c.gridx = 0;			c.gridy = 0;
		c.gridwidth = 1;		c.gridheight = 1;
		c.weightx = 0.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.insets.set(0, 0, 0, 5);
		statusPanel.add(discloseTriangle, c);

		c.gridx = 1;			c.gridy = 0;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.insets.set(0, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		statusPanel.add(statusLabel, c);

		c.gridx = 2;			c.gridy = 0;
		c.weightx = 0.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		statusPanel.add(cancelStatusLabel, c);

		msgsPanel.setBorder(new RoundedBorder());
		c.gridx = 0;			c.gridy = 0;
		c.gridwidth = 1;		c.gridheight = 1;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		msgsPanel.add(new JLabel("jsjkslsl"), c);

		c.gridy++;
		msgsPanel.add(new JLabel("ajsdsalsj"), c);

		c.gridy++;
		msgsPanel.add(new JLabel("jasd09219083120"), c);

		c.gridx = 0;			c.gridy = 1;
		c.gridwidth = 2;		c.gridheight = 1;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets.set(10, 0, 0, 0);
		statusPanel.add(msgsPanel, c);

		msgsPanel.setVisible(false);

		final JPanel taskPanel = new JPanel(new GridBagLayout());

		c.gridx = 0;			c.gridy = 0;
		c.gridwidth = 2;		c.gridheight = 1;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets.set(20, 20, 0, 20);
		taskPanel.add(titleLabel, c);

		c.gridx = 0;			c.gridy = 1;
		c.gridwidth = 1;		c.gridheight = 1;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets.set(15, 20, 0, 10);
		taskPanel.add(progressBar, c);

		c.gridx = 1;			c.gridy = 1;
		c.weightx = 0.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		c.insets.set(7, 0, 0, 20);
		taskPanel.add(cancelLabel, c);

		c.gridx = 0;			c.gridy = 2;
		c.gridwidth = 2;		c.gridheight = 1;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets.set(15, 20, 0, 20);
		taskPanel.add(statusPanel, c);

		final JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		separator.setForeground(new Color(0xdddddd));
		separator.setOpaque(false);
		c.gridx = 0;			c.gridy = 3;
		c.gridwidth = 2;		c.gridheight = 1;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		c.insets.set(20, 20, 0, 20);
		taskPanel.add(separator, c);

		c.gridx = 0;			c.gridy = count++;
		c.gridwidth = 1;		c.gridheight = 1;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets.set(0, 0, 0, 0);
		tasksPanel.add(taskPanel, c);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
				scrollBar.setUnitIncrement((scrollBar.getMaximum() - scrollBar.getMinimum()) / 20);
			}
		});
	}

	private static Icon iconFromURL(final String url) {
		return new ImageIcon(TaskWindow.class.getResource(url));
	}

	private static JLabel labelWithFont(JLabel label, String style) {
		if (style == null)
			style = "plain 12";
		style = "Helvetica " + style;
		label.setFont(Font.decode(style));
		return label;
	}
}