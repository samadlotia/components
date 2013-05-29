package samad.components.gradient;

import javax.swing.JComponent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GradientEditor extends JComponent {
  final GradientEditorUI ui = new GradientEditorUI(this);
  public GradientEditor() {
    super.setUI(ui);

    super.addMouseListener(new MouseAdapter() {
      public void mousePressed(final MouseEvent e) {
        final int x = e.getX();
        final int y = e.getY();

        if (ui.inKnobRegion(y)) {
          selectedStop = ui.xToStop(x);
        } else {
          selectedStop = null;
        }
        repaint();
      }
    });

    super.addMouseMotionListener(new MouseAdapter() {
      public void mouseDragged(final MouseEvent e) {
        if (selectedStop == null)
          return;
        final int x = e.getX();
        final float position = ui.xToPosition(x);
        selectedStop.setPosition(position);
        repaint();
      }
    });

    /*
    super.addComponentListener(new ComponentAdapter() {
      public void componentResized(final ComponentEvent e) {
        ui.prepareAfterResize();
      }

      public void componentShown(final ComponentEvent e) {
        ui.prepareAfterResize();
      }

      public void componentMoved(final ComponentEvent e) {
        ui.prepareAfterResize();
      }
    });
    */
  }

  Gradient gradient = new Gradient();
  Gradient.Stop selectedStop = null;

  public void setGradient(final Gradient gradient) {
    if (gradient == null)
      throw new IllegalArgumentException("gradient is null");
    this.gradient = new Gradient(gradient);
  }

  public Gradient getGradient() {
    return gradient;
  }

  public Gradient.Stop getSelectedStop() {
    return selectedStop;
  }
}