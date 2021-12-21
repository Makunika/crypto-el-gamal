package ru.pshiblo.elgamal.core.crypto;

import ru.pshiblo.elgamal.core.crypto.impl.GamalCryptoResult;
import ru.pshiblo.elgamal.core.crypto.key.SecretKey;

/**
 * @author Maxim Pshiblo
 */
public interface Decryptor {
    String decryption(GamalCryptoResult msg, SecretKey secretKey);
}
