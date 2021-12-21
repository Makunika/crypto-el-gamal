package ru.pshiblo.elgamal.core.crypto.key;

import ru.pshiblo.elgamal.core.crypto.key.base.BigKey;
import ru.pshiblo.elgamal.core.utils.MathUtils;

import java.math.BigInteger;

/**
 * @author Maxim Pshiblo
 */
public class GeneratorKey {

    private static GeneratorKey instance = new GeneratorKey();

    public static GeneratorKey getInstance() {
        return instance;
    }

    private GeneratorKey() {}

    public SecretKey generateNewKey(int bitsCount) {
        if (bitsCount < 32) {
            throw new IllegalArgumentException("bitsCount >= 32!");
        }
        BigKey p = new BigKey(bitsCount, true);
        BigKey g = new BigKey(MathUtils.getPRoot(p.toBigInteger()));
        BigKey x = new BigKey(MathUtils.randBetween(BigInteger.TWO, p.toBigInteger().subtract(BigInteger.ONE)));
        BigKey y = new BigKey(MathUtils.modPow(g.toBigInteger(), x.toBigInteger(), p.toBigInteger()));

        return new SecretKey(p, g, y, x);
    }
}
