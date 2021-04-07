package com.encryption.ViewModel;

import com.encryption.Encryption.PlainKeyGenerator;
import com.encryption.Encryption.PlainScrambler;
import com.encryption.Model.CodeFileService;
import com.encryption.Model.PlainKey;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.encryption.Constants.*;
import static com.encryption.Utils.*;

public class PlainViewModel extends CodeViewModel {

    private static final Logger log = Logger.getLogger(PlainViewModel.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    public PlainViewModel(PropertyChangeListener listener) {
        super(listener);
    }

    private final static int WRITE_DATA_SLIDE = 381;

    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("ID: " + e.getID());
        switch (e.getID()) {
            case 1001   -> processButton(e);
            case 2      -> processKeyAreaEvent(e);
            case 3      -> processFileAreaEvent(e);
            case 4,5,6  -> processSwitchAction(e);
            default     -> log.warning("Wrong actionEvent");
        }
    }

    @Override
    protected void processButton(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "ENCODE_ACTION"      -> processEncodeEvent(e);
            case "DECODE_ACTION"      -> processDecodeEvent(e);
            case "FREQUENCY_ACTION"   -> processFrequencyEvent(e);
            case "MENU_VIEW"          -> processMenuEvent(e);
            default -> log.warning("Wrong actionEvent");
        }
    }

    @Override
    protected void processSwitchAction(ActionEvent e) {
        switch (e.getID()) {
            case 4      -> state = "ENCODE";
            case 5      -> state = "DECODE";
            case 6      -> state = "FREQUENCY";
        }
        resetState();
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
        final List<Integer> rawData = fs.read();
        final String origExtension = fs.getExtension();

        rightAreaFile = fs.createKeyFile();
        final PlainKey key = PlainKeyGenerator.nextKey();

        final List<Integer> encryptedData = PlainScrambler.encrypt(rawData, key);

        fs.writeCoded(encryptedData);
        fs.changeExtension(".cringe");

        final CodeFileService kfs = new CodeFileService(rightAreaFile);
        kfs.write(origExtension+"#"+keyToWritableString(key));
        return rawData;
    }

    private static String keyToWritableString(PlainKey key) {
        final List<Integer> pairs = key.toBytes().stream()
                .map(integer -> integer += WRITE_DATA_SLIDE)
                .collect(Collectors.toList());

        final StringBuilder builder = new StringBuilder();
        pairs.forEach(integer -> builder.append(integer).append("#"));
        return builder.toString();
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

        final PlainKey key = parseKey(keyParams);
        final List<Integer> hiddenData = fs.readCoded();
        final List<Integer> decodedData = PlainScrambler.decrypt(hiddenData, key);

        fs.writeBites(decodedData);
        fs.changeExtension(origExtension);

        return decodedData;
    }

    private static PlainKey parseKey(String[] pairsString) {
        final ConcurrentHashMap<Integer, Integer> pairs = new ConcurrentHashMap<>();
        for (int i = 1; i < pairsString.length; i+=2) {
            final int left  = Integer.parseInt(pairsString[i+1]) - WRITE_DATA_SLIDE;
            final int right = Integer.parseInt(pairsString[i])   - WRITE_DATA_SLIDE;
            pairs.put(right, left);
        }
        return new PlainKey(pairs);
    }

    protected void processFrequencyEvent(ActionEvent e) {
        log.info(e.getActionCommand());

        if (leftAreaFile != null) {
            CodeFileService fs = new CodeFileService(leftAreaFile);

            final File freqFile = new File(leftAreaFile.getParent() + "/" + "frequencies" + ".txt");
            fs.createFile(freqFile);
            final Map<Integer, Integer> frequencies = getFrequencies(fs.read());

            final String frequenciesToWrite = printableFrequencies(frequencies);
            fs = new CodeFileService(freqFile);
            fs.write(frequenciesToWrite);

            setMassageText("Success");
            setMassageColor(Color.GREEN);
            delayResetMessage(2500, "Put new files");

        } else {
            setMassageText("Chose file to encode");
            setMassageColor(Color.red);
        }
    }

    @Override
    protected void resetState() {
        rightAreaFile = null;
        leftAreaFile = null;
        setMassageText("message");
        setMassageColor(Color.GRAY);
        setKeyText("Key file");
        setKeyColor(Color.GRAY);
        setFileText("Object file");
        setFileColor(Color.GRAY);
    }

    @Override
    public void setFileText(String fileText) {
        notifyPropertyChangeListeners("SIMPLE_FILE_TEXT", this.fileText, fileText);
        this.fileText = fileText;
    }

    @Override
    public void setFileColor(Color fileColor) {
        notifyPropertyChangeListeners("SIMPLE_FILE_COLOR", this.fileColor, fileColor);
        this.fileColor = fileColor;
    }

    @Override
    public void setKeyText(String keyText) {
        notifyPropertyChangeListeners("SIMPLE_KEY_TEXT", this.keyText, keyText);
        this.keyText = keyText;
    }

    @Override
    public void setKeyColor(Color keyColor) {
        notifyPropertyChangeListeners("SIMPLE_KEY_COLOR", this.keyColor, keyColor);
        this.keyColor = keyColor;
    }

    @Override
    public void setMassageText(String massageText) {
        notifyPropertyChangeListeners("SIMPLE_MESSAGE_TEXT", this.massageText, massageText);
        this.massageText = massageText;
    }

    @Override
    public void setMassageColor(Color massageColor) {
        notifyPropertyChangeListeners("SIMPLE_MESSAGE_COLOR", this.massageColor, massageColor);
        this.massageColor = massageColor;
    }

    public static List<Integer> testCode() {
        final PlainViewModel vm =  new PlainViewModel(null);
        vm.leftAreaFile   = new File(TEST_FILE_PATH);
        return vm.code();
    }

    public static List<Integer> testDecode() {
        final PlainViewModel vm =  new PlainViewModel(null);
        vm.leftAreaFile   = new File(TEST_ENCODED_FILE_PATH);
        vm.rightAreaFile  = new File(TEST_KEY_FILE_PATH);
        return vm.deCode();
    }
}


