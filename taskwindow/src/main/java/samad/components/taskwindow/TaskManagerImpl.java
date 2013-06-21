package samad.components.taskwindow;

import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

import java.util.Map;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		return new ImageIcon(TaskWindow.class.getResource(url));
	}

	static final Map<TaskMonitor.Level,Icon> levelIcons = new HashMap<TaskMonitor.Level,Icon>();
	static {
		levelIcons.put(TaskMonitor.Level.INFO, iconFromURL("/info-icon.png"));
		levelIcons.put(TaskMonitor.Level.WARN, iconFromURL("/warn-icon.png"));
		levelIcons.put(TaskMonitor.Level.ERROR, iconFromURL("/error-icon.png"));
	}

	static final Icon FINISHED_ICON = iconFromURL("/finished-icon.png");
	static final Icon CANCELLED_ICON = iconFromURL("/cancelled-icon.png");

	final Task t;
	final TaskWindow.TaskUI ui;
	public TaskRunner(Task t, TaskWindow window) {
		this.t = t;
		ui = window.createTask();
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
				ui.addMessage(FINISHED_ICON, "Finished.");
			}
		} catch (Exception e) {
			monitor.issue(TaskMonitor.Level.ERROR, "Could not be completed: " + e.getMessage());
			e.printStackTrace();
		}
		ui.setTaskAsCompleted();
	}
}