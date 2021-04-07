package com.encryption.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlainView extends CodeView {

    private static final Logger log = Logger.getLogger(PlainView.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    private final JRadioButton frequencyCheckBox;

    public PlainView() {

        super(new JLabel("Simple slide encryption"),
                new JButton("Encode"),
                new JButton("Menu"),
                new JTextArea("Object file"),
                new JTextArea("Key file"),
                new JLabel("Message"),
                new JPanel(),
                new JRadioButton("Encode"),
                new JRadioButton("Decode")
        );

        frequencyCheckBox = new JRadioButton("Frequency");
        frequencyCheckBox.addItemListener(this);
        buttonGroup.add(frequencyCheckBox);
        checkBoxPanel.add(frequencyCheckBox);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        log.info(e.toString());
        if (e.getSource() == firstCheckBox) {
            rightPanel.setVisible(false);
            codeButton.setActionCommand("ENCODE_ACTION");
            rightPanel.setText("Key file");
            codeButton.setText("Encode");
            notifyActionListeners("", 4);
        }
        if (e.getSource() == secondCheckBox) {
            rightPanel.setVisible(true);
            codeButton.setActionCommand("DECODE_ACTION");
            rightPanel.setText("Key file");
            codeButton.setText("Decode");
            notifyActionListeners("", 5);
        }
        if (e.getSource() == frequencyCheckBox) {
            rightPanel.setVisible(false);
            codeButton.setActionCommand("FREQUENCY_ACTION");
            rightPanel.setText("Output file");
            codeButton.setText("Analyze");
            notifyActionListeners("", 6);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "SIMPLE_MESSAGE_TEXT"  -> messageLabel.setText(evt.getNewValue().toString());
            case "SIMPLE_MESSAGE_COLOR" -> messageLabel.setForeground((Color) evt.getNewValue());

            case "SIMPLE_KEY_TEXT"      -> rightPanel.setText(evt.getNewValue().toString());
            case "SIMPLE_KEY_COLOR"     -> rightPanel.setForeground((Color) evt.getNewValue());

            case "SIMPLE_FILE_TEXT"     -> leftPanel.setText(evt.getNewValue().toString());
            case "SIMPLE_FILE_COLOR"    -> leftPanel.setForeground((Color) evt.getNewValue());
        }
    }

}
