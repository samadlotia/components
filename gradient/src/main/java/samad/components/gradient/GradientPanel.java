package samad.components.gradient;

import java.util.Arrays;
import java.util.List;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.BorderFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class GradientPanel extends JPanel {
  final GradientEditor editor = new GradientEditor();
  final ColorPanel colorPanel = new ColorPanel();
  final PositionPanel positionPanel = new PositionPanel();

  public GradientPanel() {
    super(new GridBagLayout());

    editor.addPropertyChangeListener(editor.SELECTED_STOP_CHANGED, new PropertyChangeListener() {
      public void propertyChange(final PropertyChangeEvent e) {
        final Gradient.Stop selectedStop = editor.getSelectedStop();
        colorPanel.setColor(selectedStop == null ? null : selectedStop.getColor());
        positionPanel.setPosition(selectedStop == null ? null : selectedStop.getPosition());
      }
    });

    colorPanel.addPropertyChangeListener(ColorPanel.COLOR_CHANGED, new PropertyChangeListener() {
      public void propertyChange(final PropertyChangeEvent e) {
        final Color newColor = colorPanel.getColor();
        final Gradient.Stop selectedStop = editor.getSelectedStop();
        if (selectedStop != null) {
          selectedStop.setColor(newColor);
          editor.repaint();
        }
      }
    });

    editor.addPropertyChangeListener(editor.SELECTED_STOP_POSITION_CHANGED, new PropertyChangeListener() {
      public void propertyChange(final PropertyChangeEvent e) {
        final Gradient.Stop selectedStop = editor.getSelectedStop();
        positionPanel.setPosition(selectedStop == null ? null : selectedStop.getPosition());
      }
    });

    positionPanel.addPropertyChangeListener(PositionPanel.POSITION_CHANGED, new PropertyChangeListener() {
      public void propertyChange(final PropertyChangeEvent e) {
        final float newPosition = (float) positionPanel.getPosition();
        final Gradient.Stop selectedStop = editor.getSelectedStop();
        if (selectedStop != null) {
          selectedStop.setPosition(newPosition);
          editor.repaint();
        }
      }
    });

    final GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;      c.gridy = 0;
    c.gridwidth = 3;  c.gridheight = 1;
    c.weightx = 1.0;  c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;
    c.insets = new Insets(20, 10, 10, 10);
    super.add(editor, c);

    c.gridx = 0;      c.gridy = 1;
    c.gridwidth = 1;  c.gridheight = 1;
    c.weightx = 0.0;  c.weighty = 0.0;
    c.insets = new Insets(0, 10, 10, 10);
    super.add(colorPanel, c);

    c.gridx = 1;      c.gridy = 1;
    c.insets = new Insets(0, 0, 10, 10);
    super.add(positionPanel, c);
  }	
}

class ColorPanel extends JPanel {
  public static final String COLOR_CHANGED = "color changed";
  final ColorWell well = new ColorWell();
  final JSlider sliderR = createColorSlider();
  final JSlider sliderG = createColorSlider();
  final JSlider sliderB = createColorSlider();
  final JSlider sliderA = createColorSlider();
  final JTextField fieldR = createColorField();
  final JTextField fieldG = createColorField();
  final JTextField fieldB = createColorField();
  final JTextField fieldA = createColorField();

  public ColorPanel() {
    super(new GridBagLayout());
    super.setBorder(BorderFactory.createTitledBorder("Color"));

    addChannelUpdates(fieldR, sliderR);
    addChannelUpdates(fieldG, sliderG);
    addChannelUpdates(fieldB, sliderB);
    addChannelUpdates(fieldA, sliderA);

    final GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;      c.gridy = 0;
    c.gridwidth = 1;  c.gridheight = 4;
    c.weightx = 0.0;  c.weighty = 0.0;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.NORTHWEST;
    c.insets = new Insets(10, 10, 10, 10);
    well.setBorder(BorderFactory.createEtchedBorder());
    well.setPreferredSize(new Dimension(48, 48));
    super.add(well, c);

    c.gridwidth = 1;  c.gridheight = 1;

    c.gridx = 1;      c.gridy = 0;
    c.insets = new Insets(10, 0, 0, 10);
    super.add(new JLabel("R:"), c);

    c.gridx = 2;      c.gridy = 0;
    super.add(sliderR, c);

    c.gridx = 3;      c.gridy = 0;
    super.add(fieldR, c);

    c.gridx = 1;      c.gridy = 1;
    super.add(new JLabel("G:"), c);

    c.gridx = 2;      c.gridy = 1;
    super.add(sliderG, c);

    c.gridx = 3;      c.gridy = 1;
    super.add(fieldG, c);
    
    c.gridx = 1;      c.gridy = 2;
    super.add(new JLabel("B:"), c);

    c.gridx = 2;      c.gridy = 2;
    super.add(sliderB, c);

    c.gridx = 3;      c.gridy = 2;
    super.add(fieldB, c);
    
    c.gridx = 1;      c.gridy = 3;
    c.insets = new Insets(10, 0, 10, 10);
    super.add(new JLabel("A:"), c);

    c.gridx = 2;      c.gridy = 3;
    super.add(sliderA, c);

    c.gridx = 3;      c.gridy = 3;
    super.add(fieldA, c);

  }

