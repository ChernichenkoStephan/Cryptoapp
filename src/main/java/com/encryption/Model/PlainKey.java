package com.encryption.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PlainKey {

    private final ConcurrentHashMap<Integer, Integer> pairs;

    public PlainKey(ConcurrentHashMap<Integer, Integer> pairs) {
        this.pairs = pairs;
    }

    public ConcurrentHashMap<Integer, Integer> getPairs() {
        return pairs;
    }

    public PlainKey getReverse() {
        ConcurrentHashMap<Integer, Integer> newPairs = new ConcurrentHashMap<>(256);
        pairs.forEach((k, v) -> newPairs.put(v, k));
        return new PlainKey(newPairs);
    }

    public List<Integer> toBytes() {
        ArrayList<Integer> res = new ArrayList<>(512);
        pairs.forEach((k, v) -> {res.add(k); res.add(v);});
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlainKey plainKey = (PlainKey) o;
        return Objects.equals(pairs, plainKey.pairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pairs);
    }
}
