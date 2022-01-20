package ru.pshiblo.elgamal.core.utils;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class MathUtilsTest {

    @Test
    public void testIsProbablyPrime() {
        BigInteger primeNumber = BigInteger.valueOf(125125199);
        boolean probablyPrime = MathUtils.isProbablyPrime(primeNumber, primeNumber.bitLength());
        assertTrue(probablyPrime);

        BigInteger notPrimeNumber = BigInteger.valueOf(125125125);
        probablyPrime = MathUtils.isProbablyPrime(notPrimeNumber, notPrimeNumber.bitLength());
        assertFalse(probablyPrime);
    }

    @Test
    public void testIsProbablyPrimeFerma() {
        BigInteger primeNumber = BigInteger.valueOf(125125199);
        boolean probablyPrime = MathUtils.isProbablyPrimeFerma(primeNumber, primeNumber.bitLength());
        assertTrue(probablyPrime);

        BigInteger notPrimeNumber = BigInteger.valueOf(125125125);
        probablyPrime = MathUtils.isProbablyPrimeFerma(notPrimeNumber, notPrimeNumber.bitLength());
        assertFalse(probablyPrime);
    }

    @Test
    public void testGcd() {
        BigInteger primeNumber = BigInteger.valueOf(125125199);
        assertEquals(MathUtils.gcd(primeNumber, BigInteger.valueOf(123313131)), BigInteger.ONE);
        assertEquals(MathUtils.gcd(BigInteger.valueOf(30), BigInteger.valueOf(100)), BigInteger.valueOf(10));
    }

    @Test
    public void testModPow() {
        BigInteger base = new BigInteger("123124918273");
        BigInteger exp = new BigInteger("12312412311");
        BigInteger m = new BigInteger("123912893912839821989");
        assertEquals(base.modPow(exp, m), MathUtils.modPow(base, exp, m));
    }

    @Test
    public void testRandBetween() {
        BigInteger rndNumber = MathUtils.randBetween(BigInteger.ONE, BigInteger.TEN);
        assertTrue(rndNumber.compareTo(BigInteger.ONE) > 0);
        assertTrue(rndNumber.compareTo(BigInteger.TEN) < 0);

        assertThrows(IllegalArgumentException.class, () -> MathUtils.randBetween(BigInteger.ONE, BigInteger.ONE));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.randBetween(BigInteger.TEN, BigInteger.ONE));
    }
}