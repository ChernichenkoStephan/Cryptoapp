package com.encryption.Encryption;

import com.encryption.Model.RSAKey;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.encryption.Utils.modPow;

public class RSAScrambler {

    private static final Logger log = Logger.getLogger(RSAScrambler.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    /**
     * Mapping all data with C = (M^e) mod n function
     * Uses open variant of RSAKey (d - not needed)
     */
    public static List<Integer> encrypt(final List<Integer> data, final RSAKey key) {
        long e = key.getE();
        long m = key.getMod();
        return data.stream().map(c ->  (int) modPow(c, e, m)).collect(Collectors.toList());
    }

    /**
     * Mapping all data with M = (C^d) mod n function
     * Uses closed variant of RSAKey (all data stored)
     */
    public static List<Integer> decrypt(final List<Integer> data, final RSAKey key) {
        long d = key.getD();
        long m = key.getMod();
        return data.stream().map(c -> (int) modPow(c, d, m)).collect(Collectors.toList());
    }


}
