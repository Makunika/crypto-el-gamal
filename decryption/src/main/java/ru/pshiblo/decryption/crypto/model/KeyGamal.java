package ru.pshiblo.encryption.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.pshiblo.encryption.crypto.utils.CryptoUtils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Maxim Pshiblo
 */
@Getter
@ToString
public class KeyGamal {
    private int p;
    private int g;
    private int x;
    private int y;

    public KeyGamal(int p) {
        setP(p);
    }

    public void setP(int p) {
        if (!CryptoUtils.isPrime(p)) {
            throw new IllegalArgumentException("p req prime!");
        }
        if (p < 10) {
            throw new IllegalArgumentException("p req > 11!");
        }
        this.p = p;
        this.g = CryptoUtils.getPRoot(p);
        this.x = ThreadLocalRandom.current().nextInt(2, p);
        this.y = Math.floorMod(((int) Math.pow(g, x)), p);
    }
}
