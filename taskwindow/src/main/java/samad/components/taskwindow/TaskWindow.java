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
import javax.swing.BoxLayout;
import javax.swing.Box;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class TaskWindow {
	public static interface TaskUI {
		public void setTaskAsCompleted();
		public void setTitle(String title);
		public void setProgress(float progress);
		public void addMessage(Icon icon, String msg);
		public void addCancelListener(ActionListener l);
		public void setCancelStatus(String status);
		public void hideCancelButton();
	}

	protected final JDialog dialog;
	protected final JScrollPane scrollPane;
	protected final JPanel tasksPanel;

	protected final List<TaskUIImpl> tasks = new ArrayList<TaskUIImpl>();

	public TaskWindow() {
		dialog = new JDialog(null, "Tasks", ModalityType.MODELESS);
		dialog.setPreferredSize(new Dimension(500, 400));

		dialog.setLayout(new GridBagLayout());
		final EasyGBC c = new EasyGBC();

		final JButton cleanButton = new JButton("Clean");
		cleanButton.setToolTipText("Clean list of tasks by removing all finished, cancelled, and failed tasks.");
		cleanButton.addActionListener(new CleanAction());

		tasksPanel = new JPanel();
		tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
		scrollPane = new JScrollPane(tasksPanel);
		dialog.add(scrollPane, c.expandBoth());

		final JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.add(cleanButton);
		dialog.add(buttonsPanel, c.down().expandHoriz());

		dialog.pack();
	}	

	public void show() {
		dialog.setVisible(true);
	}

	public void hide() {
		dialog.setVisible(false);
	}
	
	public TaskUI createTask() {
		TaskUIImpl taskUI = new TaskUIImpl();

		tasksPanel.add(taskUI.getPanel());

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
				scrollBar.setUnitIncrement((scrollBar.getMaximum() - scrollBar.getMinimum()) / 20);
			}
		});

		tasks.add(taskUI);

		return taskUI;
	}

	class CleanAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			final Iterator<TaskUIImpl> i = tasks.iterator();
			while (i.hasNext()) {
				final TaskUIImpl ui = i.next();
				if (ui.isCompleted()) {
					ui.remove();
					i.remove();
				}
			}
			tasksPanel.revalidate();
			tasksPanel.repaint();
		}
	}
}	

class TaskUIImpl implements TaskWindow.TaskUI {
	private static JLabel labelWithFont(String style) {
		final JLabel label = new JLabel();
		if (style == null)
			style = "plain 12";
		style = "Helvetica " + style;
		label.setFont(Font.decode(style));
		return label;
	}

	boolean completed = false;
	int numStatusMsgs = 0;
	final JLabel titleLabel;
	final RoundedProgressBar progressBar;
	final JLabel cancelLabel;
	final JPanel msgsPanel;
	final DiscloseTriangle discloseTriangle;
	final JLabel statusLabel;
	final JLabel cancelStatusLabel;
	final JPanel taskPanel;

	public void setTaskAsCompleted() {
		progressBar.setVisible(false);
		cancelLabel.setVisible(false);
		cancelStatusLabel.setVisible(false);
		completed = true;
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	public void setProgress(float progress) {
		progressBar.setValue(progress);
	}

	public void addMessage(Icon icon, String msg) {
		if (numStatusMsgs == 0) {
			statusLabel.setVisible(true);
		} else {
			final JLabel label = labelWithFont("plain 12");
			label.setText(statusLabel.getText());
			label.setIcon(statusLabel.getIcon());
			label.setHorizontalAlignment(JLabel.LEFT);
			msgsPanel.add(label);
			msgsPanel.add(Box.createRigidArea(new Dimension(0, 10)), -1);
		}
		if (numStatusMsgs == 1) {
			discloseTriangle.setVisible(true);
		}
		statusLabel.setIcon(icon);
		statusLabel.setText(msg);
		numStatusMsgs++;
	}

	public void addCancelListener(final ActionListener listener) {
		cancelLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				ActionEvent event = new ActionEvent(TaskUIImpl.this, 0, "cancel");
				listener.actionPerformed(event);
			}
		});
	}

	public void setCancelStatus(String status) {
		cancelStatusLabel.setText(status);
	}

	public void hideCancelButton() {
		cancelLabel.setVisible(false);
	}

	public void remove() {
		taskPanel.getParent().remove(taskPanel);
	}

	public boolean isCompleted() {
		return completed;
	}

	public TaskUIImpl() {
		titleLabel = labelWithFont("bold 16");
		progressBar = new RoundedProgressBar();
		cancelLabel = new CancelButton();

		msgsPanel = new JPanel();
		msgsPanel.setLayout(new BoxLayout(msgsPanel, BoxLayout.Y_AXIS));
		msgsPanel.setVisible(false);
		discloseTriangle = new DiscloseTriangle();
		statusLabel = labelWithFont("plain 12");
		cancelStatusLabel = labelWithFont("italic 12");

		discloseTriangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msgsPanel.setVisible(discloseTriangle.isOpen());
			}
		});

		discloseTriangle.setVisible(false);

		final EasyGBC c = new EasyGBC();
		final JPanel statusPanel = new JPanel(new GridBagLayout());
		statusPanel.add(discloseTriangle, c.insets(0, 0, 0, 5));
		statusPanel.add(statusLabel, c.right().expandHoriz().insets(0, 0, 0, 0));
		statusPanel.add(cancelStatusLabel, c.right().noExpand().anchor("east"));
		statusPanel.add(msgsPanel, c.down().spanHoriz(2).expandHoriz().anchor("northwest").insets(8, 17, 0, 0));

		taskPanel = new JPanel(new GridBagLayout());
		taskPanel.add(titleLabel, c.reset().spanHoriz(2).expandHoriz().insets(20, 20, 0, 20));
		taskPanel.add(progressBar, c.down().noSpan().expandHoriz().insets(15, 20, 0, 10));
		taskPanel.add(cancelLabel, c.right().noExpand().anchor("east").insets(7, 0, 0, 20));
		taskPanel.add(statusPanel, c.down().spanHoriz(2).expandHoriz().insets(15, 20, 0, 20));

		final JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		separator.setForeground(new Color(0xdddddd));
		separator.setOpaque(false);
		taskPanel.add(separator, c.down().spanHoriz(2).expandHoriz().insets(20, 20, 0, 20));
	}

	public JPanel getPanel() {
		return taskPanel;
	}
}

