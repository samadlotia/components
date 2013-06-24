package samad.components.taskwindow;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
    	final TaskManager manager = new TaskManagerImpl();
    	for (int i = 0; i < 5; i++) {
    		sleep(1000);
    		manager.execute(new WhateverTask());
    	}
    }

	private static void sleep(long ms) {
		try { Thread.sleep(ms); } catch(Exception e) {}
	}

	private static Icon iconFromURL(final String url) {
		return new ImageIcon(TaskWindow.class.getResource(url));
	}
}

class WhateverTask implements Task {
	private static final Random random = new Random();

	private static synchronized <T> T pick(T[] array) {
		final int index = random.nextInt(array.length);
		return array[index];
	}

	private static String[] statuses = {
		"Age cannot wither her, nor custom stale her infinite variety",
		"A countenance more in sorrow than in anger",
		"And shining morning face, creeping like a snail unwillingly to school",
		"How sharper than a serpent's tooth it is to have a thankless child",
		"Men's evil manners live in brass; their virtues we write in water",
		"Himself the primrose path of dalliance treads",
		"Some are born great, some achieve greatness, and some have greatness thrust upon 'em",
		"Though this be madness, yet there is method in it",
		"When sorrows come, they come not single spies, but in battalions",
		"Smooth runs the water where the brook is deep"
	};

	static final float MIN_SLEEP_SEC = 0.5f;
	static final float MAX_SLEEP_SEC = 0.7f;
	static final int STEPS = 10;
	static final double PROBABILITY_OF_EXCEPTION = 0.02207;

	static final TaskMonitor.Level[] LEVELS = {TaskMonitor.Level.INFO, TaskMonitor.Level.WARN};

	private static void sleep(long ms) {
		try { Thread.sleep(ms); } catch(Exception e) {}
	}

	boolean cancelled = false;

	public void run(TaskMonitor monitor) throws Exception {
		monitor.setTitle(pick(statuses));

		for (int i = 0; i < STEPS; i++) {
			if (cancelled)
				return;
			final int sleepMs = (int) (1000 * (Math.random() * (MAX_SLEEP_SEC - MIN_SLEEP_SEC) + MIN_SLEEP_SEC));
			sleep(sleepMs);
			if (Math.random() <= PROBABILITY_OF_EXCEPTION) {
				throw new Exception(pick(statuses));
			} else {
				monitor.setProgress(i / ((float) (STEPS - 1)));
				monitor.issue(pick(LEVELS), pick(statuses));
			}
		}
	}

	public void cancel() {
		cancelled = true;
	}
}