package samad.components.gradient;

import javax.swing.JComponent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class GradientEditor extends JComponent {
  final GradientEditorUI ui = new GradientEditorUI(this);
  public GradientEditor() {
    super.setUI(ui);

    super.addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {

      }

      public void mouseMoved(MouseEvent e) {

      }
    });
  }

  Gradient gradient = new Gradient();

  public void setGradient(final Gradient gradient) {
    if (gradient == null)
      throw new IllegalArgumentException("gradient is null");
    this.gradient = new Gradient(gradient);
    ui.loadGradient();
  }

  public Gradient getGradient() {
    return gradient;
  }
}