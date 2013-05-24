package samad.components.gradient;

import javax.swing.plaf.ComponentUI;
import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GradientEditorUI extends ComponentUI {
  protected static final float KNOB_WIDTH = 8.0f;
  protected static final float KNOB_INNER_BORDER = 1.0f;
  protected static final Color KNOB_COLOR = new Color(0xc0c0c0);
  protected static final Color KNOB_SELECTED_COLOR = new Color(0x606060);
  protected static final BufferedImage KNOB_IMAGE = buildKnobImage(KNOB_COLOR, KNOB_SELECTED_COLOR);
  protected static final BufferedImage SELECTED_KNOB_IMAGE = buildKnobImage(KNOB_SELECTED_COLOR, KNOB_SELECTED_COLOR);

  final GradientEditor editor;

  // keep these as class members so that they won't have to be reallocated
  // every time paint() is called
  protected float[] positions = null;
  protected Color[] colors = null;
  final Rectangle2D rect = new Rectangle2D.Float();
  final Rectangle2D knobRect = new Rectangle2D.Float();

  public GradientEditorUI(final GradientEditor editor) {
    this.editor = editor;
  }

  public void paint(Graphics g, JComponent component) {
    final int w = editor.getWidth();
    final int h = editor.getHeight();
    final float k = KNOB_WIDTH;
    final float kib = KNOB_INNER_BORDER;
    final int kw = KNOB_IMAGE.getWidth();
    final int kh = KNOB_IMAGE.getHeight();
    final float gx = kw / 2.0f;
    final float gy = 0.0f;
    final float gw = w - kw;
    final float gh = h - kh;

    final Gradient gradient = editor.getGradient();
    positions = gradient.getAllPositions(positions);
    colors = gradient.getAllColors(colors);

    final Graphics2D g2d = (Graphics2D) g;
    //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (int i = 0; i < positions.length - 1; i++) {
      final float p1 = positions[i];
      final float p2 = positions[i + 1];

      final Color c1 = colors[i];
      final Color c2 = colors[i + 1];
      
      final GradientPaint p = new GradientPaint(p1 * gw + gx, 0.0f, c1,
                                                p2 * gw + gx, 0.0f, c2);
      g2d.setPaint(p);

      rect.setRect(p1 * gw + gx, gy, Math.ceil((p2 - p1) * gw), gh);
      g2d.fill(rect);
    }

/*
    for (int i = 0; i < positions.length - 1; i++) {
      final float p1 = positions[i];
      final float p2 = positions[i + 1];
      g2d.setColor(Color.black);
      g2d.drawLine((int) (p1 * gw + gx), (int) (gy), (int) (p2 * gw + gx), (int) (gy + gh));
    }
    */

    for (int i = 0; i < positions.length; i++) {
      final float kx = positions[i] * gw + gx - KNOB_WIDTH;
      final float ky = gh;
      g2d.drawImage(KNOB_IMAGE, (int) kx, (int) ky, null);

      g2d.setColor(colors[i]);
      knobRect.setRect(kx + kib + 1, ky + k + kib + 1, 2.0f * (k - kib) - 1, 2.0f * (k - kib) - 1);
      g2d.fill(knobRect);
    }
  }

  public Dimension getMinimumSize() {
    return new Dimension((int) (KNOB_WIDTH * 5.0f), (int) (KNOB_WIDTH * 6.0f * editor.getGradient().size()));
  }

  protected static BufferedImage buildKnobImage(final Color fillColor, final Color borderColor) {
    final float k  = KNOB_WIDTH;
    final float k2 = KNOB_WIDTH * 2.0f;
    final float k3 = KNOB_WIDTH * 3.0f;

    final int w = (int) Math.ceil(k2) + 1;
    final int h = (int) Math.ceil(k3) + 1;

    final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2d = (Graphics2D) image.getGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


    final GeneralPath path = new GeneralPath();
    path.moveTo(k,  0);
    path.lineTo(k2, k);
    path.lineTo(k2, k3);
    path.lineTo(0,  k3);
    path.lineTo(0,  k);
    path.lineTo(k,  0);

    g2d.setColor(fillColor);
    g2d.fill(path);

    g2d.setColor(borderColor);
    g2d.draw(path);

    return image;
  }

  public boolean inKnobRegion(int mouseY) {
    final int kh = KNOB_IMAGE.getHeight();
    final int h = editor.getHeight();
    final int regionMinY = h - kh;

    return (regionMinY <= mouseY);
  }

  public float toPosition(int mouseX) {
    final int w = editor.getWidth();
    final int kw = KNOB_IMAGE.getWidth();
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
}