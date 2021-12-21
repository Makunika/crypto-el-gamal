package ru.pshiblo.elgamal.core.crypto;

import ru.pshiblo.elgamal.core.crypto.impl.GamalCryptoResult;
import ru.pshiblo.elgamal.core.crypto.key.OpenKey;

/**
 * @author Maxim Pshiblo
 */
public interface Cryptor {
    GamalCryptoResult encryption(String str, OpenKey openKey, int byteBlock);
}
