package samad.components.taskwindow;

public interface TaskMonitor {
	public void setTitle(String title);
	public void setStatusMessage(String msg);
	public void setWarnMessage(String msg);
	public void setProgress(double progress);
}