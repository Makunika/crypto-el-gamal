package ru.pshiblo.elgamal.core.crypto.service.impl;

import ru.pshiblo.elgamal.core.crypto.service.DecryptorService;
import ru.pshiblo.elgamal.core.crypto.key.SecretKey;
import ru.pshiblo.elgamal.core.crypto.service.GamalCryptoResult;
import ru.pshiblo.elgamal.core.utils.MathUtils;
import ru.pshiblo.elgamal.core.utils.Utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import static ru.pshiblo.elgamal.core.crypto.service.impl.CryptorServiceImpl.MIN_BLOCK;

/**
 * @author Maxim Pshiblo
 */
public class DecryptorServiceImpl implements DecryptorService {

    @Override
    public String decryption(GamalCryptoResult result, SecretKey secretKey) {
        if (result == null) {
            throw new IllegalArgumentException("result is null");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("secretKey is null");
        }
        int bytesSize = result.getBytes();
        String[] numbersA = result.getA().split(",");
        String[] numbersB = result.getB().split(",");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numbersA.length; i++) {
            BigInteger numberA = new BigInteger(numbersA[i], 16);
            BigInteger numberB = new BigInteger(numbersB[i], 16);

            BigInteger res = numberB.multiply(
                    MathUtils.modPow(
                            numberA,
                            secretKey.getP().toBigInteger()
                                    .subtract(BigInteger.ONE)
                                    .subtract(secretKey.getX().toBigInteger()),
                            secretKey.getP().toBigInteger())).mod(secretKey.getP().toBigInteger());
            byte[] bytes = res.toByteArray();
            Utils.reverseArray(bytes);
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            for (int j = 0; j < bytesSize / MIN_BLOCK; j++) {
                if (bytes.length / MIN_BLOCK <= j) {
                    break;
                }
                char aChar = byteBuffer.getChar();
                sb.append(aChar);
            }
        }

        return sb.toString();
    }
}
