package ru.pshiblo.encryption.crypto.service;

import org.springframework.stereotype.Service;
import ru.pshiblo.encryption.crypto.key.KeyGamal;
import ru.pshiblo.encryption.crypto.service.interfaces.CryptoService;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Maxim Pshiblo
 */
@Service
public class CryptoGamalService implements CryptoService<KeyGamal> {

    @Override
    public int encrypt(int number, KeyGamal key) {
        if (number > key.getP()) {
            throw new IllegalArgumentException("сообщение должно быть больше p");
        }
        int sessionKey = ThreadLocalRandom.current().nextInt(2, key.getP() - 1);
        int a = Math.floorMod(((int) Math.pow(key.getG(), sessionKey)), key.getP());
        int b = Math.floorMod(((int) Math.pow(key.getY(), sessionKey)), key.getP());
    }
}
