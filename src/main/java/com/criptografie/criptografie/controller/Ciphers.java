package com.criptografie.criptografie.controller;

public enum Ciphers {
    Affine(0, "Affine cipher"),
    Caesar(1, "Caesar cipher"),
    Hill(2, "Hill cipher"),
    RSA(3, "RSA cipher"),
    Transposition(4, "Transposition cipher"),
    Vernam(5, "Vernam cipher"),
    Vigenere(6, "Vigenere cipher");

    private int id;
    private String name;

    Ciphers(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
