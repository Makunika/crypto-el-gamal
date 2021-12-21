package ru.pshiblo.elgamal.core.crypto.key;

import ru.pshiblo.elgamal.core.crypto.key.base.BigKey;

/**
 * @author Maxim Pshiblo
 */
public class SecretKey {
    private BigKey p;
    private BigKey g;
    private BigKey y;
    private BigKey x;

    public SecretKey(BigKey p, BigKey g, BigKey y, BigKey x) {
        this.p = p;
        this.g = g;
        this.y = y;
        this.x = x;
    }

    public OpenKey toOpenKey() {
        return new OpenKey(p, g, y);
    }

    public BigKey getP() {
        return p;
    }

    public void setP(BigKey p) {
        this.p = p;
    }

    public BigKey getG() {
        return g;
    }

    public void setG(BigKey g) {
        this.g = g;
    }

    public BigKey getY() {
        return y;
    }

    public void setY(BigKey y) {
        this.y = y;
    }

    public BigKey getX() {
        return x;
    }

    public void setX(BigKey x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "SecretKey{" +
                "p=" + p +
                ", g=" + g +
                ", y=" + y +
                ", x=" + x +
                '}';
    }
}
