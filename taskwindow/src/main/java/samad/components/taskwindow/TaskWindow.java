package samad.components.taskwindow;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.Dialog.ModalityType;

public class TaskWindow {
	protected final JDialog dialog;
	protected final JPanel tasksPanel;

	public TaskWindow() {
		dialog = new JDialog(null, "Tasks", ModalityType.MODELESS);

		dialog.setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();

		final JButton clearButton = new JButton("Clear");

		tasksPanel = new JPanel(new GridBagLayout());
		final JScrollPane scrollPane = new JScrollPane(tasksPanel);
		c.gridx = 0;			c.gridy = 0;
		c.gridwidth = 1;		c.gridheight = 1;
		c.weightx = 1.0;		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		dialog.add(scrollPane, c);

		final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.add(clearButton);

		c.gridx = 0;			c.gridy = 1;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		dialog.add(buttonsPanel, c);

		dialog.pack();
	}	

	public void show() {
		dialog.setVisible(true);
	}

	public void addTask(final String name) {
		final JLabel titleLabel = new JLabel(name);
		final RoundedProgressBar progressBar = new RoundedProgressBar();
		progressBar.setProgress(0.235f);

		final JPanel taskPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;			c.gridy = 0;
		c.gridwidth = 1;		c.gridheight = 0;
		c.weightx = 1.0;		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
	}

	private static Icon iconFromURL(final String url) {
		return new ImageIcon(getClass().getResource(url));
	}
}