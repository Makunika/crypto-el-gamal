package ru.pshiblo.elgamal.core.crypto.key.base;

import ru.pshiblo.elgamal.core.utils.MathUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Random;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

/**
 * @author Maxim Pshiblo
 */
public class BigKey {
    private byte[] bytes;
    private int bitsSize;
    private int byteSize;

    public BigKey(BigInteger bigInteger) {
        bytes = bigInteger.toByteArray();
    }

    public BigKey(int bitsCount, boolean isPrime) {
        this.bitsSize = bitsCount;
        this.byteSize = bitsCount / 8;
        bytes = new byte[bitsCount / 8];
        Random rnd = new Random();
        if (isPrime) {
            BigInteger number;
            do {
                rnd.nextBytes(bytes);
                if (bytes[bytes.length - 1] % 2 == 0) {
                    bytes[bytes.length - 1] = (byte)((int) bytes[bytes.length - 1] + 1);
                }
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = (byte) (bytes[i] & 0xFF);
                }
                //bytes[0] = (byte) ((int) bytes[0] | 1 << 8);
                number = new BigInteger(bytes).abs();
            } while (!MathUtils.isProbablyPrime(number, 100));
            bytes = number.toByteArray();
        } else {
            rnd.nextBytes(bytes);
        }
    }

    public BigInteger toBigInteger() {
        return new BigInteger(bytes);
    }

    @Override
    public String toString() {
        return toBigInteger().toString(16);
    }

    public int getBitsSize() {
        return bitsSize;
    }

    public int getByteSize() {
        return byteSize;
    }
}