class CancelButton extends JLabel {
	private static Icon iconFromURL(final String url) {
		return new ImageIcon(TaskWindow.class.getResource(url));
	}

	protected static final Icon CANCEL_ICON = iconFromURL("/cancel-icon.png");
	protected static final Icon CANCEL_HOVER_ICON = iconFromURL("/cancel-hover-icon.png");
	protected static final Icon CANCEL_PRESSED_ICON = iconFromURL("/cancel-pressed-icon.png");

	public CancelButton() {
		super(CANCEL_ICON);
		super.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				final boolean isPressed = (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0;
				setIcon(isPressed ? CANCEL_PRESSED_ICON : CANCEL_HOVER_ICON);
			}

			public void mousePressed(MouseEvent e) {
				setIcon(CANCEL_PRESSED_ICON);
			}

			public void mouseReleased(MouseEvent e) {
				final int x = e.getX();
				final int y = e.getY();
				if (0 <= x && x <= getWidth() &&
					0 <= y && y <= getHeight()) {
					setIcon(CANCEL_HOVER_ICON);
				} else {
					setIcon(CANCEL_ICON);
				}
			}

			public void mouseExited(MouseEvent e) {
				setIcon(CANCEL_ICON);
			}
		});
	}
}

class EasyGBC extends GridBagConstraints {
	final Map<String,Integer> anchors = new HashMap<String,Integer>();
	
	public EasyGBC() {
		anchors.put("north", super.NORTH);
		anchors.put("northwest", super.NORTHWEST);
		anchors.put("west", super.WEST);
		anchors.put("south", super.SOUTH);
		anchors.put("east", super.EAST);
		reset();
	}

	public EasyGBC reset() {
		gridx = 0;			gridy = 0;
		gridwidth = 1;		gridheight = 1;
		weightx = 0.0;		weighty = 0.0;
		fill = GridBagConstraints.NONE;
		insets.set(0, 0, 0, 0);
		return this;
	}

	public EasyGBC noExpand() {
		weightx = 0.0;
		weighty = 0.0;
		fill = GridBagConstraints.NONE;
		return this;
	}

	public EasyGBC expandHoriz() {
		weightx = 1.0;
		weighty = 0.0;
		fill = GridBagConstraints.HORIZONTAL;
		return this;
	}

	public EasyGBC expandBoth() {
		weightx = 1.0;
		weighty = 1.0;
		fill = GridBagConstraints.BOTH;
		return this;
	}

	public EasyGBC right() {
		gridx++;
		return this;
	}

	public EasyGBC down() {
		gridx = 0;
		gridy++;
		return this;
	}

	public EasyGBC position(int x, int y) {
		gridx = x;
		gridy = y;
		return this;
	}

	public EasyGBC noSpan() {
		gridwidth = 1;
		gridheight = 1;
		return this;
	}

	public EasyGBC spanHoriz(int n) {
		gridwidth = n;
		gridheight = 1;
		return this;
	}

	public EasyGBC insets(int t, int l, int b, int r) {
		insets.set(t, l, b, r);
		return this;
	}

	public EasyGBC noInsets() {
		insets.set(0, 0, 0, 0);
		return this;
	}

	public EasyGBC anchor(String str) {
		anchor = anchors.get(str);
		return this;
	}
}