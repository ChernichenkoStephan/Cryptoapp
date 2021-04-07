package com.encryption.View;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class CodeView extends JPanel implements PropertyChangeListener, ItemListener {

    private static final Logger log = Logger.getLogger(CodeView.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    protected final ArrayList<ActionListener> listenerList = new ArrayList<>();;

    protected final  JButton   codeButton;
    protected final  JButton   menuButton;
    protected final  JTextArea leftPanel;
    protected final  JTextArea rightPanel;
    protected final  JLabel    messageLabel;

    protected final  JPanel checkBoxPanel;
    protected final  JRadioButton firstCheckBox;
    protected final  JRadioButton secondCheckBox;
    protected final  ButtonGroup  buttonGroup;

    protected JPanel fileAreasPanel = new JPanel(new GridLayout(1,2));
    protected JPanel controlPanel   = new JPanel(new GridLayout(1,3));
    protected JPanel midPanel       = new JPanel(new GridLayout(2,1));

    public CodeView(JLabel nameLabel, JButton codeButton, JButton menuButton, JTextArea leftPanel, JTextArea rightPanel, JLabel messageLabel, JPanel checkBoxPanel, JRadioButton firstCheckBox, JRadioButton secondCheckBox) {

        setLayout(new GridBagLayout());

        this.codeButton = codeButton;
        this.menuButton = menuButton;
        this.leftPanel = leftPanel;
        this.rightPanel = rightPanel;
        this.messageLabel = messageLabel;
        this.checkBoxPanel = checkBoxPanel;
        this.firstCheckBox = firstCheckBox;
        this.secondCheckBox = secondCheckBox;

        leftPanel.setLineWrap(true);
        leftPanel.setWrapStyleWord(true);

        rightPanel.setLineWrap(true);
        rightPanel.setWrapStyleWord(true);

        nameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        messageLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        checkBoxPanel.add(firstCheckBox);
        checkBoxPanel.add(secondCheckBox);

        fileAreasPanel.add(leftPanel);
        fileAreasPanel.add(rightPanel);

        midPanel.add(messageLabel);
        midPanel.add(checkBoxPanel);

        controlPanel.add(menuButton);
        controlPanel.add(midPanel);
        controlPanel.add(codeButton);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 350, 0, 0);
        add(nameLabel, c);

        c.insets = new Insets(3, 0, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 280;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        add(fileAreasPanel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 125;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 2;
        add(controlPanel, c);

        codeButton.setActionCommand("ENCODE_ACTION");
        menuButton.setActionCommand(ViewType.MENU_VIEW.name());

        rightPanel.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                String filePath = processFilesDrop(evt);
                notifyActionListeners(filePath, 2);
            }
        });
        leftPanel.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                String filePath = processFilesDrop(evt);
                notifyActionListeners(filePath, 3);
            }
        });
        rightPanel.setVisible(false);

        firstCheckBox.setSelected(true);
        firstCheckBox.setMnemonic(0);
        secondCheckBox.setMnemonic(0);
        firstCheckBox.addItemListener(this);
        secondCheckBox.addItemListener(this);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(firstCheckBox);
        buttonGroup.add(secondCheckBox);
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(listener);
    }

    protected void notifyActionListeners(String command, int id) {
        listenerList.forEach(listener -> listener.actionPerformed(new ActionEvent(this, id, command)));
    }

    protected String processFilesDrop(DropTargetDropEvent evt) {
        try {
            evt.acceptDrop(DnDConstants.ACTION_COPY);
            List droppedFiles = (List)
                    evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            log.info(droppedFiles.toString());
            return ((File) droppedFiles.get(0)).getPath();
        } catch (Exception ex) {
            log.warning(Arrays.toString(ex.getStackTrace()) + ex.getMessage());
            return "";
        }
    }

    public JButton getCodeButton() {
        return codeButton;
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    public JTextArea getLeftPanel() {
        return leftPanel;
    }

    public JTextArea getRightPanel() {
        return rightPanel;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        log.info("itemStateChanged");
        if (e.getSource() == firstCheckBox) {
            rightPanel.setVisible(false);
            codeButton.setActionCommand("ENCODE_ACTION");
            codeButton.setText("Encode");
        }
        if (e.getSource() == secondCheckBox) {
            rightPanel.setVisible(true);
            codeButton.setActionCommand("DECODE_ACTION");
            codeButton.setText("Decode");
        }
    }
}