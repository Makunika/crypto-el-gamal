package ru.pshiblo.elgamal.core.utils;

/**
 * @author Maxim Pshiblo
 */
public class Utils {
    public static void reverseArray(byte[] array) {
        if (array == null) {
            return;
        }
        for (int k = 0; k < array.length / 2; k++) {
            byte temp = array[k];
            array[k] = array[array.length - 1 - k];
            array[array.length - 1 - k] = temp;
        }
    }
}
