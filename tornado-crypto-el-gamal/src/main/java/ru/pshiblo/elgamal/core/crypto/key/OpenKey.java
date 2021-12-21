package ru.pshiblo.elgamal.core.crypto.key;

import ru.pshiblo.elgamal.core.crypto.key.base.BigKey;

/**
 * @author Maxim Pshiblo
 */
public class OpenKey {
    private BigKey p;
    private BigKey g;
    private BigKey y;

    public OpenKey(BigKey p, BigKey g, BigKey y) {
        this.p = p;
        this.g = g;
        this.y = y;
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
}
