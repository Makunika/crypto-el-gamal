package ru.pshiblo.elgamal.core.utils;

import org.junit.jupiter.api.Test;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void reverseArray() {

        byte[] array = {1, 2, 3, 4, 5};
        byte[] exceptedArray = {5, 4, 3, 2, 1};
        Utils.reverseArray(array);
        assertArrayEquals(array, exceptedArray);
        Utils.reverseArray(null);
        Utils.reverseArray(new byte[0]);
    }
}