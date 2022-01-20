package ru.pshiblo.elgamal.core.crypto.service;

import org.junit.jupiter.api.Test;
import ru.pshiblo.elgamal.core.crypto.key.GeneratorKey;
import ru.pshiblo.elgamal.core.crypto.key.SecretKey;
import ru.pshiblo.elgamal.core.crypto.service.impl.CryptorServiceImpl;
import ru.pshiblo.elgamal.core.crypto.service.impl.DecryptorServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CryptorAndDecryptorServiceTest {

    SecretKey generatePreSecretKey() {
        GeneratorKey generatorKey = new GeneratorKey();
        return generatorKey.generateNewKey(32);
    }

    @Test
    void encryption() {
        CryptorService service = new CryptorServiceImpl();
        SecretKey secretKey = generatePreSecretKey();
        String toEncryption = String.valueOf((char) 8);
        service.encryption(toEncryption, secretKey.toOpenKey(), 3);

        assertThrows(IllegalArgumentException.class, () ->
                service.encryption(null, secretKey.toOpenKey(), 3));

        assertThrows(IllegalArgumentException.class, () ->
                service.encryption(toEncryption, null, 3));

        assertThrows(IllegalArgumentException.class, () ->
                service.encryption(toEncryption, secretKey.toOpenKey(), secretKey.getP().getByteSize()));
    }

    @Test
    void decryption() {
        DecryptorService service = new DecryptorServiceImpl();
        SecretKey secretKey = generatePreSecretKey();

        assertThrows(IllegalArgumentException.class, () -> service.decryption(null, secretKey));
        assertThrows(IllegalArgumentException.class, () -> service.decryption(new GamalCryptoResult("13", "12", 2), null));
    }

    @Test
    void encryptionAndDecryption() {
        CryptorService cryptorService = new CryptorServiceImpl();
        DecryptorService decryptorService = new DecryptorServiceImpl();
        SecretKey secretKey = generatePreSecretKey();
        String msg = "hello1";

        GamalCryptoResult cryptoResult = cryptorService.encryption(msg, secretKey.toOpenKey(), 3);
        String result = decryptorService.decryption(cryptoResult, secretKey);

        assertEquals(msg, result);
    }
}