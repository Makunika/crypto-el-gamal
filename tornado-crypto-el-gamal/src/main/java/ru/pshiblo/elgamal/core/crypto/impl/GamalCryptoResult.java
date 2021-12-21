package ru.pshiblo.elgamal.core.crypto.impl;

/**
 * @author Maxim Pshiblo
 */
public class GamalCryptoResult {
    private String a;
    private String b;
    private int bytes;

    public GamalCryptoResult(String a, String b, int bytes) {
        this.a = a;
        this.b = b;
        this.bytes = bytes;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "GamalCryptoResult{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", bytes=" + bytes +
                '}';
    }
}
