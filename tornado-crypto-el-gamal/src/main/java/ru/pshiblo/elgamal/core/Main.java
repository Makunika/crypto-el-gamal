package ru.pshiblo.elgamal.core;

import ru.pshiblo.elgamal.core.crypto.Decryptor;
import ru.pshiblo.elgamal.core.crypto.impl.CryptorImpl;
import ru.pshiblo.elgamal.core.crypto.impl.DecryptorImpl;
import ru.pshiblo.elgamal.core.crypto.impl.GamalCryptoResult;
import ru.pshiblo.elgamal.core.crypto.key.GeneratorKey;
import ru.pshiblo.elgamal.core.crypto.key.SecretKey;

/**
 * @author Maxim Pshiblo
 */
public class Main {
    public static void main(String[] args) {
        SecretKey secretKey = GeneratorKey.getInstance().generateNewKey(1024);
        CryptorImpl crypto = new CryptorImpl();
        GamalCryptoResult encrypt = crypto.encryption("хуйййййййййй", secretKey.toOpenKey(), 1023 / 8);
        Decryptor decryptor = new DecryptorImpl();
        String decryption = decryptor.decryption(encrypt, secretKey);
        System.out.println(encrypt);
        System.out.println(decryption);
        System.out.println("хуйййййййййй");
    }
}
