package com.encryption.Model;

import java.util.Arrays;

public class BlowfishKey {
    private final int[][] S = new int[4][256];
    private final int[] P = new int[18];

    public int[][] getS() {
        return S;
    }

    public int[] getP() {
        return P;
    }

    public String toWritableString() {
        final StringBuilder keyString = new StringBuilder();

        for (int[] row: S)
            for (int i: row)
                keyString.append(i).append("#");

        for (int i: P) keyString.append(i).append("#");

        return keyString.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final BlowfishKey key = (BlowfishKey) o;
        if (!Arrays.equals(P, key.P))  return false;
        for (int i = 0; i < 4; i++)
            if (!Arrays.equals(S[i], key.S[i])) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(S);
        result = 31 * result + Arrays.hashCode(P);
        return result;
    }
}
