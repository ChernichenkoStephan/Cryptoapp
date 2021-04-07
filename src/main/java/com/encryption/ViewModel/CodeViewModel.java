package com.encryption.ViewModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CodeViewModel extends ViewModel {

    private static final Logger log = Logger.getLogger(CodeViewModel.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    protected String fileText;
    protected Color  fileColor;

    protected String keyText;
    protected Color  keyColor;

    protected String massageText;
    protected Color  massageColor;

    protected String state = "ENCODE";

    protected File leftAreaFile = null;
    protected File rightAreaFile = null;

    public CodeViewModel(PropertyChangeListener listener) {
        super(listener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("ID: " + e.getID());
        switch (e.getID()) {
            case 1001   -> processButton(e);
            case 2      -> processKeyAreaEvent(e);
            case 3      -> processFileAreaEvent(e);
            case 4, 5   -> processSwitchAction(e);
            default     -> log.warning("Wrong actionEvent");
        }
    }

    protected void delayResetMessage(long delay, String messageText) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                setMassageColor(Color.GRAY);
                setMassageText(messageText);
            }
        }, delay);
    }

    protected boolean checkKeyFile(File keyFile) {
        return keyFile.getPath().contains(".CKey");
    }

    protected boolean checkObjectFile(File keyFile, String extension) {
        return keyFile.getPath().contains(extension);
    }

    /**
     * Router function to call proper method
     */
    protected void processButton(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "ENCODE_ACTION" -> processEncodeEvent(e);
            case "DECODE_ACTION" -> processDecodeEvent(e);
            case "MENU_VIEW" -> processMenuEvent(e);
            default -> log.warning("Wrong actionEvent");
        }
    }

    protected void processSwitchAction(ActionEvent e) {
        switch (e.getID()) {
            case 4      -> state = "ENCODE";
            case 5      -> state = "DECODE";
        }
        log.info("SwitchAction  -> " + state);
        resetState();
    }

    protected void processKeyAreaEvent(ActionEvent e) {
        log.info(e.getActionCommand());
        final String path = e.getActionCommand();
        final File keyFile = new File(path);

        if (checkKeyFile(keyFile)) {
            this.rightAreaFile = keyFile;
            setKeyText(path);
            setKeyColor(Color.green);
        } else {
            setMassageText("Wrong file type. Only \".CKey\" supported");
            setMassageColor(Color.red);
            delayResetMessage(4000, "Put new files");
        }
    }

    protected void processFileAreaEvent(ActionEvent e) {
        log.info(e.getActionCommand());
        final String path = e.getActionCommand();
        final File objectFile = new File(path);

        switch (state) {
            case "ENCODE":
            case "FREQUENCY":
                this.leftAreaFile = objectFile;
                setFileText(path);
                setFileColor(Color.green);
                break;
            case "DECODE":
                if (checkObjectFile(objectFile, ".cringe")) {
                    this.leftAreaFile = objectFile;
                    setFileText(path);
                    setFileColor(Color.green);
                } else {
                    setMassageText("Wrong file type. Only \".cringe\" supported");
                    setMassageColor(Color.red);
                    delayResetMessage(4000, "Put new files");
                }
                break;
        }
    }

    protected boolean checkObjectFile(File keyFile) {
        return keyFile.getPath().contains(".");
    }

    protected abstract void processEncodeEvent(ActionEvent e);

    protected abstract void processDecodeEvent(ActionEvent e);

    protected void processMenuEvent(ActionEvent e) {
        log.info(e.getActionCommand());
        resetState();
    }

    protected abstract void resetState();

    public abstract void setFileText(String fileText);

    public abstract void setFileColor(Color fileColor);

    public abstract void setKeyText(String keyText);

    public abstract void setKeyColor(Color keyColor);

    public abstract void setMassageText(String massageText);

    public abstract void setMassageColor(Color massageColor);
}
