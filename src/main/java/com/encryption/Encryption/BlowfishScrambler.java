package com.encryption.Encryption;

import com.encryption.Model.BlowfishKey;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.encryption.Utils.*;

public class BlowfishScrambler {

    private static final Logger log = Logger.getLogger(BlowfishScrambler.class.getName());
    public static void setLoggerLevel(Level level) {log.setLevel(level);}

    /**
     * Wrapper for decrypt(final byte[], final BlowfishKey) function.
     * Gives better interface for data manipulation
     */
    public static List<Integer> encrypt(final List<Integer> data, final BlowfishKey key) {
        // Data convertation
        byte[] temp = toByteArray(data);
        // Align control
        temp = alignTo8(temp, Byte.MAX_VALUE);

        BlowfishScrambler.encrypt(temp, key);

        return toIntegerList(temp);
    }

    /**
     * Wrapper for encrypt(final byte[], final BlowfishKey) function.
     * Gives better interface for data manipulation
     */
    public static List<Integer> decrypt(final List<Integer> data, final BlowfishKey key) {
        // Data convertation
        byte[] temp = toByteArray(data);

        BlowfishScrambler.decrypt(temp, key);

        // Clearness control
        ArrayList<Integer> res = toIntegerList(temp);
        res.removeIf(integer -> integer == (int) Byte.MAX_VALUE);
        return res;
    }

    /**
     * Encrypting an array of bytes
     * @param data Data (length is a multiple of 8)
     */
    private static void encrypt(final byte[] data, final BlowfishKey key) {
        int blocks = data.length >> 3;
        for (int k = 0, p; k < blocks; k++) {
            p = k << 3;
            int Xl = clenchTo32b(data, p);
            int Xr = clenchTo32b(data, p + 4);
            int tmp;
            for (int i = 17; i > 1; i--) {
                Xl = Xl ^ key.getP()[i];
                Xr = F(Xl, key) ^ Xr;
                tmp = Xl;
                Xl = Xr;
                Xr = tmp;
            }
            tmp = Xl;
            Xl = Xr;
            Xr = tmp;
            Xr ^= key.getP()[1];
            Xl ^= key.getP()[0];
            unclenchFrom32b(Xl, data, p);
            unclenchFrom32b(Xr, data, p + 4);
        }
    }

    /**
     * Decrypting a byte array
     * @param data Data (length is a multiple of 8)
     */
    private static void decrypt(final byte[] data, final BlowfishKey key) {
        int blocks = data.length >> 3;
        for (int k = 0, p; k < blocks; k++) {
            p = k << 3;
            int Xl = clenchTo32b(data, p);
            int Xr = clenchTo32b(data, p + 4);
            int tmp;
            for (int i = 0; i < 16; i++) {
                Xl = Xl ^ key.getP()[i];
                Xr = F(Xl, key) ^ Xr;
                tmp = Xl;
                Xl = Xr;
                Xr = tmp;
            }
            tmp = Xl;
            Xl = Xr;
            Xr = tmp;
            Xr ^= key.getP()[16];
            Xl ^= key.getP()[17];
            unclenchFrom32b(Xl, data, p);
            unclenchFrom32b(Xr, data, p + 4);
        }
    }

    private static int F(int x, BlowfishKey key) {
        int a, b, c, d;
        d = x & 0xFF;
        x >>= 8;
        c = x & 0xFF;
        x >>= 8;
        b = x & 0xFF;
        x >>= 8;
        a = x & 0xFF;
        int y = key.getS()[0][a] + key.getS()[1][b];
        y ^= key.getS()[2][c];
        y += key.getS()[3][d];
        return y;
    }

    public static BlowfishKey nextKey(byte[] seed) {
        BlowfishKey key = new BlowfishKey();
        // Initializing P array
        System.arraycopy(honesties4, 0, key.getP(), 0, 18);
        // Initializing S arrays
        System.arraycopy(honesties0, 0, key.getS()[0], 0, 256);
        System.arraycopy(honesties1, 0, key.getS()[1], 0, 256);
        System.arraycopy(honesties2, 0, key.getS()[2], 0, 256);
        System.arraycopy(honesties3, 0, key.getS()[3], 0, 256);
        int data;
        int j = 0, i;
        for (i = 0; i < 18; ++i) {
            data = 0x00000000;
            for (int k = 0; k < 4; ++k) {
                data = (data << 8) | (seed[j] & 0xFF);
                j++;
                if (j >= seed.length) j = 0;
            }
            key.getP()[i] ^= data;
        }
        byte[] b = new byte[8];
        for (i = 0; i < 18; i += 2) {
            decrypt(b, key);
            key.getP()[i] = clenchTo32b(b, 0);
            key.getP()[i + 1] = clenchTo32b(b, 4);
        }
        for (i = 0; i < 4; ++i) {
            for (j = 0; j < 256; j += 2) {
                decrypt(b, key);
                key.getS()[i][j] = clenchTo32b(b, 0);
                key.getS()[i][j + 1] = clenchTo32b(b, 4);
            }
        }
        return key;
    }
}
