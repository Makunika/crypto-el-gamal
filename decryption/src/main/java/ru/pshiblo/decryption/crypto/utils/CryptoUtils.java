package ru.pshiblo.encryption.crypto.utils;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * @author Maxim Pshiblo
 */
@UtilityClass
public class CryptoUtils {

    public boolean isPrime(int p) {
        return IntStream.rangeClosed(2, (int) (Math.sqrt(p)))
                .allMatch(n -> p % n != 0);
    }

    public int getPRoot(int p) {
        for (int i = 0; i < p; i++) {
            if (isPRoot(p, i)) {
                return i;
            }
        }
        return 0;
    }

    public boolean isPRoot(int p, int root) {
        if (root == 0 || root == 1) {
            return false;
        }
        int last = 1;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < p - 1; i++) {
            last = (last * root) % p;
            if (set.contains(last)) {
                return false;
            }
            set.add(last);
        }
        return true;
    }

    public int generateRandomPrime() {
        while (true) {
            int random = ThreadLocalRandom.current().nextInt(1000000);
            if (isPrime(random)) {
                return random;
            }
        }
    }
}
