package samad.components.taskwindow;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class Main {
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			TaskWindow t = new TaskWindow();
    			t.show();
    			t.addTask("Why hello there children!", "error", 0.2f);
    			t.addTask("Hey chef", "info", 0.9f);
    			t.addTask("Importing network", "warn", 1.0f);
    			t.addTask("Importing attributes", "finished", 0.2f);
    			t.addTask("Whatever", "cancelled", 0.0f);
    		}
    	});
    }
}
