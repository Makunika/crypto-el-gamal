package ru.pshiblo.elgamal.core.crypto.service;

import ru.pshiblo.elgamal.core.crypto.key.SecretKey;

/**
 * @author Maxim Pshiblo
 */
public interface DecryptorService {
    String decryption(GamalCryptoResult msg, SecretKey secretKey);
}
