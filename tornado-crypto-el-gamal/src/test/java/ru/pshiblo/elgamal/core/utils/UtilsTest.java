package ru.pshiblo.elgamal.core.utils;

import org.junit.jupiter.api.Test;

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