  private JTextField createColorField() {
    final JTextField field = new JTextField(4);
    field.setEnabled(false);
    return field;
  }

  private JSlider createColorSlider() {
    final JSlider slider = new JSlider(0, 255, 0);
    slider.setPaintTicks(true);
    slider.setMajorTickSpacing(64);
    slider.setMinorTickSpacing(16);
    slider.setEnabled(false);
    return slider;
  }

  private void addChannelUpdates(final JTextField field, final JSlider slider) {
    field.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        field.setText(validateChannelNumber(field.getText()));
        slider.setValue(Integer.parseInt(field.getText()));
        updateColor();
      }
    });

    field.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        field.setText(validateChannelNumber(field.getText()));
        slider.setValue(Integer.parseInt(field.getText()));
        updateColor();
      }
    });

    slider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        field.setText(Integer.toString(slider.getValue()));
        updateColor();
      }
    });
  }

  public Color getColor() {
    return new Color(
      sliderR.getValue(),
      sliderG.getValue(),
      sliderB.getValue(),
      sliderA.getValue()
      );
  }

  private void updateColor() {
    final Color color = getColor();
    well.setColor(color);
    super.firePropertyChange(COLOR_CHANGED, null, null);
  }

  private static String validateChannelNumber(final String value) {
    try {
      final int numValue = Integer.parseInt(value);
      if (numValue < 0)
        return "0";
      else if (numValue > 255)
        return "255";
      else
        return value;
    } catch (NumberFormatException e) {
      return "0";
    }
  }

  public void setColor(final Color color) {
    well.setColor(color);
    if (color == null) {
      for (final JTextField field : Arrays.asList(fieldR, fieldG, fieldB, fieldA)) {
        field.setEnabled(false);
        field.setText("");
      }
      for (final JSlider slider : Arrays.asList(sliderR, sliderG, sliderB, sliderA)) {
        slider.setEnabled(false);
        slider.setValue(0);
      }
    } else {
      for (final JTextField field : Arrays.asList(fieldR, fieldG, fieldB, fieldA)) {
        field.setEnabled(true);
      }
      for (final JSlider slider : Arrays.asList(sliderR, sliderG, sliderB, sliderA)) {
        slider.setEnabled(true);
      }

      final int r = color.getRed();
      final int g = color.getGreen();
      final int b = color.getBlue();
      final int a = color.getAlpha();

      fieldR.setText(Integer.toString(r));
      fieldG.setText(Integer.toString(g));
      fieldB.setText(Integer.toString(b));
      fieldA.setText(Integer.toString(a));

      sliderR.setValue(r);
      sliderG.setValue(g);
      sliderB.setValue(b);
      sliderA.setValue(a);
    }
  }
}

class ColorWell extends JComponent {
  Color color = null;

  public void setColor(final Color color) {
    this.color = color;
    super.repaint();
  }

  public void paintComponent(Graphics g) {
    final Graphics2D g2d = (Graphics2D) g;
    final Insets insets = super.getInsets();
    final int x = insets.left;
    final int y = insets.top;
    final int w = super.getWidth() - insets.left - insets.right;
    final int h = super.getHeight() - insets.top - insets.bottom;
    g2d.setPaint(GradientEditorUI.checkeredPaint());
    g2d.fillRect(x, y, w, h);
    if (color != null) {
      g2d.setColor(color);
      g2d.fillRect(x, y, w, h);
    }
  }
}

class PositionPanel extends JPanel {
  public static final String POSITION_CHANGED = "position changed";

  final JSpinner relSpinner = createSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));

  public PositionPanel() {
    super(new GridBagLayout());
    super.setBorder(BorderFactory.createTitledBorder("Position"));

    relSpinner.addChangeListener(new ChangeListener() {
      public void stateChanged(final ChangeEvent e) {
        final double relValue = ((Number) relSpinner.getValue()).doubleValue();
        firePropertyChange(POSITION_CHANGED, null, null);
      }
    });

    final GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;      c.gridy = 1;
    c.gridwidth = 1;  c.gridheight = 1;
    c.weightx = 0.0;  c.weighty = 0.0;
    c.anchor = GridBagConstraints.NORTHWEST;
    c.insets = new Insets(10, 10, 10, 10);
    super.add(new JLabel("Relative:"), c);

    c.gridx = 1;      c.gridy = 1;
    super.add(relSpinner, c);

    c.gridx = 0;      c.gridy = 2;
    c.weightx = 0.0;  c.weighty = 1.0;
    super.add(new JPanel(), c);
  }

  private JSpinner createSpinner(final SpinnerNumberModel model) {
    final JSpinner spinner = new JSpinner(model);
    spinner.setEnabled(false);
    final JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
    editor.getTextField().setColumns(6);
    return spinner;
  }

  public void setPosition(final Float position) {
    if (position == null) {
      relSpinner.setEnabled(false);
      relSpinner.setValue(new Double(0.0));
    } else {
      relSpinner.setEnabled(true);
      relSpinner.setValue(position);
    }
  }

  public float getPosition() {
    return (((Number) relSpinner.getValue()).floatValue());
  }
}
