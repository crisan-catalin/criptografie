package com.criptografie.criptografie.service.impl;

import com.criptografie.criptografie.Constants;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.criptografie.criptografie.Constants.*;

@Service
public class RsaCipher {

    private long p;
    private long q;
    private long n;
    private long phi;
    private long e;
    private long d;

    private Random random;

    public RsaCipher() {
        random = new Random();
    }

    public boolean isValid(String text) {
        return text.length() >= 4;
    }

    public String encrypt(String plainText) {
        p = randomPrimeNumber(1, plainText.length() / 2);
        q = randomPrimeNumber(1, plainText.length() - plainText.length() / 2);
        n = p * q;
        phi = (p - 1) * (q - 1);
        e = 0;
        d = 0;

        do {
            e = randomNumber(1, phi);
            d = inverseMultiplication((int) e, (int) phi);
        } while (cmmdc(e, phi) != 1);

        StringBuilder copy = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char charToEncrypt = plainText.charAt(i);
            if (charToEncrypt >= FIRST_LETTER_LOWERCASE && charToEncrypt <= LAST_LETTER_LOWERCASE) {
                copy.append(((char) (modulo(charToEncrypt - FIRST_LETTER_LOWERCASE, e, n) % Constants.ALPHABET_LENGTH + FIRST_LETTER_LOWERCASE)));
            } else if (charToEncrypt >= FIRST_LETTER_UPPERCASE && charToEncrypt <= LAST_LETTER_UPPERCASE) {
                copy.append(((char) (modulo(charToEncrypt - FIRST_LETTER_UPPERCASE, e, n) % Constants.ALPHABET_LENGTH + FIRST_LETTER_UPPERCASE)));
            }
        }

        return copy.toString();
    }

    public String decrypt(String encryptedText) {
        StringBuilder copy = new StringBuilder(encryptedText.length());
        for (int i = 0; i < encryptedText.length(); i++) {
            char charToDecrypt = encryptedText.charAt(i);
            if (charToDecrypt >= FIRST_LETTER_LOWERCASE && charToDecrypt <= LAST_LETTER_LOWERCASE) {
                copy.append(((char) (modulo(charToDecrypt - FIRST_LETTER_LOWERCASE, d, n) % Constants.ALPHABET_LENGTH + FIRST_LETTER_LOWERCASE)));
            } else if (charToDecrypt >= FIRST_LETTER_UPPERCASE && charToDecrypt <= LAST_LETTER_UPPERCASE) {
                copy.append((char) (modulo(charToDecrypt - FIRST_LETTER_UPPERCASE, d, n) % Constants.ALPHABET_LENGTH + FIRST_LETTER_UPPERCASE));
            }
        }

        return copy.toString();
    }

    private int inverseMultiplication(int a, int n) {
        int t0 = 0, t1 = 1;
        int r0 = n, r1 = a;
        while (r1 != 0) {
            int c = r0 / r1;
            int t0_temp = t0;
            int t1_temp = t1;
            int r0_temp = r0;
            int r1_temp = r1;
            t0 = t1_temp;
            t1 = t0_temp - c * t1_temp;
            r0 = r1_temp;
            r1 = r0_temp - c * r1_temp;

        }
        if (t0 < 0) {
            t0 += n;
        }
        return t0;
    }

    private long cmmdc(long a, long b) {
        return b == 0 ? a : cmmdc(b, a % b);
    }

    private long randomNumber(long a, long b) {
        return (random.nextInt() & Integer.MAX_VALUE) % b + a;
    }

    private long randomPrimeNumber(long a, long b) {
        int iterations = 20;
        long number;
        do {
            number = ((random.nextInt() & Integer.MAX_VALUE)) % b + a;
            if (fermat(number, iterations)) {
                return number;
            }
        } while (true);
    }

    private boolean fermat(long p, int iterations) {
        if (p == 1)
            return false;

        for (int i = 0; i < iterations; i++) {
            long a = (long) ((random.nextInt() & Integer.MAX_VALUE) % (p - 1) + 1);
            if (modulo(a, p - 1, p) != 1) {
                return false;
            }
        }
        return true;
    }

    private long modulo(long base, long exponent, long mod) {
        long x = 1;
        long y = base;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                x = (x * y) % mod;
            }
            y = (y * y) % mod;
            exponent /= 2;
        }
        return x % mod;
    }
}
