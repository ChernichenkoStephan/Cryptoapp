package com.encryption.Encryption;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LSBScrambler {

    private static final Logger log = Logger.getLogger(LSBScrambler.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    /**
     * Hides data from List<Integer> data in two last not significant bits
     * @param data data to hide in image pixels
     * @param pixelData pixel colour data
     */
    public static List<Integer> insert(final List<Integer> data, final List<Integer> pixelData) throws IllegalArgumentException {
        if ((pixelData.size() / 4) < data.size()) throw new IllegalArgumentException("Data to long");
        ArrayList<Integer> bytes = new ArrayList<>(pixelData.size()/4);

        for (int i = 0; i < data.size(); i++) {
            int byteToHide = data.get(i);

            int blue  = pixelData.get(i) & 0xFC;
            blue |= ((byteToHide >> 6) & 0x3);

            int red   = pixelData.get(i+1) & 0xFC;
            red |= ((byteToHide >> 4) & 0x3);

            int green = pixelData.get(i+2) & 0xFC;
            green |= ((byteToHide >> 2) & 0x3);

            int alpha = pixelData.get(i+3) & 0xFC;
            alpha |= ((byteToHide) & 0x3);

            bytes.add(blue);
            bytes.add(red);
            bytes.add(green);
            bytes.add(alpha);
        }

        bytes.addAll(pixelData.subList(bytes.size(), pixelData.size()));
        return bytes;
    }

    /**
     * Extract data from pixels
     * 1 byte hides in 4 pixels last 2 bits
     * @param pixelData pixel colour with hidden bits data
     */
    public static List<Integer> extract(final List<Integer> pixelData) throws IllegalArgumentException {
        ArrayList<Integer> bytes = new ArrayList<>(pixelData.size()/4);
        for (int i = 0; i < pixelData.size(); i+=4) {
            int hiddenByte = 0;
            hiddenByte |= ((pixelData.get(i)   & 0x3) << 6);
            hiddenByte |= ((pixelData.get(i+1) & 0x3) << 4);
            hiddenByte |= ((pixelData.get(i+2) & 0x3) << 2);
            hiddenByte |= ((pixelData.get(i+3) & 0x3));
            bytes.add(hiddenByte);
        }
        return bytes;
    }

}
