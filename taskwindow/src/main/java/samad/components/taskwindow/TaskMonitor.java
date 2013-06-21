package samad.components.taskwindow;

public interface TaskMonitor {
	public void setTitle(String title);
	public void setStatusMessage(String msg);
	public void setProgress(double progress);
	
	public static enum Level { INFO, WARN, ERROR };
	public void issue(Level level, String message);
}