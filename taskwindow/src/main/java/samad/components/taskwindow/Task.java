package samad.components.taskwindow;

public interface Task {
	public void run(TaskMonitor monitor) throws Exception;
	public void cancel();
}
