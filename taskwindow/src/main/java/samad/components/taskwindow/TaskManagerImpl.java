package samad.components.taskwindow;

import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

import java.util.Map;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.net.URL;

public class TaskManagerImpl implements TaskManager {
	final TaskWindow window;

	final Executor executor = Executors.newCachedThreadPool();

	public TaskManagerImpl() {
		window = new TaskWindow();
		window.show();
	}

	public void execute(Task t) {
		executor.execute(new TaskRunner(t, window));
	}	
}

class TaskRunner implements Runnable {
	private static Icon iconFromURL(final String url) {
		return new ImageIcon(TaskRunner.class.getResource(url));
	}

	static final Map<TaskMonitor.Level,Icon> levelIcons = new HashMap<TaskMonitor.Level,Icon>();
	static {
		levelIcons.put(TaskMonitor.Level.INFO, iconFromURL("/info-icon.png"));
		levelIcons.put(TaskMonitor.Level.WARN, iconFromURL("/warn-icon.png"));
		levelIcons.put(TaskMonitor.Level.ERROR, iconFromURL("/error-icon.png"));
	}

	static final Map<TaskMonitor.Level,URL> levelIconURLs = new HashMap<TaskMonitor.Level,URL>();
	static {
		levelIconURLs.put(TaskMonitor.Level.INFO, TaskRunner.class.getResource("/info-icon.png"));
		levelIconURLs.put(TaskMonitor.Level.WARN, TaskRunner.class.getResource("/warn-icon.png"));
		levelIconURLs.put(TaskMonitor.Level.ERROR, TaskRunner.class.getResource("/error-icon.png"));
	}

	static final Icon FINISHED_ICON = iconFromURL("/finished-icon.png");
	static final Icon CANCELLED_ICON = iconFromURL("/cancelled-icon.png");

	final Task t;
	final TaskWindow.TaskUI ui;
	final Map<TaskMonitor.Level,Integer> levelCounts = new HashMap<TaskMonitor.Level,Integer>();

	public TaskRunner(Task t, TaskWindow window) {
		this.t = t;
		ui = window.createTaskUI();
	}

	public void run() {
		final TaskMonitor monitor = new TaskMonitor() {
			public void setTitle(String title) {
				ui.setTitle(title);
			}

			public void setProgress(double progress) {
				ui.setProgress((float) progress);
			}

			public void setStatusMessage(String message) {
				issue(TaskMonitor.Level.INFO, message);
			}

			public void issue(TaskMonitor.Level level, String message) {
				if (!levelCounts.containsKey(level))
					levelCounts.put(level, 0);
				levelCounts.put(level, levelCounts.get(level) + 1);
				ui.addMessage(levelIcons.get(level), message);
			}
		};

		final boolean[] cancelInvoked = {false};

		ui.addCancelListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ui.hideCancelButton();
				ui.setCancelStatus("Cancelling");
				t.cancel();
				cancelInvoked[0] = true;
			}
		});

		try {
			t.run(monitor);
			if (cancelInvoked[0]) {
				ui.addMessage(CANCELLED_ICON, "Cancelled.");
			} else {
				ui.addMessage(FINISHED_ICON, buildFinishedString());
			}
		} catch (Exception e) {
			monitor.issue(TaskMonitor.Level.ERROR, "Could not be completed: " + e.getMessage());
			e.printStackTrace();
		}
		ui.setTaskAsCompleted();
	}

	private String buildFinishedString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("<html>Finished.");
		if (levelCounts.size() > 0) {
			for (Map.Entry<TaskMonitor.Level,Integer> levelCount : levelCounts.entrySet()) {
				buffer.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				buffer.append("<img align=\"baseline\" src=\"");
				buffer.append(levelIconURLs.get(levelCount.getKey()).toString());
				buffer.append("\">&nbsp;");
				buffer.append(levelCount.getValue());
			}
		}
		buffer.append("</html>");
		return buffer.toString();
	}
}