package ru.pshiblo.elgamal.core.crypto.key;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class GeneratorKeyTest {

    @Test
    void generateNewKey() {
        GeneratorKey generatorKey = new GeneratorKey();
        SecretKey secretKey = generatorKey.generateNewKey(1024);
        assertEquals(secretKey.getP().getBitsSize(), 1024);
        assertEquals(secretKey.getP().toBigInteger().bitLength(), 1023);

        assertThrows(IllegalArgumentException.class, () -> generatorKey.generateNewKey(-20));
        assertThrows(IllegalArgumentException.class, () -> generatorKey.generateNewKey(1));
    }
}