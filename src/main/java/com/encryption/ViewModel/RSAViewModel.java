package com.encryption.ViewModel;

import com.encryption.Encryption.RSAKeyGenerator;
import com.encryption.Encryption.RSAScrambler;
import com.encryption.Model.CodeFileService;
import com.encryption.Model.FileWorker;
import com.encryption.Model.RSAKey;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.encryption.Constants.*;

public class RSAViewModel extends CodeViewModel {

    private static final Logger log = Logger.getLogger(RSAViewModel.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    public RSAViewModel(PropertyChangeListener listener) {
        super(listener);
    }

    @Override
    protected void processEncodeEvent(ActionEvent e) {
        log.info(e.getActionCommand());
        if (leftAreaFile == null || rightAreaFile == null) {
            setMassageText("Fill file areas");
            setMassageColor(Color.red);
            delayResetMessage(5000, "Put new files");
            return;
        }
        if (state.equals("DECODE") && !leftAreaFile.getPath().contains(".")) {
            setMassageText("Folders not supported");
            setMassageColor(Color.red);
            delayResetMessage(5000, "Put new files");
            return;
        }

        code();

        setMassageText("Success");
        setMassageColor(Color.GREEN);
        delayResetMessage(2500, "Put new files");
    }

    @Override
    protected void processDecodeEvent(ActionEvent e) {
        log.info(e.getActionCommand());
        if (rightAreaFile != null && leftAreaFile != null) {

            deCode();

            setMassageText("Success");
            setMassageColor(Color.GREEN);
            delayResetMessage(2500, "Put new files");
        }
    }

    protected void processKeyGenEvent(ActionEvent e) {
        log.info(e.getActionCommand());
        if (leftAreaFile != null) {
            final RSAKey key = RSAKeyGenerator.nextKey();

            final File openKeyFile   = FileWorker.createFile(leftAreaFile.getPath() + "/openKey.CKey");
            final File closedKeyFile = FileWorker.createFile(leftAreaFile.getPath() + "/closedKey.CKey");

            final CodeFileService ofs = new CodeFileService(openKeyFile);
            final CodeFileService cfs = new CodeFileService(closedKeyFile);

            ofs.write(key.toOpenString());
            cfs.write(key.toClosedString());
        } else {
            setMassageText("Choose parent folder to store the keys");
            setMassageColor(Color.red);
            setFileText("Put here");
            setFileColor(Color.blue);
            delayResetMessage(5000, "Put new files");
        }
    }

    /**
     * Reads open key file, encrypts source file, changes source file extension
     * @return original data from source file
     */
    private List<Integer> code() {
        final CodeFileService fs  = new CodeFileService(leftAreaFile);
        final CodeFileService kfs = new CodeFileService(rightAreaFile);
        final List<Integer> dataToHide = fs.read();

        final String[] keyParams = kfs.readString().split("#");
        final RSAKey key = new RSAKey(Long.parseLong(keyParams[0]),Long.parseLong(keyParams[1]));

        final List<Integer> encData = RSAScrambler.encrypt(dataToHide, key);

        fs.writeCoded(encData);
        fs.changeExtension(fs.getExtension()+ ".cringe");
        return dataToHide;
    }

    /**
     * Reads closed key file, decrypts source file, changes source file extension to original
     * @return Decrypted data from encrypted file
     */
    private List<Integer> deCode() {
        final CodeFileService fs  = new CodeFileService(leftAreaFile);
        final CodeFileService kfs = new CodeFileService(rightAreaFile);

        final String[] keyParams = kfs.readString().split("#");
        final RSAKey key = new RSAKey(Long.parseLong(keyParams[0]),Long.parseLong(keyParams[1]),Long.parseLong(keyParams[2]));

        final List<Integer> hiddenData = fs.readCoded();
        final List<Integer> decodedData = RSAScrambler.decrypt(hiddenData, key);

        fs.writeBites(decodedData);
        fs.changeExtension("");
        return decodedData;
    }

    @Override
    protected void resetState() {
        log.info("RESET STATE");
        rightAreaFile = null;
        leftAreaFile = null;
        if (state.equals("ENCODE")) {
            setMassageText("RSA ENCODE Message");
            setKeyText("Open key file");
            setFileText("Object file / keys target folder");
        } else {
            setMassageText("RSA DECODE Message");
            setKeyText("Closed key file");
            setFileText("Encrypted file");
        }
        setMassageColor(Color.GRAY);
        setKeyColor(Color.GRAY);
        setFileColor(Color.GRAY);
    }

    protected void processButton(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "ENCODE_ACTION"    -> processEncodeEvent(e);
            case "DECODE_ACTION"    -> processDecodeEvent(e);
            case "MENU_VIEW"        -> processMenuEvent(e);
            case "KEY_GEN_ACTION"   -> processKeyGenEvent(e);
            default -> log.warning("Wrong actionEvent");
        }
    }

    @Override
    public void setFileText(String fileText) {
        notifyPropertyChangeListeners("RSA_FILE_TEXT", this.fileText, fileText);
        this.fileText = fileText;
    }

    @Override
    public void setFileColor(Color fileColor) {
        notifyPropertyChangeListeners("RSA_FILE_COLOR", this.fileColor, fileColor);
        this.fileColor = fileColor;
    }

    @Override
    public void setKeyText(String keyText) {
        notifyPropertyChangeListeners("RSA_KEY_TEXT", this.keyText, keyText);
        this.keyText = keyText;
    }

    @Override
    public void setKeyColor(Color keyColor) {
        notifyPropertyChangeListeners("RSA_KEY_COLOR", this.keyColor, keyColor);
        this.keyColor = keyColor;
    }

    @Override
    public void setMassageText(String massageText) {
        notifyPropertyChangeListeners("RSA_MESSAGE_TEXT", this.massageText, massageText);
        this.massageText = massageText;
    }

    @Override
    public void setMassageColor(Color massageColor) {
        notifyPropertyChangeListeners("RSA_MESSAGE_COLOR", this.massageColor, massageColor);
        this.massageColor = massageColor;
    }

    public static List<Integer> testCode() {
        final RSAViewModel vm = new RSAViewModel(null);

        vm.leftAreaFile  = new File(TEST_FOLDER_PATH);
        vm.processKeyGenEvent(new ActionEvent(new Object(), 0, "TEST"));

        vm.leftAreaFile  = new File(TEST_FILE_PATH);
        vm.rightAreaFile = new File(TEST_RSA_OPEN_KEY_FILE_PATH);

        return vm.code();
    }

    public static List<Integer> testDecode() {
        final RSAViewModel vm = new RSAViewModel(null);

        vm.leftAreaFile  = new File(TEST_RSA_ENCODED_FILE_PATH);
        vm.rightAreaFile = new File(TEST_RSA_CLOSED_KEY_FILE_PATH);

        return vm.deCode();
    }
}
