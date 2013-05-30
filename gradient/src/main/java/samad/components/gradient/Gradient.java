package samad.components.gradient;

import java.awt.Color;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class Gradient {
  public class Stop implements Comparable<Stop> {
    protected float position;
    protected Color color;

    public Stop(final Stop stop) {
      this(stop.getPosition(), stop.getColor());
    }

    public Stop(final float position, final Color color) {
      if (0.0f > position || position > 1.0f)
        throw new IllegalArgumentException("position must be in [0.0, 1.0]");
      if (color == null)
        throw new IllegalArgumentException("color cannot be null");
      this.position = position;
      this.color = color;
    }

    public float getPosition() {
      return position;
    }

    public Color getColor() {
      return color;
    }

    public void setPosition(final float position) {
      if (0.0f > position || position > 1.0f)
        throw new IllegalArgumentException("position must be in [0.0, 1.0]");
      this.position = position;
      Collections.sort(stops);
    }

    public void setColor(final Color color) {
      if (color == null)
        throw new IllegalArgumentException("color cannot be null");
      this.color = color;
    }

    public int compareTo(final Stop that) {
      return Double.compare(this.getPosition(), that.getPosition());
    }

    public String toString() {
      return String.format("%.2f @ 0x%x", position, color.getRGB());
    }
  }	

  List<Stop> stops = new ArrayList<Stop>();

  public Gradient() {
    this(Color.RED, Color.BLACK, Color.GREEN);
  }

  public Gradient(Gradient that) {
    for (Stop stop : that.getStops())
      this.add(stop);
    Collections.sort(stops);
  }

  public Gradient(Color ... colors) {
    final int n = colors.length;
    if (n < 2)
      throw new IllegalArgumentException("must be at least two colors");
    for (int i = 0; i < n; i++)
      this.add(i / ((float) (n - 1)), colors[i]);
  }

  public boolean add(final Stop stop) {
    final boolean result = stops.add(new Stop(stop));
    Collections.sort(stops);
    return result;
  }

  public Stop add(final float position, final Color color) {
    final Stop stop = new Stop(position, color);
    stops.add(stop);
    Collections.sort(stops);
    return stop;
  }

  public boolean remove(final Stop stop) {
    if (stops.size() <= 2)
      throw new IllegalStateException("Gradient must have at least two stops");
    final boolean result = stops.remove(stop);
    Collections.sort(stops);
    return result;
  }

  public Iterable<Stop> getStops() {
    return stops;
  }

  public int size() {
    return stops.size();
  }

  public String toString() {
    return stops.toString();
  }

  public int indexOfStop(final Stop stop) {
    return stops.indexOf(stop);
  }

  public Stop stopAtIndex(final int i) {
    return stops.get(i);
  }
}