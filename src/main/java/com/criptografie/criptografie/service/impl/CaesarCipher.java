package com.criptografie.criptografie.service.impl;

import org.springframework.stereotype.Service;

import static com.criptografie.criptografie.Constants.*;

@Service
public class CaesarCipher {

    public String encrypt(final String plainText, int offset) {
        StringBuilder copy = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char charToEncrypt = plainText.charAt(i);
            if (charToEncrypt >= FIRST_LETTER_LOWERCASE && charToEncrypt <= LAST_LETTER_LOWERCASE) {
                copy.append((char) ((charToEncrypt + offset - FIRST_LETTER_LOWERCASE) % ALPHABET_LENGTH + FIRST_LETTER_LOWERCASE));
                continue;
            } else if (charToEncrypt >= FIRST_LETTER_UPPERCASE && charToEncrypt <= LAST_LETTER_UPPERCASE) {
                copy.append((char) ((charToEncrypt + offset - FIRST_LETTER_UPPERCASE) % ALPHABET_LENGTH + FIRST_LETTER_UPPERCASE));
                continue;
            }
            copy.append(charToEncrypt);
        }

        return copy.toString();
    }

    public String decrypt(final String cryptedText, int offset) {
        StringBuilder copy = new StringBuilder(cryptedText.length());
        for (int i = 0; i < cryptedText.length(); i++) {
            char charToDecrypt = cryptedText.charAt(i);
            if (charToDecrypt >= FIRST_LETTER_LOWERCASE && charToDecrypt <= LAST_LETTER_LOWERCASE) {
                copy.append((char) (((charToDecrypt - offset - FIRST_LETTER_LOWERCASE) + ALPHABET_LENGTH) % ALPHABET_LENGTH + FIRST_LETTER_LOWERCASE));
                continue;
            } else if (charToDecrypt >= FIRST_LETTER_UPPERCASE && charToDecrypt <= LAST_LETTER_UPPERCASE) {
                copy.append((char) (((charToDecrypt - offset - FIRST_LETTER_UPPERCASE) + ALPHABET_LENGTH) % ALPHABET_LENGTH + FIRST_LETTER_UPPERCASE));
                continue;
            }
            copy.append(charToDecrypt);
        }
        return copy.toString();
    }
}
