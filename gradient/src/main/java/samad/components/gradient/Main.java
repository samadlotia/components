package samad.components.gradient;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.BorderFactory;

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
    editor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  	editor.setGradient(new Gradient(Color.red, Color.orange, Color.yellow, Color.green, Color.blue, Color.magenta));
    //editor.setGradient(new Gradient(Color.blue, Color.black, Color.yellow));
    //editor.setGradient(new Gradient(Color.blue, Color.black));
  	frame.add(editor);
  	frame.pack();
  	frame.setVisible(true);
  }
}