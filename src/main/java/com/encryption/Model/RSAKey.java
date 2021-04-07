package com.encryption.Model;

import java.io.Serializable;
import java.util.Objects;

public class RSAKey implements Serializable {
    private final long e;
    private long d;
    private final long mod;

    public RSAKey(long e, long d, long mod) {
        this.e = e;
        this.d = d;
        this.mod = mod;
    }

    public RSAKey(long e, long mod) {
        this.e = e;
        this.mod = mod;
    }

    public String toOpenString() {
        return e + "#" + mod + "#";
    }

    public String toClosedString() {
        return e + "#" + d + "#" + mod + "#";
    }

    public long getE() {
        return e;
    }

    public long getD() {
        return d;
    }

    public long getMod() {
        return mod;
    }

    @Override
    public String toString() {
        return "RSAKey{" +
                "open=" + e +
                ", closed=" + d +
                ", mod=" + mod +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RSAKey rsaKey = (RSAKey) o;
        return Objects.equals(e, rsaKey.e) && Objects.equals(d, rsaKey.d) && Objects.equals(mod, rsaKey.mod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e, d, mod);
    }
}
