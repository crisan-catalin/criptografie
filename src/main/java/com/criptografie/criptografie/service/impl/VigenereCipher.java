package com.criptografie.criptografie.service.impl;

import com.criptografie.criptografie.Constants;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import static com.criptografie.criptografie.Constants.*;

@Service
public class VigenereCipher {

    private static final int CHARACTER_SPACE = 32;
    private static String key;
    private static char[][] table;

    public boolean isValidKey(String key) {
        char[] charArr = key.toCharArray();
        for (char ch : charArr) {
            if (Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    public String encrypt(String plainText, String key) {
        initializeContext(key);

        StringBuilder copy = new StringBuilder(plainText.length());
        int characterIndex = 0;
        for (int i = 0; i < plainText.length(); i++) {
            char charToEncrypt = plainText.charAt(i);
            if (charToEncrypt != CHARACTER_SPACE) {
                int charAlphabetPosition = 0;
                if (charToEncrypt >= FIRST_LETTER_LOWERCASE && charToEncrypt <= LAST_LETTER_LOWERCASE) {
                    charAlphabetPosition = charToEncrypt - 'a';
                } else if (charToEncrypt >= FIRST_LETTER_UPPERCASE && charToEncrypt <= LAST_LETTER_UPPERCASE) {
                    charAlphabetPosition = charToEncrypt - 'A';
                }
                copy.append(table[characterIndex % VigenereCipher.key.length()][charAlphabetPosition]);
                characterIndex++;
                continue;
            }
            copy.append(charToEncrypt);
        }
        return copy.toString();
    }

    public String decrypt(String encryptedText, String key) {
        initializeContext(key);

        StringBuilder copy = new StringBuilder(encryptedText.length());
        for (int i = 0; i < encryptedText.length(); i++) {
            char charToDecrypt = encryptedText.charAt(i);
            if (charToDecrypt == CHARACTER_SPACE) {
                copy.append(charToDecrypt);
                continue;
            }

            int decryptPositionInTable = 0;
            for (int k = 0; k < ALPHABET_LENGTH; k++) {
                if (table[i % key.length()][k] == charToDecrypt) {
                    decryptPositionInTable = k;
                    break;
                }
            }
            copy.append((char) (decryptPositionInTable + 'a'));

        }
        return copy.toString();
    }

    private void initializeContext(String key) {
        VigenereCipher.key = key.replace(" ", "");
        table = new char[VigenereCipher.key.length()][Constants.ALPHABET_LENGTH];
        for (int i = 0; i < VigenereCipher.key.length(); i++) {
            for (int j = 0; j < Constants.ALPHABET_LENGTH; j++) {
                char charOfKey = VigenereCipher.key.charAt(i);
                if (charOfKey >= FIRST_LETTER_LOWERCASE && charOfKey <= LAST_LETTER_LOWERCASE) {
                    table[i][j] = (char) ((charOfKey + j - 'a') % ALPHABET_LENGTH + 'a');
                    continue;
                }
                if (charOfKey >= FIRST_LETTER_UPPERCASE && charOfKey <= LAST_LETTER_UPPERCASE) {
                    table[i][j] = (char) ((charOfKey + j - 'A') % ALPHABET_LENGTH + 'A');
                }
            }
        }
    }
}
