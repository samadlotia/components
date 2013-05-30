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
  	frame.add(new GradientPanel());
  	frame.pack();
  	frame.setVisible(true);
  }
}