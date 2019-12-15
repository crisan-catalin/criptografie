package com.criptografie.criptografie.service.impl;

import org.springframework.stereotype.Service;

import static com.criptografie.criptografie.Constants.*;

@Service
public class AffineCipher {

    private static int keyA;
    private static int keyB;

    public boolean isValidKeyA(int keyA) {
        return isPrime(keyA, ALPHABET_LENGTH);
    }

    public String encrypt(String plainText, int keyA, int keyB) {
        initializeContext(keyA, keyB);

        StringBuilder copy = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char charToEncrypt = plainText.charAt(i);
            if (charToEncrypt >= FIRST_LETTER_LOWERCASE && charToEncrypt <= LAST_LETTER_LOWERCASE) {
                copy.append((char) ((keyA * (charToEncrypt - FIRST_LETTER_LOWERCASE) + keyB) % ALPHABET_LENGTH + FIRST_LETTER_LOWERCASE));
                continue;
            }
            if (charToEncrypt >= FIRST_LETTER_UPPERCASE && charToEncrypt <= LAST_LETTER_UPPERCASE) {
                copy.append((char) ((keyA * (charToEncrypt - FIRST_LETTER_UPPERCASE) + keyB) % ALPHABET_LENGTH + LAST_LETTER_UPPERCASE));
                continue;
            }
            copy.append(charToEncrypt);
        }
        return copy.toString();
    }

    private void initializeContext(int keyA, int keyB) {
        this.keyA = keyA;
        this.keyB = keyB;
    }

    public String decrypt(String cryptedText) {
        int symmetricKey = findSymmetricKeyOf(keyA);
        StringBuilder copy = new StringBuilder(cryptedText.length());

        for (int i = 0; i < cryptedText.length(); i++) {
            char charToDecrypt = cryptedText.charAt(i);
            if (charToDecrypt >= FIRST_LETTER_LOWERCASE && charToDecrypt <= LAST_LETTER_LOWERCASE) {
                copy.append((char) (symmetricKey * (charToDecrypt - FIRST_LETTER_LOWERCASE - keyB + ALPHABET_LENGTH) % ALPHABET_LENGTH + FIRST_LETTER_LOWERCASE));
                continue;
            }
            if (charToDecrypt >= FIRST_LETTER_UPPERCASE && charToDecrypt <= LAST_LETTER_UPPERCASE) {
                copy.append((char) (symmetricKey * (charToDecrypt - FIRST_LETTER_UPPERCASE - keyB + ALPHABET_LENGTH) % ALPHABET_LENGTH + LAST_LETTER_UPPERCASE));
                continue;
            }
            copy.append(charToDecrypt);
        }
        return copy.toString();
    }

    private int findSymmetricKeyOf(int keyA) {
        int symmetricNumber = 1;
        while (symmetricNumber * keyA % ALPHABET_LENGTH != 1) {
            symmetricNumber++;
        }
        return symmetricNumber;
    }

    private int cmmdc(int n1, int n2) {
        while (n1 != n2) {
            if (n1 > n2) {
                n1 -= n2;
            } else {
                n2 -= n1;
            }
        }
        return n1;
    }

    private boolean isPrime(int n1, int n2) {
        return cmmdc(n1, n2) == 1;
    }
}
