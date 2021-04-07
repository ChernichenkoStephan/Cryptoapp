package com.encryption.Encryption;

import com.encryption.Model.PlainKey;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PlainScrambler {

    private static final Logger log = Logger.getLogger(PlainScrambler.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    /**
     * Wrapper of code(final List<Integer>, final PlainKey) method.
     * Ensures that key is in correct form for encryption.
     */
    public static List<Integer> encrypt(final List<Integer> data, final PlainKey key) {
        if (key.getPairs().size() != 256) return new ArrayList<>();
        if (!key.getPairs().containsKey(0))
            return code(data, key.getReverse());
        return code(data, key);
    }

    /**
     * Wrapper of code(final List<Integer>, final PlainKey) method.
     * Ensures that key is in correct form for decryption.
     */
    public static List<Integer> decrypt(final List<Integer> data, final PlainKey key) {
        if (key.getPairs().size() != 256) return new ArrayList<>();
        if (key.getPairs().containsKey(0) || key.getPairs().size() != 256)
            return code(data, key.getReverse());
        return code(data, key);
    }

    /**
     * Encryption method of plain replacement of each byte for an arbitrary disk file.
     * The key is the size of about 256
     * It's pretty safe, all absent bytes
     */
    private static List<Integer> code(final List<Integer> data, final PlainKey key) {
        return data.stream().map(b -> key.getPairs().getOrDefault(b, b)).collect(Collectors.toList());
    }
}
