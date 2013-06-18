package samad.components.taskwindow;

public interface Task {
	public void run() throws Exception;
	public void cancel();
}
