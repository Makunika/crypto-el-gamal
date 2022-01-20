package ru.pshiblo.elgamal.core.crypto.key;

import ru.pshiblo.elgamal.core.crypto.key.base.BigKey;
import ru.pshiblo.elgamal.core.utils.MathUtils;

import java.math.BigInteger;

/**
 * @author Maxim Pshiblo
 */
public class GeneratorKey {
    public SecretKey generateNewKey(int bitsCount) {
        if (bitsCount < 32) {
            throw new IllegalArgumentException("bitsCount >= 32!");
        }
        System.out.println("Generate p start");
        BigKey p = new BigKey(bitsCount, true);
        System.out.println("Generate g start");
        //BigKey g = new BigKey(MathUtils.getPRoot(p.toBigInteger()));
        BigKey g = new BigKey(MathUtils.randBetween(BigInteger.TWO, p.toBigInteger().subtract(BigInteger.ONE)));
        System.out.println("Generate x start");
        BigKey x = new BigKey(MathUtils.randBetween(BigInteger.TWO, p.toBigInteger().subtract(BigInteger.ONE)));
        System.out.println("Generate y start");
        BigKey y = new BigKey(MathUtils.modPow(g.toBigInteger(), x.toBigInteger(), p.toBigInteger()));

        System.out.println("p = " + p);
        System.out.println("g = " + g);
        System.out.println("x = " + x);
        System.out.println("y = " + y);

        return new SecretKey(p, g, y, x);
    }
}
