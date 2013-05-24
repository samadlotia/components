package samad.components.gradient;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import java.awt.Color;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Main main = new Main();
      }
    });
  }

  public Main() {
  	final JFrame frame = new JFrame();
  	final GradientEditor editor = new GradientEditor();
  	//editor.setGradient(new Gradient(Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.magenta));
 	editor.setGradient(new Gradient(Color.blue, Color.black, Color.yellow));
  	frame.add(editor);
  	frame.pack();
  	frame.setVisible(true);
  }
}