package com.criptografie.criptografie.service.impl;

import org.springframework.stereotype.Service;

@Service
public class VernamCipher {

    private static int[][] keyAsBitsMatrix;
    private static int[][] plainTextAsBitsMatrix;

    public boolean isValidKey(String plainText, String key) {
        return key.length() >= plainText.length();
    }

    public String encrypt(String plainText, String key) {
        keyAsBitsMatrix = stringToBitsMatrix(key);
        plainTextAsBitsMatrix = stringToBitsMatrix(plainText);

        int[][] copy = new int[plainTextAsBitsMatrix.length][8];
        for (int i = 0; i < plainText.length(); i++) {
            for (int j = 0; j < 8; j++) {
                copy[i][j] = plainTextAsBitsMatrix[i][j] ^ keyAsBitsMatrix[i][j];
            }
        }

        return bitsMatrixToString(copy);
    }


    public String decrypt(String encryptedText, String key) {
        keyAsBitsMatrix = stringToBitsMatrix(key);

        int[][] cryptedTextAsBitsMatrix = stringToBitsMatrix(encryptedText);
        int[][] decryptedTextAsBitsMatrix = new int[cryptedTextAsBitsMatrix.length][8];

        for (int i = 0; i < encryptedText.length(); i++) {
            for (int j = 0; j < 8; j++) {
                decryptedTextAsBitsMatrix[i][j] = cryptedTextAsBitsMatrix[i][j] ^ keyAsBitsMatrix[i][j];
            }
        }
        return bitsMatrixToString(decryptedTextAsBitsMatrix);
    }

    private int[][] stringToBitsMatrix(String string) {
        int rest;
        int[][] bitsMatrix = new int[string.length()][8];

        for (int i = 0; i < string.length(); i++) {
            char charOfString = string.charAt(i);
            for (int j = 0; j < 8; j++) {
                rest = charOfString % 2;
                charOfString = (char) (charOfString / 2);
                bitsMatrix[i][j] = rest;
            }
        }
        return bitsMatrix;
    }

    private String bitsMatrixToString(int[][] bitsMatrix) {
        StringBuilder copy = new StringBuilder(bitsMatrix.length);
        for (int i = 0; i < bitsMatrix.length; i++) {
            int sum = 0;
            for (int j = 7; j >= 0; j--) {
                sum += bitsMatrix[i][j] * Math.pow(2, j);
            }
            copy.append((char) sum);
        }
        return copy.toString();
    }
}
