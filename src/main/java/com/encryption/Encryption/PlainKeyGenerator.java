package com.encryption.Encryption;

import com.encryption.Model.PlainKey;

import java.util.concurrent.ConcurrentHashMap;

public class PlainKeyGenerator {
    public static PlainKey nextKey() {
        ConcurrentHashMap<Integer, Integer> pairs = new ConcurrentHashMap<>(256);
        for (int left = 0, right; left < 256; left++) {
            right = (int) (left * 10_000 + Math.random() * 10_000 * (left + 1));
            pairs.put(left, right);
        }
        return new PlainKey(pairs);
    }
}
