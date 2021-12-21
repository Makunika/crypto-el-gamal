package ru.pshiblo.elgamal.core.crypto.impl;

import ru.pshiblo.elgamal.core.crypto.Cryptor;
import ru.pshiblo.elgamal.core.crypto.key.OpenKey;
import ru.pshiblo.elgamal.core.utils.MathUtils;
import ru.pshiblo.elgamal.core.utils.Utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Pshiblo
 */
public class CryptorImpl implements Cryptor {

    public static int MIN_BLOCK = 2;

    @Override
    public GamalCryptoResult encryption(String str, OpenKey openKey, int byteBlock) {
        if (openKey == null) {
            throw new IllegalArgumentException("openKey is null");
        }
        if (byteBlock >= openKey.getP().getByteSize()) {
            throw new IllegalArgumentException("byteBlock > p byteSize");
        }

        int[] chars = str.chars().toArray();
        List<String> resultA = new ArrayList<>();
        List<String> resultB = new ArrayList<>();

        for (int i = 0; i < chars.length; i+=byteBlock/MIN_BLOCK) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(Math.min(chars.length * MIN_BLOCK, byteBlock));
            for (int j = i; j < i+byteBlock/MIN_BLOCK; j++) {
                if (j >= chars.length)
                    break;
                else
                    byteBuffer.putChar((char) chars[j]);
            }
            byte[] array = byteBuffer.array();
            Utils.reverseArray(array);
            BigInteger number = new BigInteger(array);
            BigInteger sessionKey = MathUtils.randBetween(BigInteger.TWO, openKey.getP().toBigInteger().subtract(BigInteger.TWO));
            BigInteger a = MathUtils.modPow(openKey.getG().toBigInteger(), sessionKey, openKey.getP().toBigInteger());
            BigInteger b = number.multiply(MathUtils.modPow(openKey.getY().toBigInteger(), sessionKey, openKey.getP().toBigInteger())).mod(openKey.getP().toBigInteger());
            resultA.add(a.toString(16));
            resultB.add(b.toString(16));
        }

        return new GamalCryptoResult(String.join(",", resultA),String.join(",", resultB), Math.min(chars.length * MIN_BLOCK, byteBlock));
    }
}
