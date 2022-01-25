package ru.pshiblo.elgamal.core.crypto.service;

import org.junit.jupiter.api.Test;
import ru.pshiblo.elgamal.core.crypto.key.GeneratorKey;
import ru.pshiblo.elgamal.core.crypto.key.SecretKey;
import ru.pshiblo.elgamal.core.crypto.service.impl.CryptorServiceImpl;
import ru.pshiblo.elgamal.core.crypto.service.impl.DecryptorServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
                service.encryption("", secretKey.toOpenKey(), 3));

        assertThrows(IllegalArgumentException.class, () ->
                service.encryption(toEncryption, secretKey.toOpenKey(), secretKey.getP().getByteSize()));

        assertThrows(IllegalArgumentException.class, () ->
                service.encryption(toEncryption, secretKey.toOpenKey(), 1));
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

        GamalCryptoResult result1 = cryptorService.encryption(msg, secretKey.toOpenKey(), 2);
        GamalCryptoResult result2 = cryptorService.encryption(msg, secretKey.toOpenKey(), 2);

        assertNotEquals(result1.getA(), result2.getA());
        assertNotEquals(result1.getB(), result2.getB());


        SecretKey secretKey2 = generatePreSecretKey();

        GamalCryptoResult result3 = cryptorService.encryption(msg, secretKey.toOpenKey(), 2);

        String decryption1 = decryptorService.decryption(result3, secretKey);
        String decryption2 = decryptorService.decryption(result3, secretKey2);

        assertNotEquals(decryption1, decryption2);
    }
}