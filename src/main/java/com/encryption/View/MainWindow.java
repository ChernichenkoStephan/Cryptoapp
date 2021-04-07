package com.encryption.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow implements ActionListener {

    public static MainWindow defaultWindow = new MainWindow();

    private final CardLayout layout;
    private final JFrame frame;
    private final JPanel panel;

    private MainWindow() {
        frame = new JFrame();
        layout = new CardLayout();
        panel = new JPanel(layout);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        frame.setPreferredSize(new Dimension(800, 550));
        frame.setSize(800, 550);
        frame.add(panel);
    }

    public void draw() {
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        layout.show(panel, e.getActionCommand());
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getPanel() {
        return panel;
    }
}
