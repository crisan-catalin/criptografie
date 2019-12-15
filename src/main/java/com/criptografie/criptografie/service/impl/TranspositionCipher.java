package com.criptografie.criptografie.service.impl;

import java.util.Arrays;
import java.util.stream.Stream;

public class TranspositionCipher {

    private int key[];
    private int keyAndSortedKeyMapping[];

    public boolean validateKey(String key) {
        return Stream.of(key.toCharArray()).distinct().count() == key.length();
    }

    public String encrypt(String plainText, final String key) {
        prepareKey(key);

        StringBuilder copy = new StringBuilder(plainText);
        while (copy.toString().length() % key.length() != 0) {
            copy.append(" ");
        }
        int maxtrixRows = copy.length() / key.length();
        char[][] plainTextMatrix = new char[maxtrixRows][key.length()];

        int index = 0;
        for (int i = 0; i < maxtrixRows; i++) {
            for (int j = 0; j < key.length(); j++) {
                plainTextMatrix[i][j] = copy.charAt(index++);
            }
        }

        sortKey();

        copy = new StringBuilder(plainText.length());
        for (int i = 0; i < keyAndSortedKeyMapping.length; i++) {
            for (int k = 0; k < maxtrixRows; k++) {
                copy.append(plainTextMatrix[k][this.key[i]]);
            }
        }

        return copy.toString();
    }

    public String decrypt(String encryptedText) {
        int maxtrixRows = encryptedText.length() / key.length;
        char[][] encryptedTextMatrix = new char[maxtrixRows][key.length];
        char[][] decryptedTextMatrix = new char[maxtrixRows][key.length];

        int encryptedTextIndex = 0;
        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < maxtrixRows; j++) {
                encryptedTextMatrix[j][i] = encryptedText.charAt(encryptedTextIndex++);
            }
        }

        for (int i = 0; i < keyAndSortedKeyMapping.length; i++) {
            for (int j = 0; j < maxtrixRows; j++) {
                decryptedTextMatrix[j][i] = encryptedTextMatrix[j][keyAndSortedKeyMapping[i]];
            }
        }

        StringBuilder copy = new StringBuilder(encryptedText.length());
        for (int i = 0; i < maxtrixRows; i++) {
            for (int j = 0; j < key.length; j++) {
                copy.append(decryptedTextMatrix[i][j]);
            }
        }

        return copy.toString().trim();
    }

    /**
     * If key is entered as characters instead of number we need to convert it
     */
    private void prepareKey(String key) {
        this.key = new int[key.length()];
        String[] valuesOfKey = Arrays.stream(key.split("")).sorted().toArray(String[]::new);
        for (int i = 0; i < key.length(); i++) {
            int valueIndex = Arrays.binarySearch(valuesOfKey, String.valueOf(key.charAt(i)));
            this.key[i] = valueIndex;
        }
    }

    //TODO: Explain it
    private void sortKey() {
        int[] sortedKey = Arrays.copyOf(key, key.length);
        Arrays.sort(sortedKey);

        keyAndSortedKeyMapping = new int[key.length];
        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < key.length; j++) {
                if (key[j] == sortedKey[i]) {
                    keyAndSortedKeyMapping[i] = j;
                }
            }
        }
    }
}
