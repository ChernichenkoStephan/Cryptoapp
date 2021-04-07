package com.encryption.Model;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BMPFileService extends CodeFileService {

    private static final Logger log = Logger.getLogger(BMPFileService.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    private BMPImage img;

    public BMPFileService(String path) throws IllegalArgumentException {
        super(path);
        this.file = new File(path);
        img = readImage(file);
    }

    public BMPFileService(File file) throws IllegalArgumentException {
        super(file);
        img = readImage(file);
        this.file = file;
    }

    @Override
    public List<Integer> read() {
        ArrayList<Integer> list = new ArrayList<>(img.pixelData.size());
        img.pixelData.forEach(b -> list.add(b.intValue()));
        return list;
    }

    @Override
    public void writeBites(List<Integer> data) throws IllegalArgumentException {
        if (data.size() != img.pixelData.size()) throw new IllegalArgumentException("Data from wrong file");

        try (OutputStream os = new FileOutputStream(file)) {
            final ArrayList<Integer> bytes = new ArrayList<>(img.headerData.size() + data.size());

            img.headerData.forEach(b -> bytes.add(b.intValue()));
            bytes.addAll(data);

            final byte[] array = toByteArray(bytes);
            os.write(array);
        } catch (IOException e) {
            log.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < img.pixelData.size(); i++)
            img.pixelData.set(i, img.pixelData.get(i) & 0xFC);
        writeBites(img.pixelData);
    }


    private BMPImage readImage(File file) throws IllegalArgumentException {
        BMPImage res = new BMPImage(0,0, new LinkedList(), new LinkedList());
        final ArrayList<Integer> pixelData = new ArrayList<>(1_000_000);
        final ArrayList<Integer> headerData = new ArrayList<>(50);
        try (InputStream is = new FileInputStream(file)) {
            final byte[] data = is.readAllBytes();

            final byte[] bFileSize = new byte[] {data[2], data[3], data[4], data[5]};
            final int fileSize = ByteBuffer.wrap(bFileSize).order(ByteOrder.LITTLE_ENDIAN).getInt();

            final byte[] bDataOffset = new byte[] {data[10], data[11], data[12], data[13]};
            final int offset = ByteBuffer.wrap(bDataOffset).order(ByteOrder.LITTLE_ENDIAN).getInt();

            for (int i = 0; i < offset; i++) headerData.add((int) data[i]);
            for (int i = offset; i < data.length; i++) pixelData.add((int) data[i]);

            res = new BMPImage(offset, fileSize, headerData, pixelData);
        } catch (IOException e) {
            log.warning(Arrays.toString(e.getStackTrace()));
            throw new IllegalArgumentException("Wrong file or not found");
        }
        return res;
    }

    private byte[] toByteArray(List<Integer> list) {
        final byte[] array = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) array[i] = list.get(i).byteValue();
        return array;
    }


}

