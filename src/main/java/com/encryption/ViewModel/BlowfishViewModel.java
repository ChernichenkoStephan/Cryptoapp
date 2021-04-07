package com.encryption.ViewModel;

import com.encryption.Encryption.BlowfishScrambler;
import com.encryption.Model.BlowfishKey;
import com.encryption.Model.CodeFileService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.encryption.Constants.*;

import static com.encryption.Utils.toIntegerList;

public class BlowfishViewModel extends CodeViewModel {

    private static final Logger log = Logger.getLogger(BlowfishViewModel.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    public BlowfishViewModel(PropertyChangeListener listener) {
        super(listener);
    }

    @Override
    protected void processEncodeEvent(ActionEvent e) {
        log.info(e.getActionCommand());

        if (leftAreaFile == null) {
            setMassageText("Chose file to encode");
            setMassageColor(Color.red);
            delayResetMessage(5000, "Put new files");
            return;
        }
        if (!leftAreaFile.getPath().contains(".")) {
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

    /**
     * Creates key file, encrypts source file, changes source file extension
     * @return original data from source file
     */
    private List<Integer> code() {
        final CodeFileService fs = new CodeFileService(leftAreaFile);
        final String origExtension = fs.getExtension();
        final List<Byte> temp = fs.readBytes();
        final List<Integer> dataToHide = toIntegerList(temp);

        rightAreaFile = fs.createKeyFile();
        final CodeFileService kfs = new CodeFileService(rightAreaFile);

        final BlowfishKey key = BlowfishScrambler.nextKey("VERY_COMPLICATED_SEED".getBytes());
        final List<Integer> encData = BlowfishScrambler.encrypt(dataToHide, key);

        fs.writeBites(encData);
        fs.changeExtension(".cringe");
        kfs.write(origExtension+"#"+key.toWritableString());

        return dataToHide;
    }

    /**
     * Reads key file, decrypts source file, changes source file extension to original
     * @return Decrypted data from encrypted file
     */
    private List<Integer> deCode() {
        final CodeFileService fs = new CodeFileService(leftAreaFile);
        final CodeFileService kfs = new CodeFileService(rightAreaFile);

        final String[] keyParams = kfs.readString().split("#");
        final String origExtension = keyParams[0];

        final List<Byte> temp = fs.readBytes();
        final List<Integer> hiddenData = toIntegerList(temp);
        final BlowfishKey key = parseKey(keyParams);
        final List<Integer> decodedData = BlowfishScrambler.decrypt(hiddenData, key);

        fs.writeBites(decodedData);
        fs.changeExtension(origExtension);
        return decodedData;
    }

    private BlowfishKey parseKey(String[] keyParams) {
        final BlowfishKey key = new BlowfishKey();
        final int S_BORDER = 1 + 4 * 256;
        final int P_BORDER = S_BORDER + 18;

        for (int i = 0; i < 4; i++)
            for (int j = 0, k = 1; j < 256; j++, k++)
                key.getS()[i][j] = Integer.parseInt(keyParams[i*256 + k]);

        for (int i = S_BORDER, j = 0; i < P_BORDER; i++, j++)
            key.getP()[j] = Integer.parseInt(keyParams[i]);

        return key;
    }

    @Override
    protected void resetState() {
        setMassageText("Blowfish message");
        setMassageColor(Color.GRAY);
        setKeyText("Key file");
        setKeyColor(Color.GRAY);
        setFileText("Object file");
        setFileColor(Color.GRAY);
    }

    @Override
    public void setFileText(String fileText) {
        notifyPropertyChangeListeners("BLOWFISH_FILE_TEXT", this.fileText, fileText);
        this.fileText = fileText;
    }

    @Override
    public void setFileColor(Color fileColor) {
        notifyPropertyChangeListeners("BLOWFISH_FILE_COLOR", this.fileColor, fileColor);
        this.fileColor = fileColor;
    }

    @Override
    public void setKeyText(String keyText) {
        notifyPropertyChangeListeners("BLOWFISH_KEY_TEXT", this.keyText, keyText);
        this.keyText = keyText;
    }

    @Override
    public void setKeyColor(Color keyColor) {
        notifyPropertyChangeListeners("BLOWFISH_KEY_COLOR", this.keyColor, keyColor);
        this.keyColor = keyColor;
    }

    @Override
    public void setMassageText(String massageText) {
        notifyPropertyChangeListeners("BLOWFISH_MESSAGE_TEXT", this.massageText, massageText);
        this.massageText = massageText;
    }

    @Override
    public void setMassageColor(Color massageColor) {
        notifyPropertyChangeListeners("BLOWFISH_MESSAGE_COLOR", this.massageColor, massageColor);
        this.massageColor = massageColor;
    }

    public static List<Integer> testCode() {
        final BlowfishViewModel vm = new BlowfishViewModel(null);
        vm.leftAreaFile   = new File(TEST_FILE_PATH);
        return vm.code();
    }

    public static List<Integer> testDecode() {
        final BlowfishViewModel vm = new BlowfishViewModel(null);
        vm.leftAreaFile   = new File(TEST_ENCODED_FILE_PATH);
        vm.rightAreaFile  = new File(TEST_KEY_FILE_PATH);
        return vm.deCode();
    }

}
