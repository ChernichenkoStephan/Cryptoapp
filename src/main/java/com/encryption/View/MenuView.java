package com.encryption.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuView extends JPanel {

    private final JButton simpleButton;
    private final JButton RSAButton;
    private final JButton LSBButton;
    private final JButton BlowfishButton;

    public MenuView(ActionListener listener) {
        setLayout(new GridLayout(4,1));

        this.simpleButton   = new JButton("Simple");
        this.RSAButton      = new JButton("RSA");
        this.LSBButton      = new JButton("LSB");
        this.BlowfishButton = new JButton("Blowfish");

        simpleButton.setActionCommand(ViewType.SIMPLE_VIEW.name());
        RSAButton.setActionCommand(ViewType.RSA_VIEW.name());
        LSBButton.setActionCommand(ViewType.LSB_VIEW.name());
        BlowfishButton.setActionCommand(ViewType.BLOWFISH_VIEW.name());

        add(simpleButton);
        add(RSAButton);
        add(LSBButton);
        add(BlowfishButton);

        simpleButton.addActionListener(listener);
        RSAButton.addActionListener(listener);
        LSBButton.addActionListener(listener);
        BlowfishButton.addActionListener(listener);

        setPreferredSize(new Dimension(700, 500));
        setSize(700, 500);
    }

    public JButton getSimpleButton() {
        return simpleButton;
    }

    public JButton getRSAButton() {
        return RSAButton;
    }

    public JButton getLSBButton() {
        return LSBButton;
    }

    public JButton getBlowfishButton() {
        return BlowfishButton;
    }
}
