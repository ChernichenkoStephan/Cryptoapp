package com.encryption.ViewModel;

import com.encryption.Encryption.LSBScrambler;
import com.encryption.Model.BMPFileService;
import com.encryption.Model.CodeFileService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.encryption.Constants.*;

public class LSBViewModel extends CodeViewModel {

    private static final Logger log = Logger.getLogger(LSBViewModel.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    public LSBViewModel(PropertyChangeListener listener) {
        super(listener);
    }

    @Override
    protected void processEncodeEvent(ActionEvent e) {
        log.info(e.getActionCommand());
        if (leftAreaFile != null) {

            insert();

            setMassageText("Success");
            setMassageColor(Color.GREEN);
            delayResetMessage(2500, "Put new files");

        } else {
            setMassageText("Chose file to encode");
            setMassageColor(Color.red);
            delayResetMessage(4000, "Put new files");
        }
    }

    @Override
    protected void processDecodeEvent(ActionEvent e) {
        log.info(e.getActionCommand());
        if (rightAreaFile != null && leftAreaFile != null) {

            extract();

            setMassageText("Success");
            setMassageColor(Color.GREEN);
            delayResetMessage(4000, "Put new files");
        } else {
            setMassageText("Load file");
            setMassageColor(Color.RED);
            delayResetMessage(4000, "Put new files");
        }
    }

    /**
     * Reads data  from source file, reads image data, hides source file data to image,
     * writes new image data.
     * @return original data from source file
     */
    private List<Integer> insert() {
        final CodeFileService fs = new CodeFileService(leftAreaFile);
        final BMPFileService bfs = new BMPFileService(rightAreaFile);
        final ArrayList<Integer> dataToHide = new ArrayList<>(fs.read());
        dataToHide.add(255);

        final List<Integer> pixelData = bfs.read();
        final List<Integer> insertData = LSBScrambler.insert(dataToHide, pixelData);

        bfs.writeBites(insertData);

        dataToHide.remove(dataToHide.size()-1);
        return dataToHide;
    }

    /**
     * Reads image data, extracts hidden data, writes hidden data to destination file
     * @return hidden in image file pixels data
     */
    private List<Integer> extract() {
        final CodeFileService fs = new CodeFileService(leftAreaFile);
        final BMPFileService bfs = new BMPFileService(rightAreaFile);
        final List<Integer> pixelHiddenData = bfs.read();


        final List<Integer> extractedData = LSBScrambler.extract(pixelHiddenData);
        final int DATA_BOARDER = extractedData.indexOf(255);
        final List<Integer> res = extractedData.subList(0, DATA_BOARDER);

        fs.writeBites(res);
        return res;
    }

    @Override
    protected void resetState() {
        leftAreaFile = null;
        rightAreaFile = null;
        setMassageText("BMP message");
        setMassageColor(Color.GRAY);
        setKeyText("Image file");
        setKeyColor(Color.GRAY);
        setFileText("Data file");
        setFileColor(Color.GRAY);
    }

    @Override
    protected void processKeyAreaEvent(ActionEvent e) {
        log.info(e.getActionCommand());
        final String path = e.getActionCommand();
        final File keyFile = new File(path);

        if (checkKeyFile(keyFile)) {
            this.rightAreaFile = keyFile;
            setKeyText(path);
            setKeyColor(Color.green);
        } else {
            setMassageText("Wrong file type. Only \".bmp\" supported");
            setMassageColor(Color.red);
        }
    }

    protected void processFileAreaEvent(ActionEvent e) {
        log.info(e.getActionCommand());
        final String path = e.getActionCommand();
        final File objectFile = new File(path);

        if (checkObjectFile(objectFile)) {
            this.leftAreaFile = objectFile;
            setFileText(path);
            setFileColor(Color.green);
        } else {
            setMassageText("Folders not supported");
            setMassageColor(Color.red);
        }
    }

    protected boolean checkKeyFile(File keyFile) {
        return keyFile.getPath().contains(".bmp");
    }

    @Override
    public void setFileText(String fileText) {
        notifyPropertyChangeListeners("LSB_FILE_TEXT", this.fileText, fileText);
        this.fileText = fileText;
    }

    @Override
    public void setFileColor(Color fileColor) {
        notifyPropertyChangeListeners("LSB_FILE_COLOR", this.fileColor, fileColor);
        this.fileColor = fileColor;
    }

    @Override
    public void setKeyText(String keyText) {
        notifyPropertyChangeListeners("LSB_KEY_TEXT", this.keyText, keyText);
        this.keyText = keyText;
    }

    @Override
    public void setKeyColor(Color keyColor) {
        notifyPropertyChangeListeners("LSB_KEY_COLOR", this.keyColor, keyColor);
        this.keyColor = keyColor;
    }

    @Override
    public void setMassageText(String massageText) {
        notifyPropertyChangeListeners("LSB_MESSAGE_TEXT", this.massageText, massageText);
        this.massageText = massageText;
    }

    @Override
    public void setMassageColor(Color massageColor) {
        notifyPropertyChangeListeners("LSB_MESSAGE_COLOR", this.massageColor, massageColor);
        this.massageColor = massageColor;
    }

    public static List<Integer> testInsert() {
        final LSBViewModel vm = new LSBViewModel(null);

        vm.leftAreaFile  = new File(TEST_FILE_PATH);
        vm.rightAreaFile = new File(TEST_PICTURE_FILE_PATH);

        return vm.insert();
    }

    public static List<Integer> testExtract() {
        final LSBViewModel vm = new LSBViewModel(null);

        vm.leftAreaFile  = new File(TEST_FILE_PATH);
        vm.rightAreaFile = new File(TEST_PICTURE_FILE_PATH);

        return vm.extract();
    }
}