package samad.components.gradient;

import java.awt.Color;

import java.util.SortedSet;
import java.util.TreeSet;

public class Gradient {
  public static class Stop implements Comparable<Stop> {
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
    }

    public void setColor(final Color color) {
      if (color == null)
        throw new IllegalArgumentException("color cannot be null");
      this.color = color;
    }

    public int compareTo(final Stop that) {
      return Double.compare(this.getPosition(), that.getPosition());
    }
  }	

  SortedSet<Stop> stops = new TreeSet<Stop>();

  public Gradient() {
    this(Color.RED, Color.BLACK, Color.GREEN);
  }

  public Gradient(Gradient that) {
    for (Stop stop : that.getStops())
      this.add(stop);
  }

  public Gradient(Color ... colors) {
    final int n = colors.length;
    if (n < 2)
      throw new IllegalArgumentException("must be at least two colors");
    for (int i = 0; i < n; i++)
      this.add(i / ((float) (n - 1)), colors[i]);
  }

  public boolean add(final Stop stop) {
    return stops.add(new Stop(stop));
  }

  public boolean add(final float position, final Color color) {
    return stops.add(new Stop(position, color));
  }

  public boolean remove(final Stop stop) {
    if (stops.size() <= 2)
      throw new IllegalStateException("Gradient must have at least two stops");
    return stops.remove(stop);
  }

  public float[] getAllPositions(float[] array) {
    if (array == null || array.length != stops.size())
      array = new float[stops.size()];
    int i = 0;
    for (Stop stop : stops)
      array[i++] = stop.getPosition();
    return array;
  }

  public Color[] getAllColors(Color[] array) {
    if (array == null || array.length != stops.size())
      array = new Color[stops.size()];
    int i = 0;
    for (Stop stop : stops)
      array[i++] = stop.getColor();
    return array;
  }

  public Iterable<Stop> getStops() {
    return stops;
  }

  public int size() {
    return stops.size();
  }
}