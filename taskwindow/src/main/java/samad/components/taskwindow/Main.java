package samad.components.taskwindow;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			TaskWindow t = new TaskWindow();
    			t.show();
    		}
    	});
    }
}
