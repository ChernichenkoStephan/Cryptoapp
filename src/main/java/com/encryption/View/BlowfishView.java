package com.encryption.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlowfishView extends CodeView {

    private static final Logger log = Logger.getLogger(BlowfishView.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    public BlowfishView() {
        super(new JLabel("Blowfish encryption"),
                new JButton("Encode"),
                new JButton("Menu"),
                new JTextArea("Object file"),
                new JTextArea("Key file"),
                new JLabel("Blowfish message"),
                new JPanel(),
                new JRadioButton("Encode"),
                new JRadioButton("Decode")
        );
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
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "BLOWFISH_MESSAGE_TEXT"  -> messageLabel.setText(evt.getNewValue().toString());
            case "BLOWFISH_MESSAGE_COLOR" -> messageLabel.setForeground((Color) evt.getNewValue());

            case "BLOWFISH_KEY_TEXT"      -> rightPanel.setText(evt.getNewValue().toString());
            case "BLOWFISH_KEY_COLOR"     -> rightPanel.setForeground((Color) evt.getNewValue());

            case "BLOWFISH_FILE_TEXT"     -> leftPanel.setText(evt.getNewValue().toString());
            case "BLOWFISH_FILE_COLOR"    -> leftPanel.setForeground((Color) evt.getNewValue());
        }
    }
}
