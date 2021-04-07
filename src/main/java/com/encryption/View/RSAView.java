package com.encryption.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RSAView extends CodeView {

    private static final Logger log = Logger.getLogger(RSAView.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    private JButton genKey = new JButton("Generate key");

    public RSAView() {
        super(new JLabel("RSA encryption"),
                new JButton("Encode"),
                new JButton("Menu"),
                new JTextArea("Object file / keys target folder"),
                new JTextArea("Open key file"),
                new JLabel("RSA ENCODE Message"),
                new JPanel(),
                new JRadioButton("Encode"),
                new JRadioButton("Decode")
        );
        rightPanel.setVisible(true);
        controlPanel.setLayout(new GridLayout(1, 4));
        genKey.setActionCommand("KEY_GEN_ACTION");
        controlPanel.add(genKey);
    }

    public JButton getGenKeyButton() {
        return genKey;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        log.info(e.toString());
        if (e.getSource() == firstCheckBox) {
            codeButton.setActionCommand("ENCODE_ACTION");
            rightPanel.setText("Key file");
            codeButton.setText("Encode");
            notifyActionListeners("", 4);
        }
        if (e.getSource() == secondCheckBox) {
            codeButton.setActionCommand("DECODE_ACTION");
            rightPanel.setText("Key file");
            codeButton.setText("Decode");
            notifyActionListeners("", 5);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "RSA_MESSAGE_TEXT"  -> messageLabel.setText(evt.getNewValue().toString());
            case "RSA_MESSAGE_COLOR" -> messageLabel.setForeground((Color) evt.getNewValue());

            case "RSA_KEY_TEXT"      -> rightPanel.setText(evt.getNewValue().toString());
            case "RSA_KEY_COLOR"     -> rightPanel.setForeground((Color) evt.getNewValue());

            case "RSA_FILE_TEXT"     -> leftPanel.setText(evt.getNewValue().toString());
            case "RSA_FILE_COLOR"    -> leftPanel.setForeground((Color) evt.getNewValue());
        }
    }
}
