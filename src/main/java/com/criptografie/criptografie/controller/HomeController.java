package com.criptografie.criptografie.controller;

import com.criptografie.criptografie.Views;
import com.criptografie.criptografie.service.impl.CaesarCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;

@Controller
@RequestMapping
public class HomeController {
    private final String CIPHER_LIST_ATTR = "CIPHERS_LIST";

    @Autowired
    private CaesarCipher caesarCipher;

    @GetMapping("/")
    public String getPage(Model model) {
        model.addAttribute(CIPHER_LIST_ATTR, Ciphers.values());
        return Views.MAIN_PAGE;
    }

    @PostMapping(value = "/encrypt", consumes = {"application/json"})
    public ResponseEntity<String> encrypt(@RequestBody HashMap<String, String> body) {
        int cipherId = Integer.valueOf(body.get("cipherId"));
        int offset = Integer.valueOf(body.get("offset"));
        String text = body.get("text");
        boolean isValidCipher = Arrays.stream(Ciphers.values()).map(Ciphers::getId).anyMatch(id -> id.equals(cipherId));

        if (!isValidCipher) {
            return ResponseEntity.badRequest().body("Invalid cipher");
        }

        String encryptedText = caesarCipher.encrypt(text, offset);
        return ResponseEntity.status(HttpStatus.OK).body(encryptedText);
    }


    @PostMapping("/decrypt")
    public void decrypt() {

    }
}
