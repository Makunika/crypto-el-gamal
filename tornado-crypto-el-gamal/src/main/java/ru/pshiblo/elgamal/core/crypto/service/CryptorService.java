package ru.pshiblo.elgamal.core.crypto.service;

import ru.pshiblo.elgamal.core.crypto.key.OpenKey;

/**
 * @author Maxim Pshiblo
 */
public interface CryptorService {
    GamalCryptoResult encryption(String str, OpenKey openKey, int byteBlock);
}
