package com.criptografie.criptografie.controller;

import com.criptografie.criptografie.Views;
import com.criptografie.criptografie.service.impl.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.HashMap;

@Controller
@RequestMapping
public class HomeController {
    private final String CIPHER_LIST_ATTR = "CIPHERS_LIST";

    @Autowired
    private CaesarCipher caesarCipher;

    @Autowired
    private AffineCipher affineCipher;

    @Autowired
    private HillCipher hillCipher;

    @Autowired
    private RsaCipher rsaCipher;

    @Autowired
    private TranspositionCipher transpositionCipher;

    @Autowired
    private VernamCipher vernamCipher;

    @Autowired
    private VigenereCipher vigenereCipher;

    @GetMapping("/")
    public String getPage(Model model) {
        model.addAttribute(CIPHER_LIST_ATTR, Ciphers.values());
        return Views.MAIN_PAGE;
    }

    @PostMapping(value = "/encrypt", consumes = {"application/json"})
    public ResponseEntity<String> encrypt(@RequestBody HashMap<String, String> body) {
        int cipherId = Integer.valueOf(body.get("cipherId"));
        String keyA = body.get("keyA");
        String keyB = body.get("keyB");
        String text = body.get("text");
        boolean isValidCipher = Arrays.stream(Ciphers.values()).map(Ciphers::getId).anyMatch(id -> id.equals(cipherId));

        if (!isValidCipher) {
            return ResponseEntity.badRequest().body("Cipher not found.");
        }

        String encryptedText = null;
        if (cipherId == Ciphers.Affine.getId()) {
            if (!NumberUtils.isCreatable(keyA) || !NumberUtils.isCreatable(keyB)) {
                return ResponseEntity.badRequest().body("Key A and Key B must be numbers.");
            }

            int keyA_int = Integer.parseInt(keyA);
            int keyB_int = Integer.parseInt(keyB);
            if (!affineCipher.isValidKeyA(keyA_int)) {
                return ResponseEntity.badRequest().body("Key A must be prime.");
            }
            encryptedText = affineCipher.encrypt(text, keyA_int, keyB_int);
        } else if (cipherId == Ciphers.Caesar.getId()) {
            if (!NumberUtils.isCreatable(keyA)) {
                return ResponseEntity.badRequest().body("Key A must be a number.");
            }
            int keyA_int = Integer.parseInt(keyA);
            encryptedText = caesarCipher.encrypt(text, keyA_int);
        } else if (cipherId == Ciphers.Hill.getId()) {
            //TODO
            return ResponseEntity.badRequest().body("Cipher not implemented.");
        } else if (cipherId == Ciphers.RSA.getId()) {
            if (!NumberUtils.isCreatable(keyA) || !NumberUtils.isCreatable(keyB)) {
                return ResponseEntity.badRequest().body("Key A and Key B must be numbers.");
            }

            int keyA_int = Integer.parseInt(keyA);
            int keyB_int = Integer.parseInt(keyB);
            if (!rsaCipher.isValid(keyA_int, keyB_int)) {
                return ResponseEntity.badRequest().body("Key A and Key B must be prime numbers.");
            }
            encryptedText = rsaCipher.encrypt(text, keyA_int, keyB_int);
        } else if (cipherId == Ciphers.Transposition.getId()) {
            encryptedText = transpositionCipher.encrypt(text, keyA);
        } else if (cipherId == Ciphers.Vernam.getId()) {
            if (!vernamCipher.isValidKey(text, keyA)) {
                return ResponseEntity.badRequest().body("Key length must me at least equal with number of characters.");
            }
            encryptedText = vernamCipher.encrypt(text, keyA);
        } else if (cipherId == Ciphers.Vigenere.getId()) {
            if (!vigenereCipher.isValidKey(keyA)) {
                return ResponseEntity.badRequest().body("Numeric key is not accepted.");
            }
            encryptedText = vigenereCipher.encrypt(text, keyA);
        }

        if (encryptedText == null || encryptedText.isEmpty()) {
            return ResponseEntity.badRequest().body("Something was wrong.");
        }

        return ResponseEntity.ok(encryptedText);
    }

    @PostMapping(value = "/decrypt", consumes = {"application/json"})
    public ResponseEntity<String> decrypt(@RequestBody HashMap<String, String> body) {
        int cipherId = Integer.valueOf(body.get("cipherId"));
        String keyA = body.get("keyA");
        String keyB = body.get("keyB");
        String text = body.get("text");
        boolean isValidCipher = Arrays.stream(Ciphers.values()).map(Ciphers::getId).anyMatch(id -> id.equals(cipherId));

        if (!isValidCipher) {
            return ResponseEntity.badRequest().body("Cipher not found.");
        }

        String decryptedText = null;
        if (cipherId == Ciphers.Affine.getId()) {
            if (!NumberUtils.isCreatable(keyA) || !NumberUtils.isCreatable(keyB)) {
                return ResponseEntity.badRequest().body("Key A and Key B must be numbers.");
            }

            int keyA_int = Integer.parseInt(keyA);
            int keyB_int = Integer.parseInt(keyB);
            if (!affineCipher.isValidKeyA(keyA_int)) {
                return ResponseEntity.badRequest().body("Key A must be prime.");
            }
            decryptedText = affineCipher.decrypt(text, keyA_int, keyB_int);
        } else if (cipherId == Ciphers.Caesar.getId()) {
            if (!NumberUtils.isCreatable(keyA)) {
                return ResponseEntity.badRequest().body("Key A must be a number.");
            }
            int keyA_int = Integer.parseInt(keyA);
            decryptedText = caesarCipher.decrypt(text, keyA_int);
        } else if (cipherId == Ciphers.Hill.getId()) {
            //TODO
            return ResponseEntity.badRequest().body("Cipher not implemented.");
        } else if (cipherId == Ciphers.RSA.getId()) {
            if (!NumberUtils.isCreatable(keyA) || !NumberUtils.isCreatable(keyB)) {
                return ResponseEntity.badRequest().body("Key A and Key B must be numbers.");
            }

            int keyA_int = Integer.parseInt(keyA);
            int keyB_int = Integer.parseInt(keyB);
            if (!rsaCipher.isValid(keyA_int, keyB_int)) {
                return ResponseEntity.badRequest().body("Key A and Key B must be prime numbers.");
            }
            decryptedText = rsaCipher.decrypt(text);
        } else if (cipherId == Ciphers.Transposition.getId()) {
            decryptedText = transpositionCipher.decrypt(text, keyA);
        } else if (cipherId == Ciphers.Vernam.getId()) {
            if (!vernamCipher.isValidKey(text, keyA)) {
                return ResponseEntity.badRequest().body("Key length must me at least equal with number of characters.");
            }
            decryptedText = vernamCipher.decrypt(text, keyA);
        } else if (cipherId == Ciphers.Vigenere.getId()) {
            if (!vigenereCipher.isValidKey(keyA)) {
                return ResponseEntity.badRequest().body("Numeric key is not accepted.");
            }
            decryptedText = vigenereCipher.decrypt(text, keyA);
        }

        if (decryptedText == null || decryptedText.isEmpty()) {
            return ResponseEntity.badRequest().body("Something was wrong.");
        }

        return ResponseEntity.ok(decryptedText);
    }
}
