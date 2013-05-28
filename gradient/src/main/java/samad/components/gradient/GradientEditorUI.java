package samad.components.gradient;

import javax.swing.plaf.ComponentUI;
import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GradientEditorUI extends ComponentUI {
  final GradientEditor editor;
  final KnobUI knobUI = new KnobUI();

  // keep these as class members so that they won't have to be reallocated
  // every time paint() is called
  protected float[] positions = new float[0];
  protected Color[] colors = new Color[0];
  final Rectangle2D gradientRect = new Rectangle2D.Float();

  public GradientEditorUI(final GradientEditor editor) {
    this.editor = editor;
  }

  public void loadGradient() {
    final Gradient gradient = editor.getGradient();
    positions = gradient.getAllPositions(positions);
    colors = gradient.getAllColors(colors);
  }

  public void paint(final Graphics g, final JComponent component) {
    final int w = editor.getWidth();
    final int h = editor.getHeight();
    final float kw = KnobUI.knobWidth();
    final float kh = KnobUI.knobHeight();
    final float gx = kw / 2.0f;
    final float gy = 0.0f;
    final float gw = w - kw;
    final float gh = h - kh;

    final Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (int i = 0; i < positions.length - 1; i++) {
      final float p1 = positions[i];
      final float p2 = positions[i + 1];

      final Color c1 = colors[i];
      final Color c2 = colors[i + 1];

      final float x1 = p1 * gw + gx;
      final float x2 = p2 * gw + gx;
      
      final GradientPaint p = new GradientPaint(x1, 0.0f, c1,
                                                x2, 0.0f, c2);
      g2d.setPaint(p);

      gradientRect.setRect(x1, gy, x2 - x1, gh);
      g2d.fill(gradientRect);
    }

    for (int i = 0; i < positions.length; i++) {
      final float kx = positions[i] * gw;
      knobUI.paintKnob(g2d, kx, gh, colors[i], (i % 2) != 0);
    }
  }

  public Dimension getMinimumSize() {
    return new Dimension((int) (KnobUI.knobWidth() * 5.0f), (int) (KnobUI.knobHeight() * 12.0f * editor.getGradient().size()));
  }

  public boolean inKnobRegion(final int mouseY) {
    final int kh = (int) knobUI.knobHeight();
    final int h = editor.getHeight();
    final int regionMinY = h - kh;

    return (regionMinY <= mouseY);
  }

  public float toPosition(final int mouseX) {
    final int w = editor.getWidth();
    final float kw = knobUI.knobWidth();
    final float gx = kw / 2.0f;
    final float gw = w - kw;

    float position = (mouseX - gx) / gw;
    // cap the position between 0 and 1
    if (position < 0.0f)
      position = 0.0f;
    else if (position > 1.0f)
      position = 1.0f;
    return position;
  }

  protected static class KnobUI {
    protected static final float KNOB_WIDTH = 16.0f;

    protected static final float KNOB_BORDER = 1.2f;
    protected static final Stroke KNOB_BORDER_STROKE = new BasicStroke(KNOB_BORDER);
    protected static final float KNOB_INNER_PADDING = 1.0f;
    protected static final float KNOB_SELECTED_BORDER_FACTOR = 1.3f;

    protected static final Color KNOB_INNER_COLOR = new Color(0xEDEDED);
    protected static final Color KNOB_BORDER_COLOR = new Color(0xA0A0A0);
    protected static final Color KNOB_SELECTED_COLOR = new Color(0x47CAACC);

    protected static final Path2D.Float KNOB_PATH = knobPath();
    protected static final Path2D.Float KNOB_SELECTED_PATH = knobSelectedPath();
    protected static final Rectangle2D  KNOB_BOUNDS = KNOB_SELECTED_PATH.getBounds2D();
    protected static final Path2D.Float COLOR_FILL_PATH = colorFillPath();

    protected static Path2D.Float knobPath() {
      final float k = KNOB_WIDTH;
      final Path2D.Float path = new Path2D.Float();
      path.moveTo(k * 0.5f, 0.0f);            // tip
      path.lineTo(k       , k * 1.0f / 3.0f); // top-right corner
      path.lineTo(k       , k * 4.0f / 3.0f); // bottom-right
      path.lineTo(0.0f    , k * 4.0f / 3.0f); // bottom-left
      path.lineTo(0.0f    , k * 1.0f / 3.0f); // top-left corner
      path.closePath();
      return path;
    }

    protected static Path2D.Float knobSelectedPath() {
      final Path2D.Float path = KNOB_PATH;
      final Path2D.Float newPath = new Path2D.Float(path);

      final AffineTransform t = new AffineTransform();
      t.setToScale(KNOB_SELECTED_BORDER_FACTOR, KNOB_SELECTED_BORDER_FACTOR);
      newPath.transform(t);

      final Rectangle2D pathBounds = path.getBounds2D();
      final Rectangle2D newPathBounds = newPath.getBounds2D();
      final double centerx = (newPathBounds.getWidth() - pathBounds.getWidth()) / 2.0;
      final double centery = (newPathBounds.getHeight() - pathBounds.getHeight()) / 2.0;
      t.setToTranslation(-centerx, -centery);
      newPath.transform(t);

      return newPath;
    }

    protected static Path2D.Float colorFillPath() {
      final float k = KNOB_WIDTH;
      final float kp = KNOB_INNER_PADDING;
      final Path2D.Float path = new Path2D.Float();
      path.moveTo(k - kp , k * 1.0f / 3.0f + kp); // top-right corner
      path.lineTo(k - kp , k * 4.0f / 3.0f - kp); // bottom-right
      path.lineTo(kp     , k * 4.0f / 3.0f - kp); // bottom-left
      path.lineTo(kp     , k * 1.0f / 3.0f + kp); // top-left corner
      path.closePath();
      return path;     
    }

    public static float knobWidth() {
      return (float) KNOB_BOUNDS.getWidth();
    }

    public static float knobHeight() {
      return (float) KNOB_BOUNDS.getHeight();
    }

    protected OffsetPath knobPath = new OffsetPath(KNOB_PATH);
    protected OffsetPath knobSelectedPath = new OffsetPath(KNOB_SELECTED_PATH);
    protected OffsetPath colorFillPath = new OffsetPath(COLOR_FILL_PATH);

    public void paintKnob(final Graphics2D g, final float x, final float y,
                          final Color knobColor, final boolean selected) {
      if (selected) {
        g.setColor(KNOB_SELECTED_COLOR);
        g.fill(knobSelectedPath.offset(x, y)); }

      g.setColor(KNOB_INNER_COLOR);
      g.fill(knobPath.offset(x, y));

      g.setStroke(KNOB_BORDER_STROKE);
      g.setPaint(KNOB_BORDER_COLOR);
      g.draw(knobPath.path());

      g.setColor(knobColor);
      g.fill(colorFillPath.offset(x, y));
    }
  }
}

class OffsetPath {
  final Path2D path;
  final AffineTransform t = new AffineTransform();
  float lastx = 0.0f;
  float lasty = 0.0f;
  public OffsetPath(final Path2D path) {
    this.path = new Path2D.Float(path);
  }

  public Path2D path() {
    return path;
  }

  public Path2D offset(final float x, final float y) {
    t.setToTranslation(x - lastx, y - lasty);
    path.transform(t);
    lastx = x;
    lasty = y;
    return path;
  }
}
