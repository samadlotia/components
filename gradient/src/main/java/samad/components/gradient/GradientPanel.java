package samad.components.gradient;

import java.util.Arrays;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics;
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

    positionPanel.setAbsoluteRange(-30.0, 50.0);

    final GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;      c.gridy = 0;
    c.gridwidth = 3;  c.gridheight = 1;
    c.weightx = 1.0;  c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;
    c.insets = new Insets(0, 0, 10, 0);
    super.add(editor, c);

    c.gridx = 0;      c.gridy = 1;
    c.gridwidth = 1;  c.gridheight = 1;
    c.weightx = 0.0;  c.weighty = 0.0;
    super.add(colorPanel, c);

    c.gridx = 1;      c.gridy = 1;
    super.add(positionPanel, c);


  }	
}

class ColorPanel extends JPanel {
  public static final String COLOR_CHANGED = "color changed";
  final ColorWell well = new ColorWell();
  final JSlider sliderR = createColorSlider();
  final JSlider sliderG = createColorSlider();
  final JSlider sliderB = createColorSlider();
  final JTextField fieldR = createColorField();
  final JTextField fieldG = createColorField();
  final JTextField fieldB = createColorField();

  public ColorPanel() {
    super(new GridBagLayout());
    super.setBorder(BorderFactory.createTitledBorder("Color"));

    addChannelUpdates(fieldR, sliderR);
    addChannelUpdates(fieldG, sliderG);
    addChannelUpdates(fieldB, sliderB);

    final GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;      c.gridy = 0;
    c.gridwidth = 1;  c.gridheight = 3;
    c.weightx = 0.0;  c.weighty = 0.0;
    c.fill = GridBagConstraints.NONE;
    c.anchor = GridBagConstraints.NORTHWEST;
    c.insets = new Insets(0, 10, 10, 10);
    well.setBorder(BorderFactory.createEtchedBorder());
    well.setPreferredSize(new Dimension(48, 48));
    super.add(well, c);

    c.gridx = 1;      c.gridy = 0;
    c.gridwidth = 1;  c.gridheight = 1;
    super.add(new JLabel("R:"), c);

    c.gridx = 1;      c.gridy = 1;
    super.add(new JLabel("G:"), c);
    
    c.gridx = 1;      c.gridy = 2;
    super.add(new JLabel("B:"), c);

    c.gridx = 2;      c.gridy = 0;
    super.add(sliderR, c);

    c.gridx = 2;      c.gridy = 1;
    super.add(sliderG, c);

    c.gridx = 2;      c.gridy = 2;
    super.add(sliderB, c);

    c.gridx = 3;      c.gridy = 0;
    super.add(fieldR, c);

    c.gridx = 3;      c.gridy = 1;
    super.add(fieldG, c);

    c.gridx = 3;      c.gridy = 2;
    super.add(fieldB, c);

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
      sliderB.getValue());
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
      for (final JTextField field : Arrays.asList(fieldR, fieldG, fieldB)) {
        field.setEnabled(false);
        field.setText("");
      }
      for (final JSlider slider : Arrays.asList(sliderR, sliderG, sliderB)) {
        slider.setEnabled(false);
        slider.setValue(0);
      }
    } else {
      for (final JTextField field : Arrays.asList(fieldR, fieldG, fieldB)) {
        field.setEnabled(true);
      }
      for (final JSlider slider : Arrays.asList(sliderR, sliderG, sliderB)) {
        slider.setEnabled(true);
      }

      final int r = color.getRed();
      final int g = color.getGreen();
      final int b = color.getBlue();

      fieldR.setText(Integer.toString(r));
      fieldG.setText(Integer.toString(g));
      fieldB.setText(Integer.toString(b));

      sliderR.setValue(r);
      sliderG.setValue(g);
      sliderB.setValue(b);
    }
  }
}

class ColorWell extends JComponent {
  protected static final Color DISABLED_COLOR = new Color(0xD6D6D6);

  Color color = null;

  public void setColor(final Color color) {
    this.color = color;
    super.repaint();
  }

  public void paintComponent(Graphics g) {
    final Insets insets = super.getInsets();
    g.setColor(color == null ? DISABLED_COLOR : color);
    g.fillRect(insets.left, insets.top, super.getWidth() - insets.left - insets.right, super.getHeight() - insets.top - insets.bottom);
  }
}

class PositionPanel extends JPanel {
  public static final String POSITION_CHANGED = "position changed";

  double absRangeMin = 0.0;
  double absRangeMax = 1.0;

  final SpinnerNumberModel absSpinnerModel = new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1);
  final JSpinner absSpinner = createSpinner(absSpinnerModel);
  final JSpinner relSpinner = createSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));

  public PositionPanel() {
    super(new GridBagLayout());
    super.setBorder(BorderFactory.createTitledBorder("Position"));

    absSpinner.addChangeListener(new ChangeListener() {
      public void stateChanged(final ChangeEvent e) {
        final double absValue = ((Number) absSpinner.getValue()).doubleValue();
        relSpinner.setValue(absToRel(absValue));
        firePropertyChange(POSITION_CHANGED, null, null);
      }
    });

    relSpinner.addChangeListener(new ChangeListener() {
      public void stateChanged(final ChangeEvent e) {
        final double relValue = ((Number) relSpinner.getValue()).doubleValue();
        absSpinner.setValue(relToAbs(relValue));
        firePropertyChange(POSITION_CHANGED, null, null);
      }
    });

    final GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;      c.gridy = 0;
    c.gridwidth = 1;  c.gridheight = 1;
    c.weightx = 0.0;  c.weighty = 0.0;
    c.anchor = GridBagConstraints.NORTHWEST;
    c.insets = new Insets(0, 10, 10, 10);
    super.add(new JLabel("Absolute:"), c);

    c.gridx = 1;      c.gridy = 0;
    super.add(absSpinner, c);

    c.gridx = 0;      c.gridy = 1;
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

  public void setAbsoluteRange(final double min, final double max) {
    this.absRangeMin = min;
    this.absRangeMax = max;
    absSpinnerModel.setMinimum(min);
    absSpinnerModel.setMaximum(max);
    absSpinnerModel.setStepSize(Math.abs(0.1 * (max - min)));
  }

  public void setPosition(final Float position) {
    if (position == null) {
      absSpinner.setEnabled(false);
      relSpinner.setEnabled(false);
      absSpinner.setValue(new Double(0.0));
      relSpinner.setValue(new Double(0.0));
    } else {
      absSpinner.setEnabled(true);
      relSpinner.setEnabled(true);
      relSpinner.setValue(position);
    }
  }

  public float getPosition() {
    return (((Number) relSpinner.getValue()).floatValue());
  }

  private double absToRel(final double abs) {
    return (abs - absRangeMin) / (absRangeMax - absRangeMin);
  }

  private double relToAbs(final double rel) {
    return absRangeMin + rel * (absRangeMax - absRangeMin);
  }
}
