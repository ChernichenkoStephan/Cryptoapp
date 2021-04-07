package com.encryption.View;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LSBView extends CodeView {

    private static final Logger log = Logger.getLogger(LSBView.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    public LSBView() {
        super(new JLabel("LSB encryption"),
                new JButton("Extract"),
                new JButton("Menu"),
                new JTextArea("Data file"),
                new JTextArea("Image file"),
                new JLabel("BMP message"),
                new JPanel(),
                new JRadioButton("Insert"),
                new JRadioButton("Extract")
        );
        Level chosenLevel = log.getLevel();
        log.setLevel(Level.OFF);
        secondCheckBox.setSelected(true);
        rightPanel.setVisible(true);
        log.setLevel(chosenLevel);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        log.info(e.toString());
        if (e.getSource() == firstCheckBox) {
            codeButton.setActionCommand("ENCODE_ACTION");
            codeButton.setText("Insert");
            notifyActionListeners("", 4);
        }
        if (e.getSource() == secondCheckBox) {
            codeButton.setActionCommand("DECODE_ACTION");
            codeButton.setText("Extract");
            notifyActionListeners("", 5);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "LSB_MESSAGE_TEXT"  -> messageLabel.setText(evt.getNewValue().toString());
            case "LSB_MESSAGE_COLOR" -> messageLabel.setForeground((Color) evt.getNewValue());

            case "LSB_KEY_TEXT"      -> rightPanel.setText(evt.getNewValue().toString());
            case "LSB_KEY_COLOR"     -> rightPanel.setForeground((Color) evt.getNewValue());

            case "LSB_FILE_TEXT"     -> leftPanel.setText(evt.getNewValue().toString());
            case "LSB_FILE_COLOR"    -> leftPanel.setForeground((Color) evt.getNewValue());
        }
    }
}
