package ru.pshiblo.elgamal.core.utils;

import ru.pshiblo.elgamal.core.crypto.key.base.BigKey;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author Maxim Pshiblo
 */
public class MathUtils {

    /**
     * Тест Ферма
     */
    public static boolean isProbablyPrime(BigInteger number, int k) {
        if (number.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("!!!!");
        }
        if (number.compareTo(BigInteger.TWO) == 0) {
            return true;
        }
        for (int i = 0; i < k; i++) {
            BigInteger a = randBetween(BigInteger.TWO, number.subtract(BigInteger.TWO));
            if (gcd(a, number).compareTo(BigInteger.ONE) != 0) {
                return false;
            }
            if (modPow(a, number.subtract(BigInteger.ONE), number).compareTo(BigInteger.ONE) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Алгоритм Евклида - нахождение наибольшего общего делителя
     */
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        BigInteger bigA = new BigInteger(a.toByteArray());
        BigInteger bigB = new BigInteger(b.toByteArray());

        while (bigA.compareTo(BigInteger.ZERO) != 0 && bigB.compareTo(BigInteger.ZERO) != 0) {
            if (bigA.compareTo(bigB) > 0) {
                bigA = bigA.mod(bigB);
            } else {
                bigB = bigB.mod(bigA);
            }
        }
        return bigA.add(bigB);
    }

    /**
     * Схема "Справа налево" или бинарное умножение по модулю
     */
    public static BigInteger modPow(final BigInteger base, final BigInteger exp, final BigInteger m) {
        BigInteger c = BigInteger.ONE;
        BigInteger base_ = new BigInteger(base.toByteArray());
        BigInteger exp_ = new BigInteger(exp.toByteArray());
        while (!exp_.equals(BigInteger.ZERO)) {
            if (exp_.and(BigInteger.ONE).compareTo(BigInteger.ZERO) != 0) {
                c = c.multiply(base_).mod(m);
            }
            base_ = base_.multiply(base_).mod(m);
            exp_ = exp_.shiftRight(1);
        }
        return c;
    }

    public static BigInteger getPRoot(BigInteger p) {
        List<BigInteger> queue = new ArrayList<>();
        BigInteger phi = p.subtract(BigInteger.ONE);
        BigInteger n = new BigInteger(phi.toByteArray());
        for (BigInteger i = BigInteger.TWO; i.multiply(i).compareTo(n) >= 0; i = i.add(BigInteger.ONE)) {
            if (n.mod(i).compareTo(BigInteger.ZERO) == 0) {
                queue.add(i);
                while (n.mod(i).compareTo(BigInteger.ZERO) == 0) {
                    n = n.divide(i);
                }
            }
        }
        if (n.compareTo(BigInteger.ONE) > 0) {
            queue.add(n);
        }
        for (BigInteger res = BigInteger.TWO; res.compareTo(p) <= 0; res = res.add(BigInteger.ONE)) {
            boolean ok = true;
            for (int i = 0; i < queue.size() && ok; i++) {
                ok = modPow(res, phi.divide(queue.get(i)), p).compareTo(BigInteger.ONE) != 0;
            }
            if (ok) return res;
        }
        return BigInteger.ZERO;
    }

    public static BigInteger randBetween(String number1, String number2) {
        BigInteger num1 = new BigInteger(number1, 10);
        BigInteger num2 = new BigInteger(number2, 10);
        Random rnd = new Random();
        BigInteger result = new BigInteger(Math.max(num1.bitLength(), num2.bitLength()), rnd);
        result = result.mod(num2).add(num1);
        return result;
    }

    public static BigInteger randBetween(BigInteger num1, BigInteger num2) {
        Random rnd = new Random();
        BigInteger result = new BigInteger(Math.max(num1.bitLength(), num2.bitLength()), rnd);
        result = result.mod(num2).add(num1);
        return result;
    }
}
