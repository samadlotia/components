package samad.components.taskwindow;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
import java.awt.RenderingHints;

import javax.swing.border.Border;

public class RoundedBorder implements Border {
	final Insets insets = new Insets(5, 15, 15, 15);
	final float radius = 10.0f;
	final Color color = new Color(0xdddddd);
	final Color borderColor = new Color(0xcccccc);

	public RoundedBorder() {}

	public Insets getBorderInsets(Component c) {
		return insets;
	}

	public boolean isBorderOpaque() {
		return true;
	}

	final RoundRectangle2D.Float rect = new RoundRectangle2D.Float();
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		rect.setRoundRect(x, y, w, h, radius, radius);

		g2d.setColor(color);
		g2d.fill(rect);

		rect.setRoundRect(x, y, w - 1, h - 1, radius, radius);
		g2d.setColor(borderColor);
		g2d.draw(rect);
	}
}