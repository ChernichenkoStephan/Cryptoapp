package com.encryption.Encryption;

import com.encryption.Model.RSAKey;

import java.util.Arrays;

import static com.encryption.Utils.gcd;

public class RSAKeyGenerator {

    public static RSAKey nextKey() {
        long p = 0, q = 0, n = 0;
        PrimePair pair = new PrimePair(100);

        for (int i = 0; i < 5000; i++) {
            p = pair.getLeft();
            q = pair.getRight();
            n = p * q;
            if (n > 255) break;
            pair.nextPair();
        }

        final long fn = (p - 1) * (q - 1);
        final long e = getExp(fn, (int) n);
        final long d = getPrivateKey(e, fn);

        return d == -1  ? nextKey() : new RSAKey(e, d, n);
    }

    private static int getPrivateKey(long e, long fn) {
        for (int k = 1; k < 100_000; k++) {
            final double d =  (k * fn + 1) / (double) e;
            if (d - (int) d == 0) return (int) d;
        }
        return -1;
    }

    private static long getExp(long fn, int boarder) {
        for (int i = 2; i < boarder; i++)
            if (gcd(fn, i) == 1) return i;
        return 0;
    }

    private static class PrimePair {
        boolean[] primes;
        private long left;
        private long right;

        public PrimePair() {
            primes = new boolean[50];
            fillSieve();
            nextPair();
        }

        PrimePair(int primeRangeBorder) {
            primes = new boolean[primeRangeBorder];
            fillSieve();
            nextPair();
        }

        public void fillSieve() {
            Arrays.fill(primes, true);
            primes[0] = false;
            primes[1] = false;
            for (int i = 2; i < primes.length; ++i) {
                if (primes[i]) {
                    for (int j = 2; i * j < primes.length; ++j) {
                        primes[i * j] = false;
                    }
                }
            }
        }

        public long getLeft() {
            return left;
        }

        public long getRight() {
            return right;
        }

        public void nextPair() {
            left  = getRandomPrime();
            right = getRandomPrime();
            while (left == right) right = getRandomPrime();
        }

        private long getRandomPrime() {
            int index =  20 + (int) (Math.random() * primes.length - 20);
            while (!primes[index] && index > 1) index--;
            return index;
        }
    }

}

