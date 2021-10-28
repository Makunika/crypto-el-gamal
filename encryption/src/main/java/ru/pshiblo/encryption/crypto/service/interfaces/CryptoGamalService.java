package ru.pshiblo.encryption.crypto.service.interfaces;

/**
 * @author Maxim Pshiblo
 */
public interface CryptoService<K> {
    int encrypt(int number, K key);
}
